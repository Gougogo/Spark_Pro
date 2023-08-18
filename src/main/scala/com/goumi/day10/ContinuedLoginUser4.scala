package com.goumi.day10

import com.goumi.utils.{DateUtils, SparkUtils, UidPartitioner}
import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkContext}

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

object ContinuedLoginUser4 {
  def main(args: Array[String]): Unit = {
    val sc: SparkContext = SparkUtils.createContext()
    val lines: RDD[String] = sc.textFile("in/data1.csv")

    //数据预处理
    val uidAndDt: RDD[(String, String)] = lines.map(e => {
      val fields: Array[String] = e.split(",")
      val uid: String = fields(0)
      val dt: String = fields(1)
      (uid, dt)
    }).distinct()

    //这里去重的的原因是前面的去重是针对(uid,dt)去重的，这里想要的是所有的用户
    val uidArr: Array[String] = uidAndDt.keys.distinct().collect()

    val rdd1: RDD[((String, String), Null)] =
      uidAndDt.map((_, null)).repartitionAndSortWithinPartitions(new MyHashPartitioner(2))

    val uidAndDtAndRowNum: RDD[(String, String, Int, String)] = rdd1.mapPartitions(it => {
      var rowNum: Int = 0

      it.map(e => {
        rowNum += 1

        (e._1._1, e._1._2, rowNum, new DateUtils("yyyy-MM-dd").parseDateFromNum(e._1._2, rowNum))
      })
    })

    val rdd2: RDD[((String, String), Iterable[(String, String, Int, String)])] =
      uidAndDtAndRowNum.groupBy(e => (e._1, e._4))

    val rdd3: RDD[(String, String, String, Int)] = rdd2.map(e => {
      implicit val ord = Ordering[String].on[(String, String, Int, String)](t => t._2)
      val start: String = e._2.min._2
      val end: String = e._2.max._2
      (e._1._1, start, end, e._2.size)
    })

    val res = rdd3.map(e=>(e._1,(e._2, e._3, e._4))).partitionBy(new UidPartitioner(uidArr))

    res.saveAsTextFile("out/out15")
  }

  class MyHashPartitioner(partitions : Int) extends Partitioner {

    override def numPartitions: Int = partitions

    private[this] def nonNegativeMod(x: Int, mod: Int): Int = {
      val rawMod = x%mod
      rawMod + (if (rawMod < 0) mod else 0)
    }

    override def getPartition(key: Any): Int = {
      if (key == null){
        1
      }else{
        nonNegativeMod(key.asInstanceOf[(String, String)]._1.hashCode, numPartitions)
      }
      /*case null => 0
      case _ => nonNegativeMod(key.asInstanceOf[(String, String)]._1.hashCode, numPartitions)
      这样用模式匹配居然还报错
      */
    }
  }
}