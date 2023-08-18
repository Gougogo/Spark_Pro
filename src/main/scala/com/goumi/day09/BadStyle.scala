package com.goumi.day09

import com.goumi.utils.SparkUtils
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object BadStyle {
  def main(args: Array[String]): Unit = {
    val sc: SparkContext = SparkUtils.createContext()
    val rdd1: RDD[Int] = sc.parallelize(List(1, 2, 3, 4, 5, 6, 7))
    val rdd2: RDD[String] = sc.parallelize(List("word", "spark", "hadoop", "word"))
    //val rdd2Count: Long = rdd2.count()

    println(rdd1.map(e => {
      //行动算子得在Driver端执行，因为要在Driver做一些准备工作
      (e, rdd2.count())
      //(e, rdd2Count)

    }).collect().toBuffer)

  }

}
