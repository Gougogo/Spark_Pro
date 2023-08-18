package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object FlatMapDemo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("loca[*]").setAppName("FlatMapDemo")
    val sc = new SparkContext(conf)

    val strs = List("asd asd", "asdw aweqw")
    val rdd1: RDD[String] = sc.parallelize(strs)
    rdd1.flatMap(_.split(" "))
    rdd1.flatMap(x => x.split(" "))

  }

}
