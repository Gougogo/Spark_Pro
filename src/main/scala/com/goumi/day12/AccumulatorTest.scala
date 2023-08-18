package com.goumi.day12

import com.goumi.utils.SparkUtils
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.util.LongAccumulator

object AccumulatorTest {
  def main(args: Array[String]): Unit = {
    val sc: SparkContext = SparkUtils.createContext()
    val arr: Array[Int] = Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val rdd1: RDD[Int] = sc.parallelize(arr,2)

    val accumulator: LongAccumulator = sc.longAccumulator("even")
    val rdd4 = rdd1.map(e => {
      accumulator.add(1)
      println(accumulator.value)
      e
    })

    println(rdd4.collect().toBuffer)
    println("触发action之后：" + accumulator.value)
  }
}
