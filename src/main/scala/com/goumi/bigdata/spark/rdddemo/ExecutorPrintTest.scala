package com.goumi.bigdata.spark.rdddemo

import com.goumi.utils.SparkUtils
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object ExecutorPrintTest {
  def main(args: Array[String]): Unit = {
    val sc: SparkContext = SparkUtils.createContext()
    val lines: RDD[String] = sc.textFile("in/category.log")
    val rdd1: RDD[Unit] = lines.map(e => {
      println("asdasdasda")
    })

    rdd1.collect()

    Thread.sleep(10000000)
  }
}
