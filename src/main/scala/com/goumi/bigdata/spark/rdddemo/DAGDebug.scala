package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object DAGDebug {
  def main(args: Array[String]): Unit = {
    val config21: SparkConf = new SparkConf().setMaster("local[*]").setAppName("DAGDebug")
    val sc = new SparkContext(config21)

    val tuples = List(("cat", 2), ("cat", 2), ("ca", 2), ("ca", 2), ("cat", 2))

    val pairRDD: RDD[(String, Int)] = sc.parallelize(tuples, 2)
    val rdd1: RDD[(String, Int)] = pairRDD.aggregateByKey(0)((A: Int, B: Int) => {
      math.max(A, B)
    }, (A: Int, B: Int) => {
      A + B
    })
    rdd1.collect().foreach(println)

    //Thread.sleep(10000000)
  }
}
