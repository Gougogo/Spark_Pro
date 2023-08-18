package org.apache.spark

import org.apache.spark.rdd.{MapPartitionsRDD, RDD}
import org.apache.spark.{Partition, SparkConf, SparkContext}

object Spark01_RDD {
  def main(args: Array[String]): Unit = {
    val config: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCount")
    val sc = new SparkContext(config)
    val listRDD: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4), 10)
    val func = (e:Int)=>e*10
    val filterFunc = (e:Int) => e % 2 == 0
    val testRDD = new MapPartitionsRDD[Int, Int](listRDD, (context, pid, iter) => iter.filter(filterFunc))
    println(testRDD.collect().toBuffer)
  }
}
