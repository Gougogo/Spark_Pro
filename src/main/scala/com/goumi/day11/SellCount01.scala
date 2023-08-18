package com.goumi.day11

import com.goumi.utils.{SparkUtils, UidPartitioner}
import org.apache.spark.{Partitioner, SparkContext}
import org.apache.spark.rdd.RDD

import scala.collection.mutable

object SellCount01 {
  def main(args: Array[String]): Unit = {
    /*
    shop1,2021-01-18,500
    shop1,2021-02-10,500
    shop1,2021-02-10,200
    shop1,2021-02-11,600
    shop1,2021-02-12,400
    shop1,2021-02-13,200
    shop1,2021-02-15,100
    shop1,2021-03-05,180
    shop1,2021-04-05,280
    shop1,2021-04-06,220
    =>
    shop1,1000
    shop1,2021-01-18,500
    shop1,2021-02-10,700

    shop2,2021-02-10,100
    shop2,2021-02-11,100
    shop2,2021-02-13,100

    */

    val sc: SparkContext = SparkUtils.createContext()
    val lines: RDD[String] = sc.textFile("in/shop.txt")

    val sourceRDD: RDD[(String, String, Double)] = lines.map(e => {
      val fields: Array[String] = e.split(",")

      val shop: String = fields(0)
      val date: String = fields(1)
      val money: Double = fields(2).toDouble
      (shop, date, money)
    })

    val uids: Array[String] = sourceRDD.map(e => e._1).collect().distinct

    val rdd1: RDD[(String, String, Double)] =
      sourceRDD.map(e => (e._1, e._3)).reduceByKey(_ + _).map(e => (e._1, "", e._2))
    //sourceRDD.map(e=>(e._1, (e._2, e._3))).reduceByKey((a, b)=>("", a._2+b._2))

    //这里有个问题，key一旦设置了，就不能变，但如果有需求是根据Key中的一部分作为Key进行聚合呢？
    //所以这里是先将Key处理好之后再reduceByKey
    val rdd2: RDD[(String, String, Double)] =
      sourceRDD.map(e => ((e._1, e._2.substring(0, 7)), e._3))
        .reduceByKey(_ + _)
        //.combineByKey()
        .map(e => (e._1._1, e._1._2, e._2))

    val res: RDD[(String, String, Double)] = rdd1.union(rdd2)

    implicit val ord = Ordering[String].on[(String, (String, Double))](t => t._2._1)
    res.map(e=>(e._1, (e._2, e._3)))
      .map(e=>(e.swap, null))
      .repartitionAndSortWithinPartitions(new UidPartitioner03(uids))
      .map(e=>(e._1._2, e._1._1._1, e._1._1._2))
      .saveAsTextFile("out/out18")

    //println(res.collect().toBuffer)
  }

  class UidPartitioner03(uids:Array[String]) extends Partitioner{
    override def numPartitions: Int = uids.size

    private val uidToIndex = new mutable.HashMap[String, Int]()

    var index:Int = 0

    for (elem <- uids) {
      uidToIndex(elem) = index
      index+=1
    }

    override def getPartition(key: Any): Int = {
      uidToIndex(key.asInstanceOf[((String, Double), String)]._2)
    }
  }
}
