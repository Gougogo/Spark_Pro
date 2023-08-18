package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object MinDemo {
  def main(args: Array[String]): Unit = {
    val config1: SparkConf = new SparkConf().setMaster("local[*]").setAppName("DistinctDemo")
    val sc = new SparkContext(config1)

    val rdd1: RDD[Int] = sc.parallelize(List(1, 2, 3, 4), 2)

    //val ints: Array[Int] = rdd1.sortBy(x => x, false).take(1)
    //ints.foreach(println)
    println(rdd1.reduce((a, b) => math.max(a, b)))
    rdd1.min()

    Thread.sleep(1000000000)
  }

}
