package com.goumi.bigdata.spark.rdddemo

import com.goumi.utils.SparkUtils
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object ZipWithIndexDemo {
  def main(args: Array[String]): Unit = {
    val sc: SparkContext = SparkUtils.createContext()
    val lines: RDD[String] = sc.textFile("in/zipTest.txt")
    //println(lines.zipWithIndex().collect().toBuffer)
    lines.zipWithIndex().saveAsTextFile("out/out101")


  }

}
