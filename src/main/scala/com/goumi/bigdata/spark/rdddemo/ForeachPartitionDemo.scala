package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object ForeachPartitionDemo {
  def main(args: Array[String]): Unit = {
    val config: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCount")
    val sc = new SparkContext(config)

    val lst = List("辽宁省,沈阳市,200", "辽宁省,本溪市,300")
    val rdd1: RDD[String] = sc.parallelize(lst)
    //rdd1.foreachPartition()
  }
}
