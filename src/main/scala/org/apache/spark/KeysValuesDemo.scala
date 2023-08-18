package org.apache.spark

import org.apache.spark.rdd.{MapPartitionsRDD, RDD}

object KeysValuesDemo {
  def main(args: Array[String]): Unit = {
    val config1: SparkConf = new SparkConf().setMaster("local[*]").setAppName("DistinctDemo")
    val sc = new SparkContext(config1)

    val arr: Array[(String, Int)] = Array(("spark", 1), ("hadoop", 2), ("spark", 1), ("hadoop", 2))

    val rdd1: RDD[(String, Int)] = sc.makeRDD(arr, 2)

    val func2: ((String, Int)) => Int = (e: (String, Int)) => e._2

    new MapPartitionsRDD[Int, (String, Int)](rdd1, (context, pid, iter) => iter.map(func2)).collect().toBuffer


    val keys: RDD[String] = rdd1.keys
    //println(keys.collect().toBuffer)
  }
}
