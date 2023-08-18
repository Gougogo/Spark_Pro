package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object SubtractDemo {
  def main(args: Array[String]): Unit = {
    val config1: SparkConf = new SparkConf().setMaster("local[*]").setAppName("DistinctDemo")
    val sc = new SparkContext(config1)

    val rdd1: RDD[(Int)] = sc.parallelize(List(1, 2, 2, 3, 4), 3)
    val rdd2: RDD[(Int)] = sc.parallelize(List(5, 2, 7, 8), 3)
    //rdd1.map()

    rdd1.subtract(rdd2).foreach(println)

    Thread.sleep(10000000)

  }
}
