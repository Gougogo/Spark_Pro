package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object getPartitionsDemo {
  def main(args: Array[String]): Unit = {
    val config1: SparkConf = new SparkConf().setMaster("local[*]").setAppName("DistinctDemo")
    val sc = new SparkContext(config1)

    val listRDD: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 5), 3)
    val length: Int = listRDD.partitions.length
    println(length)

    /*    (0 until numSlices).iterator.map { i =>
      val start = ((i * length) / numSlices).toInt
      val end = (((i + 1) * length) / numSlices).toInt
      (start, end)
      0 1 2
      (0, 1) 1
      (1, 3) 2
      (3, 5) 2
      */


  }
}
