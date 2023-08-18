package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object LeftOuterJoinDemo {
  def main(args: Array[String]): Unit = {
    val config1: SparkConf = new SparkConf().setMaster("local[*]").setAppName("DistinctDemo")
    val sc = new SparkContext(config1)

    val rdd1: RDD[(String, Int)] = sc.parallelize(List(("tom", 1), ("kitty", 2), ("huni", 3)))
    val rdd2: RDD[(String, Int)] = sc.parallelize(List(("jerry", 1), ("kitty", 2), ("shuke", 3)))

    rdd1.leftOuterJoin(rdd2).foreach(println)

    val rdd3: RDD[(String, (Iterable[Int], Iterable[Int]))] = rdd1.cogroup(rdd2)
    rdd3.foreach(println)

    rdd3.flatMapValues(pair => {
      if (pair._2.isEmpty) {
        pair._1.iterator.map(e => (e, None))
      } else {
        for (v1 <- pair._1; v2 <- pair._2) yield (Some(v1), Some(v2))
      }
    })

  }

}
