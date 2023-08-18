package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkConf, SparkContext}

object JoinDemo {

  class MyPartitioner(i: Int) extends Partitioner {
    override def numPartitions: Int = {
      i
    }

    override def getPartition(key: Any): Int = {
      1
    }


  }


  def main(args: Array[String]): Unit = {
    val config1: SparkConf = new SparkConf().setMaster("local[*]").setAppName("DistinctDemo")
    val sc = new SparkContext(config1)

    val arr: Array[(String, Int)] = Array(("hadoop", 2))

    val rdd1: RDD[(String, Int)] = sc.makeRDD(arr, 2)

    val arr2: Array[(String, Int)] = Array(("hive", 1), ("hadoop", 2))

    val rdd2: RDD[(String, Int)] = sc.makeRDD(arr2, 2)


    val value: RDD[(String, (Iterable[Int], Iterable[Int]))] = rdd1.cogroup(rdd2)
    println(value.collect().toBuffer)

    println(rdd1.join(rdd1, new MyPartitioner(20)).collect().toBuffer)
  }
}
