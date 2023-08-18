package org.apache.spark

import org.apache.spark.rdd.{MapPartitionsRDD, RDD}
import org.apache.spark.{Partition, SparkConf, SparkContext}

object FlatMapValuesDemo {
  def main(args: Array[String]): Unit = {
    val config: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCount")
    val sc = new SparkContext(config)
    val arr: Array[(String, String)] = Array(("spark", "1,2,3"), ("hadoop", "1,2,3"))
    val rdd1: RDD[(String, String)] = sc.makeRDD(arr, 10)
    val value1: RDD[(String, (String, String))] = rdd1.join(rdd1)
    println(value1.collect().toBuffer)

    val value: RDD[(String, String)] = rdd1.flatMapValues((v:String)=>v.split(","))
    val tuples: Array[(String, String)] = rdd1.collect()
    //println(value.collect().toBuffer)
    //println(rdd1.collect().mkString("Array(", ", ", ")"))


    val rdd2: RDD[(String, String)] = rdd1.flatMap(
      t => {
        val key: String = t._1
        val nums: Array[String] = t._2.split(",")
        val arr: Array[(String, String)] = nums.map(e => (
          (key, e)
          ))
        arr
      })

    rdd1.flatMapValues(_.split(","))

    //println(rdd2.collect().toBuffer)
  }
}
