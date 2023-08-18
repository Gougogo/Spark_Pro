package com.goumi.day12

import com.goumi.utils.{DateUtils, SparkUtils, UidPartitioner, UidPartitioner02}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

import java.text.{ParseException, SimpleDateFormat}

object FlowCount2 {
  /*
  uid,start,end,flow
  1,2020-02-18 14:20:30,   2020-02-18 14:46:30,  2020-02-18 14:20:30    ,20     ,0
  1,2020-02-18 14:47:20,   2020-02-18 15:20:30,  2020-02-18 14:46:30    ,30     ,0
  1,2020-02-18 15:37:23,   2020-02-18 16:05:26,  2020-02-18 15:20:30    ,40     ,1
  1,2020-02-18 16:06:27,   2020-02-18 17:20:49,  2020-02-18 16:05:26    ,50     ,0
  1,2020-02-18 17:21:50,   2020-02-18 18:03:27,  2020-02-18 17:20:49    ,60.0   ,0

  2,2020-02-18 14:18:24,2020-02-18 15:01:40,20
  2,2020-02-18 15:20:49,2020-02-18 15:30:24,30
  2,2020-02-18 16:01:23,2020-02-18 16:40:32,40
  2,2020-02-18 16:44:56,2020-02-18 17:40:52,50
  3,2020-02-18 14:39:58,2020-02-18 15:35:53,20
  3,2020-02-18 15:36:39,2020-02-18 15:24:54,30

  tmp = 0

  0     0
  0     0
  1     1
  0     1
  0     1
  */

  val sc: SparkContext = SparkUtils.createContext()

  //首先对于原始数据，应该有个数据校验
  //只有处理的目标数据格式正确之后，才能进一步处理
  def checkLines(lines: RDD[String]): RDD[(Boolean, Long)] = {

    val dataWithIndex: RDD[(String, Long)] = lines.zipWithIndex()

    val linesOk: RDD[(Boolean, Long)] = dataWithIndex.map(e => {
      if (!e._1.matches(".*?(,).*?(,).*?(,).*?")) {
        (false, e._2)
      }
      else {
        val dateObj = new DateUtils("yyyy-MM-dd HH:mm:ss")

        val start: String = e._1.split(",")(1)
        val end: String = e._1.split(",")(2)
        var startNum: Long = 0
        var endNum: Long = 0

        try {
          startNum = dateObj.parseDateToNums(start)
        } catch {
          case e: ParseException => {
            startNum = 0
          }
        }

        try {
          endNum = dateObj.parseDateToNums(end)
        } catch {
          case e: ParseException => {
            endNum = 0
          }
        }

        if (startNum < endNum && startNum != 0 && endNum != 0) {
          //println("true")
          (true,e._2)
        } else {
          //println("parse false")
          (false, e._2)
        }
      }
    })

    linesOk
  }

  def changeLines(rdd1: RDD[(String, (String, String, Int, Long, Long))], uids: Array[String]) ={
    val rdd2: RDD[(String, (String, String, Int, Long, Long))]
    = rdd1.repartitionAndSortWithinPartitions(new UidPartitioner(uids))

    val rdd3: RDD[(String, String)] =
      rdd2.map(e => (e._1, e._2._2))

    val rdd4: RDD[(String, String)] = rdd2.map(e => (e._1, e._2._1)).mapPartitions(datas => {
      datas.zipWithIndex.filter(e => e._2 == 0).map(e => (e._1._1, e._1._2))
    })
    //有个问题是用partitionBy，还是repartitionAndSortWithinPartitions
    //rdd4.saveAsTextFile("out/rdd4")
    val rdd5: RDD[((Long, String), Long)] = rdd4.union(rdd3).partitionBy(new UidPartitioner(uids)).mapPartitions((datas: Iterator[(String, String)]) =>{
      val list: List[(String, String)] = datas.toList
      val tuples: List[((String, String), Int)] = list.zipWithIndex.filter(e => e._2 != list.size-1)
      tuples.toIterator
    }).map(e=>{
      val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      ((e._2.toLong, e._1._1), dateFormat.parse(e._1._2).getTime)
    })

    //rdd5.saveAsTextFile("out/rdd5")

    val rdd6: RDD[((Long, String), (String, String, Int, Long, Long))] =
      rdd2.partitionBy(new UidPartitioner(uids))
        .mapPartitions(datas=> {
          datas.zipWithIndex
            .map(e => ((e._2, e._1._1), (e._1._2._1, e._1._2._2, e._1._2._3, e._1._2._4, e._1._2._5)))
        })

    implicit val ord = Ordering[String].on[((Long, String), ((String, String, Int, Long, Long), String))](t=>t._2._1._2)
    val rdd7: RDD[((Long, String), ((String, String, Int, Long, Long), Long))] =
      rdd6.join(rdd5)
        .repartitionAndSortWithinPartitions(new UidPartitioner02(uids))
    //rdd7.saveAsTextFile("out/rdd71")
    rdd7
  }

  def getBinaryLines(rdd7: RDD[((Long, String), ((String, String, Int, Long, Long), Long))]) = {
    val rdd9: RDD[((Long, String), (((String, String, Int, Long, Long), Long), Int))] = rdd7.mapPartitions(e => {
      e.map(e => {
        if (e._2._1._4 - e._2._2 > 600000)
          (e._1, (e._2, 1))
        else
          (e._1, (e._2, 0))
      })
    })
    //rdd9.saveAsTextFile("out/rdd9")
    rdd9
  }

  def accBinaryGroupAndGetRes(rdd8 :RDD[((Long, String), (((String, String, Int, Long, Long), Long), Int))], uids: Array[String])={
    rdd8.mapPartitions(e=>{
      var tmp = 0
      e.map(e=>{
        tmp += e._2._2
        ((e._1._2,tmp), (e._1._1, e._2._1._1._1, e._2._1._1._2, e._2._1._1._3))
      })
    }).reduceByKey((a,b)=>{
      val total = a._4+b._4
      if (a._1 < b._1) {
        (a._1, a._2, b._3, total)
      }else{
        (a._1, b._2, a._3, total)
      }
    }).map(e=>(e._1._1, e._2))
      .partitionBy(new UidPartitioner(uids))
    .saveAsTextFile("out/rdd102")
  }

  def main(args: Array[String]): Unit = {
    val lines: RDD[String] = sc.textFile("in/dataWithHeader.csv")
    val rdd1: RDD[(String, (String, String, Int, Long, Long))] = lines.mapPartitions(it => {
      val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      it.map((e: String) => {
        val fields = e.split(",")
        val uid = fields(0)
        val start = fields(1)
        val end = fields(2)
        val flow = fields(3).toInt
        val startTime = dateFormat.parse(start).getTime
        val endTime = dateFormat.parse(end).getTime
        (uid, (start, end, flow, startTime, endTime))
      })
    })
    val uids: Array[String] = rdd1.map(e => e._1).collect().distinct

    checkLines(lines).map(e=>{
      if(!e._1)
        println("error line:" + (e._2 + 1))
    }).collect()

    val rdd8 = changeLines(rdd1, uids)

    val rdd9 = getBinaryLines(rdd8)
    accBinaryGroupAndGetRes(rdd9,uids)
    //Thread.sleep(1000000000)
  }
}
