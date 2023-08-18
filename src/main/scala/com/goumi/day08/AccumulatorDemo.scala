package com.goumi.day08

import com.goumi.utils.SparkUtils
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.util.LongAccumulator

object AccumulatorDemo {
  def main(args: Array[String]): Unit = {
    val sc: SparkContext = SparkUtils.createContext()
    val arr: Array[Int] = Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val rdd1: RDD[Int] = sc.parallelize(arr)
    rdd1.filter(e=>e%2==0).count()

    //这儿多次用到了rdd1，其实cache一下，但也要注意rdd1的数据量
    val rdd2: RDD[Int] = rdd1.map(e => {
      e * 10
    })

    //println(rdd2.collect().toBuffer)

    //上面的例子中是触发了2次action
    //现在需要触发一次action，并将要统计的次数和最终计算好的结果都算出来

    /*var nums:Int = 0
    val rdd3: RDD[Int] = rdd1.map(e => {
      if (e % 2 == 0)
        nums += 1
      e * 10
    })
    println("触发action之前：" + nums)
    println(rdd3.collect().toBuffer)
    println("触发action之后：" + nums)*/

    val accumulator: LongAccumulator = sc.longAccumulator("even")
    val rdd4: RDD[Int] = rdd1.map(e => {
      if (e % 2 == 0)
        accumulator.add(1)

      e * 10
    })
    println("触发action之前：" + accumulator.value)
    println(rdd4.collect().toBuffer)
    println("触发action之后：" + accumulator.value)
  }
}
