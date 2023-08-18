package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark01_RDD {
  def main(args: Array[String]): Unit = {
    val config: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCount")
    val sc = new SparkContext(config)

    val listRDD: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4))

    listRDD.partitions.length

    val rdd2: RDD[Int] = listRDD.filter(_ % 2 == 0)

    val rdd3: RDD[Int] = rdd2.map(_ * 10)

    rdd3.collect()
  }
}
