package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object CoGroupDemo {
  def main(args: Array[String]): Unit = {
    val config: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCount")
    val sc = new SparkContext(config)

    val lst = List(("a", 1), ("b", 2), ("a", 1), ("b", 2))
    val rdd1: RDD[(String, Int)] = sc.parallelize(lst)
    val rdd2: RDD[(String, Int)] = sc.parallelize(lst)
    rdd1.count()

    val res: RDD[(String, (Iterable[Int], Iterable[Int]))] = rdd1.cogroup(rdd2)

    val value: RDD[(String, Int)] = res.mapValues(t => (t._1.sum + t._2.sum))


    value.foreach(println)


  }
}
