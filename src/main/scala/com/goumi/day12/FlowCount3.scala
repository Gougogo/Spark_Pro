package com.goumi.day12

import com.goumi.utils.{SparkUtils, UidPartitioner03}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

import java.text.{ParseException, SimpleDateFormat}

object FlowCount3 {
  val sc: SparkContext = SparkUtils.createContext()


  def main(args: Array[String]): Unit = {
    val lines: RDD[String] = sc.textFile("in/dataWithHeader.csv")

    val rdd1 = lines.mapPartitions(it => {
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


    rdd1.groupByKey().flatMapValues(it=>{
      val sorted: List[(String, String, Int, Long, Long)] = it.toList.sortBy(_._1)
      var flag = 0
      var index = 0
      var startTime = 0.0
      var endTime = 0.0
      var sum = 0
      sorted.map(e=>{
        if (index == 0)
          endTime = e._5
        startTime = e._4
        if (startTime -  endTime > 600000)
          flag = 1
        else{
          flag = 0
        }

        endTime = e._5
        index += 1
        sum += flag
        (e, flag, sum)
      })
    }).map(e=>((e._1, e._2._3), (e._2._1._1, e._2._1._2, e._2._1._3)))
      .reduceByKey((a,b)=>{
        val total = a._3 + b._3
        if (a._1 < b._1)
          (a._1, b._2, total)
        else
          (b._1, a._2, total)
      }).map(e=>((e._1._1,e._2._1, e._2._2), e._2._3))
      .repartitionAndSortWithinPartitions(new UidPartitioner03(uids))
      .saveAsTextFile("out/rdd1")
    //Thread.sleep(1000000000)
  }
}
