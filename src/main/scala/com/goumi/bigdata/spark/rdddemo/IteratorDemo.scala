package com.goumi.bigdata.spark.rdddemo

import com.goumi.utils.SparkUtils
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object IteratorDemo {
  def main(args: Array[String]): Unit = {
    val sc: SparkContext = SparkUtils.createContext()
    val lines: RDD[String] = sc.textFile("in/shop.txt", 2)
    val dataWithIndex = lines.mapPartitions(datas => {
      val index: Iterator[(String, Int)] = datas.zipWithIndex
      //index.length
      datas
    })
    println(dataWithIndex.collect().toBuffer)

  }

}
