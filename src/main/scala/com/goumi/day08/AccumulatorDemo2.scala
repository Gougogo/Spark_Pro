package com.goumi.day08

import com.goumi.utils.SparkUtils
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.util.LongAccumulator

object AccumulatorDemo2 {
  def main(args: Array[String]): Unit = {
    val sc: SparkContext = SparkUtils.createContext()
    val arr: Array[Int] = Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val rdd1: RDD[Int] = sc.parallelize(arr)
    sc.collectionAccumulator
  }
}
