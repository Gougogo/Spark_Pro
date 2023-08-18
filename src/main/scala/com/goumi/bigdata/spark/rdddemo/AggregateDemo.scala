package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object AggregateDemo {
  def main(args: Array[String]): Unit = {
    val config: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCount")
    val sc = new SparkContext(config)

    val lst = List(1, 2, 3, 4, 5, 6)
    val rdd1: RDD[Int] = sc.parallelize(lst, 3)
    //println(rdd1.partitions.length)
    val func1 = (a: Int, b: Int) => {
      //println("aaaaaa")
      a + b
    }

    val func2 = (a: Int, b: Int) => {
      //println("bbbbbbb")
      a + b
    }

    //println(rdd1.aggregate(0)(func1, func2))

    /*1+1+2 4
    1+3+4 8
    1+5+6 12
    1+4+8+12 25
    */

    val strs = List("a", "b", "c")
    val rdd2: RDD[String] = sc.parallelize(strs, 3)
    val str: String = rdd2.aggregate("$")(_ + _, _ + _)

    val list = List(1, 2, 3, 4, 5, 6, 7, 8, 9)
    val (mul, sum, count) = sc.parallelize(list, 2).aggregate((1, 0, 0))(
      (acc, number) => (acc._1 * number, acc._2 + number, acc._3 + 1),
      (x, y) => (x._1 * y._1, x._2 + y._2, x._3 + y._3)
    )
    (sum / count, mul)

    Thread.sleep(1000000)


  }
}
