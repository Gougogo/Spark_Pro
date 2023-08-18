package com.goumi.day08

import com.goumi.utils.SparkUtils
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.util.LongAccumulator

object AccumulatorDemo1 {
  def main(args: Array[String]): Unit = {
    val sc: SparkContext = SparkUtils.createContext()
    val arr: Array[Int] = Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val rdd1: RDD[Int] = sc.parallelize(arr,2)

    val accumulator: LongAccumulator = sc.longAccumulator("even")
    val rdd4: RDD[Int] = rdd1.map(e => {
      if (e % 2 == 0)
        accumulator.add(1)

      println(accumulator.value)
      e * 10
    })
    //rdd4.saveAsTextFile("out/out10")
    println("触发action之前：" + accumulator.value)
    println(rdd4.collect().toBuffer)
    println("触发action之后：" + accumulator.value)
    //输出之所以是10，是因为重新执行action算子之后，它会重新从源头读数据计算，相当于accumulator.add(1)执行了2次
    //但累加器初始化好之后一直在起作用
    //首先这个程序会被分为2个Task，一个Task中奇数是2，另一个Task中奇数是3
    //这种东西，你得自己有那个能力通过源码调试分析出来
    Thread.sleep(10000000)
  }
}
