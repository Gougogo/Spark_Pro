package com.goumi.day11

import com.goumi.utils.SparkUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkContext}

import scala.collection.mutable

object SellCount02 {
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

    //这里有个问题，key一旦设置了，就不能变，但如果有需求是根据Key中的一部分作为Key进行聚合呢？
    //所以这里是先将Key处理好之后再reduceByKey
    val rdd2: RDD[(String, String, Double)] =
      sourceRDD.map(e => ((e._1, e._2.substring(0, 7)), e._3))
        .reduceByKey(_ + _)
        //.combineByKey()
        .map(e => (e._1._1, e._1._2, e._2))

    rdd2.saveAsTextFile("out/out22")

    rdd2.groupBy(_._1).mapValues(it=>{
      val sorted: List[(String, String, Double)] = it.toList.sortBy(_._2)
      var rollup = 0.0
      sorted.map(t=>{
        val sid: String = t._1
        val mth: String = t._2
        val mth_sales: Double = t._3
        rollup += mth_sales
        (sid, mth, mth_sales, rollup)
      })
    }).flatMapValues(lst => lst)
      .map(e=>(e._1, e._2._2, e._2._3, e._2._4))
      .saveAsTextFile("out/out23")
  }
}
