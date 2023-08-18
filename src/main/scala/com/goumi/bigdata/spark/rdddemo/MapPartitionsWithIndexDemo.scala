package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object MapPartitionsWithIndexDemo {
  def main(args: Array[String]): Unit = {
    val config1: SparkConf = new SparkConf().setMaster("local[*]").setAppName("DistinctDemo")
    val sc = new SparkContext(config1)

    val listRDD: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 1, 2, 3, 4), 2)
    val rdd2: RDD[String] = listRDD.mapPartitionsWithIndex((index, iter) => {
      iter.map(e => s"partition:$index, value:$e")
    })
    val res: Array[String] = rdd2.collect()
    println(res.toBuffer)

    Thread.sleep(1000000000);
  }
}
