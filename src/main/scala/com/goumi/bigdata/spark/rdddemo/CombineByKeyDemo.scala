package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object CombineByKeyDemo {
  def main(args: Array[String]): Unit = {

    val config: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCount")
    val sc = new SparkContext(config)

    val tuples = List(("a", 1), ("b", 2), ("c", 3), ("a", 1), ("b", 2), ("c", 3))

    val rdd1: RDD[(String, Int)] = sc.parallelize(tuples, 2)
    rdd1.combineByKey(x => x, (acc: Int, v) => math.max(acc, v), (acc: Int, v: Int) => math.max(acc, v))

  }

}
