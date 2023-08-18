package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object MapPartitionsDemo {

  def main(args: Array[String]): Unit = {
    val config: SparkConf = new SparkConf().setMaster("local[*]").setAppName("DistinctDemo")
    val sc = new SparkContext(config)

    val listRDD: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 1, 2, 3, 4), 2)
    //listRDD.flatMap()

    val rdd2: RDD[Int] = listRDD.mapPartitions((datas: Iterator[Int]) => {
      val list: List[Int] = datas.toList
      var lastIndex = 0
      for (elem <- list) {
        lastIndex+=1
      }
      println("lastIndex="+ lastIndex)
      //datas.filter(e=>e % 2 == 0)
      list.toIterator
    })
    val ints: Array[Int] = rdd2.collect()
    println(ints.toBuffer)
  }
}
