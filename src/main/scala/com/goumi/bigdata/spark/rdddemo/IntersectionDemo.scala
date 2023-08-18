package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object IntersectionDemo {
  def main(args: Array[String]): Unit = {
    val config1: SparkConf = new SparkConf().setMaster("local[*]").setAppName("DistinctDemo")
    val sc = new SparkContext(config1)

    /*val rdd1: RDD[(String, Int)] = sc.parallelize(List(("tom", 1), ("kitty", 2), ("huni", 3)))
    val rdd2: RDD[(String, Int)] = sc.parallelize(List(("jerry", 1), ("kitty", 2), ("shuke", 3)))

    val rdd23: RDD[Int] = sc.parallelize(List(1, 2, 3))
    val rdd24: RDD[Int] = sc.parallelize(List(4, 5, 6))
    rdd23.intersection(rdd24)*/

    //rdd1.cogroup(rdd2).foreach(println)

    /*def intersection(other: RDD[T]): RDD[T] = withScope {
      this.map(v => (v, null)).cogroup(other.map(v => (v, null)))
        .filter { case (_, (leftGroup, rightGroup)) => leftGroup.nonEmpty && rightGroup.nonEmpty }
        .keys
    }*/

    //rdd1.intersection(rdd2).foreach(println)

    //val value1: RDD[((String, Int), Null)] = rdd1.map(v => (v, null))
    //value1.foreach(println)
    //rdd1.map(v => (v, null)).cogroup(rdd2.map(v => (v, null))).foreach(println)

    val rdd3: RDD[(Int)] = sc.parallelize(List(1, 2, 2, 3, 4), 3)
    val rdd4: RDD[(Int)] = sc.parallelize(List(5, 2, 7, 8), 3)

    val rdd5: RDD[(Int, Null)] = rdd3.map((_, null))
    val rdd6: RDD[(Int, Null)] = rdd4.map((_, null))
    val value1: RDD[(Int, (Null, Null))] = rdd5.join(rdd6)
    val value2: RDD[Int] = value1.mapPartitions(it => it.map(_._1), true)
    value2.distinct().collect()
    println(value2.partitions.length)
    println(value2.partitioner)
    /*val keys: RDD[Int] = value1.keys
    println(keys.partitioner)
    keys.distinct().collect().toBuffer*/

    //println(keys.distinct().collect().toBuffer)
    Thread.sleep(10000000)

  }
}
