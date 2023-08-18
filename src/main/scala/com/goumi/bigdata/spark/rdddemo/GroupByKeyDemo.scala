package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ArrayBuffer

object GroupByKeyDemo {
  def main(args: Array[String]): Unit = {
    val config: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCount")
    val sc = new SparkContext(config)
    val listRDD: RDD[String] = sc.textFile("in/test2.txt")
    println(listRDD.count())
    println("partitions=" + listRDD.partitions.length)

    val value1: RDD[String] = listRDD.flatMap(_.split(" "))

    val value: RDD[(String, Int)] = listRDD.flatMap(_.split(" ")).map((_, 1))

    val wordAndOne: RDD[(String, Int)] = listRDD.flatMap(_.split(" ")).map((_, 1))
    wordAndOne.groupByKey()

    wordAndOne.combineByKey((_, 1),
      (acc: (Int, Int), v) => (acc._1 + v, acc._2 + 1),
      (acc1: (Int, Int), acc2: (Int, Int)) => (acc1._1 + acc2._1, acc1._2 + acc2._2))

    //val grouped: RDD[(String, Iterable[Int])] = new PairRDDFunctions[String, Int](wordAndOne).groupByKey()

    val f1 = (v: Int) => ArrayBuffer[Int](v)

    val f2 = (acc: ArrayBuffer[Int], in: Int) => acc += in

    val f3 = (acc1: ArrayBuffer[Int], acc2: ArrayBuffer[Int]) => acc1 ++= acc2

    //grouped.saveAsTextFile("out-group1")
    sc.stop()
  }

}
