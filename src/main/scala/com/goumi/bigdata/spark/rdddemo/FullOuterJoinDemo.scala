package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object FullOuterJoinDemo {
  def main(args: Array[String]): Unit = {
    val config1: SparkConf = new SparkConf().setMaster("local[*]").setAppName("DistinctDemo")
    val sc = new SparkContext(config1)

    val rdd1: RDD[(String, Int)] = sc.parallelize(List(("tom", 1), ("kitty", 2), ("huni", 3)))
    val rdd2: RDD[(String, Int)] = sc.parallelize(List(("jerry", 1), ("kitty", 2), ("shuke", 3)))

    rdd1.fullOuterJoin(rdd2).foreach(println)

    rdd1.cogroup(rdd2).foreach(println)
  }

}
