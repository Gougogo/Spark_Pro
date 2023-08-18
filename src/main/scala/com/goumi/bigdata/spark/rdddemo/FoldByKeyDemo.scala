package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object FoldByKeyDemo {
  def main(args: Array[String]): Unit = {
    val config1: SparkConf = new SparkConf().setMaster("local[*]").setAppName("DistinctDemo")
    val sc = new SparkContext(config1)

    val tuples = List(("cat", 2), ("cat", 5), ("mouse", 4), ("mouse", 3),
      ("cat", 12), ("dog", 12), ("dog", 15), ("mouse", 7), ("mouse", 2))
    val rdd1: RDD[(String, Int)] = sc.parallelize(tuples, 2)

    println(rdd1.foldByKey(0)(_ + _).collect().toBuffer)


    Thread.sleep(10000000)
  }

}
