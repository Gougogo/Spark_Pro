package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object SortByKeyDemo {
  def main(args: Array[String]): Unit = {
    val config1: SparkConf = new SparkConf().setMaster("local[*]").setAppName("DistinctDemo")
    val sc = new SparkContext(config1)

    val listRDD: RDD[Int] = sc.makeRDD(List(1, 9, 8, 6, 3, 5, 2, 7), 4)
    listRDD.foreach(println)
    val strings: Array[String] = listRDD.mapPartitionsWithIndex((index, iter) => {
      iter.map(e => s"\npar:$index, value:$e\n")
    }
    ).collect()
    println(strings.toBuffer)

    val value: RDD[Int] = listRDD.sortBy(x => x)
    val strings1: Array[String] = value.mapPartitionsWithIndex((index, iter) => {
      iter.map(e => s"\npar:$index, value:$e\n")
    }
    ).collect()

    println(strings1.toBuffer)

    val rdd4: RDD[Int] = listRDD.keyBy(x => x)
      .sortByKey(false, 4)
      .values

    val strings2: Array[String] = rdd4.mapPartitionsWithIndex((index, iter) => {
      iter.map(e => s"\npar:$index, value:$e\n")
    }
    ).collect()

    println(strings2.toBuffer)


    Thread.sleep(1000000)
  }
}
