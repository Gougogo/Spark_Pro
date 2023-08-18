package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object CombineByKeyDemo2 {
  def main(args: Array[String]): Unit = {

    val config: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordC")
    val sc = new SparkContext(config)

    val tuples = List(("a", 1), ("b", 2), ("c", 3), ("a", 1), ("b", 2), ("c", 3))

    val rdd1: RDD[(String, Int)] = sc.parallelize(tuples, 3)
    val rdd2: RDD[(String, (Int, Int))] = rdd1.combineByKey(
      (_, 1),
      (acc: (Int, Int), v) => (acc._1 + v, acc._2 + 1),
      (acc1: (Int, Int), acc2: (Int, Int)) => (acc1._1 + acc2._1, acc1._2 + acc2._2))
    println(rdd2.collect().toBuffer)

    rdd2.map { case (key, value) => (key, value._1 / value._2.toDouble) }

    Thread.sleep(10000000)
  }

}
