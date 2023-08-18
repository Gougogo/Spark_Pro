package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object ReduceDemo {
  def func1(a: Int, b: Int): Int = {
    println("test")
    a + b
  }

  def main(args: Array[String]): Unit = {
    val config: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCount")
    val sc = new SparkContext(config)

    val rdd1: RDD[Int] = sc.parallelize(List(1, 2, 3, 4), 2)

    println(rdd1.reduce(func1))

    println(rdd1.fold(0)(func1))

    println(rdd1.min())
    println(rdd1.max())


    Thread.sleep(100000000)

  }

}
