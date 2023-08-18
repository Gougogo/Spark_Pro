package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object TakeDemo {
  def main(args: Array[String]): Unit = {
    val config1: SparkConf = new SparkConf().setMaster("local[*]").setAppName("DistinctDemo")
    val sc = new SparkContext(config1)

    val rdd1: RDD[Int] = sc.parallelize(List(1, 2, 3, 4), 2)

    val ints: Array[Int] = rdd1.take(3)
    ints.foreach(println)

    rdd1.takeOrdered(2)

    Thread.sleep(1000000000)
  }

}
