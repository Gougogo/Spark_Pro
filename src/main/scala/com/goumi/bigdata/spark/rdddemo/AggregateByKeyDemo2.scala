package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object AggregateByKeyDemo2 {
  def main(args: Array[String]): Unit = {
    val config1: SparkConf = new SparkConf().setMaster("local[*]").setAppName("DistinctDemo")
    val sc = new SparkContext(config1)

    val tuples = List(("cat", 2), ("cat", 5), ("mouse", 4), ("mouse", 3),
      ("cat", 12), ("dog", 12), ("dog", 15), ("mouse", 7), ("mouse", 2))
    val rdd1: RDD[(String, Int)] = sc.parallelize(tuples, 2)

    /*def positions(length: Long, numSlices: Int): Iterator[(Int, Int)] = {
      (0 until numSlices).iterator.map { i =>
        val start = ((i * length) / numSlices).toInt
        val end = (((i + 1) * length) / numSlices).toInt
        (start, end)
      }

      start = 0
      end = 4
      4

      start = 4
      end = 9
      5
    }*/


    val rdd11: RDD[String] = rdd1.mapPartitionsWithIndex((index, iter) => {
      iter.map(e => s"\n partition:$index, value:$e")
    })
    val res: Array[String] = rdd11.collect()
    //println(res.toBuffer)


    val rdd2: RDD[(String, Int)] = rdd1.aggregateByKey(0)(math.max, _ + _)

    println(rdd2.collect().toBuffer)

    Thread.sleep(10000000)
  }

}
