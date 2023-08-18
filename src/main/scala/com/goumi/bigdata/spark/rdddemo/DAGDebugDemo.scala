package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object DAGDebugDemo {
  def main(args: Array[String]): Unit = {
    val config21: SparkConf = new SparkConf().setMaster("local[*]").setAppName("DAGDebug")
    val sc = new SparkContext(config21)

    val tuples1 = List(("cat", 2), ("cat", 2), ("ca", 2), ("ca", 2), ("cat", 2))
    val tuples2 = List(("cat", 2), ("cat", 2), ("ca", 2), ("ca", 2), ("cat", 2))

    val strings = List("cat", "head", "hook")


    val rdd1: RDD[(String, Int)] = sc.parallelize(tuples1, 2)

    val rdd2: RDD[(String, Int)] = sc.parallelize(tuples2, 2)
    val rdd3: RDD[String] = sc.parallelize(strings, 2)

    val groupByRDD: RDD[(String, Iterable[Int])] = rdd1.groupByKey()
    val mapRDD: RDD[(String, Int)] = rdd3.map(x => (x, 1))

    val unionRDD: RDD[(String, Int)] = mapRDD.union(rdd2)

    val resRDD: RDD[(String, (Int, Iterable[Int]))] = unionRDD.join(groupByRDD)

    resRDD.collect().foreach(println)





    //Thread.sleep(10000000)
  }
}
