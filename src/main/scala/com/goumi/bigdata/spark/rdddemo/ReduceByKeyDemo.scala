package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkConf, SparkContext}

object ReduceByKeyDemo {
  def main(args: Array[String]): Unit = {
    val config: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCount")
    val sc = new SparkContext(config)
    val listRDD: RDD[String] = sc.textFile("in/test.txt")
    val wordAndOne: RDD[(String, Int)] = listRDD.flatMap(_.split(" ")).map((_, 1))
    //println(wordAndOne.partitioner)
    wordAndOne.sortByKey()
    val value: RDD[(String, Int)] = wordAndOne.reduceByKey(_ + _)
    val partitioner: Option[Partitioner] = value.partitioner

    //wordAndOne.groupByKey()

    /*val ints = List(1, 1, 1, 1)
    val ints1: List[Int] = ints.map(x => x * 10000)

    val f1 = (x : Int) => x

    val f2 = (x: Int, y: Int) => x + y

    val f3 = (a: Int, b: Int) => a + b

    //val reduced: RDD[(String, Int)] = wordAndOne.combineByKey(f1, f2, f3)

    val shuffledRDD = new ShuffledRDD[String, Int, Int](wordAndOne, new HashPartitioner(wordAndOne.partitions.length))

    shuffledRDD.setMapSideCombine(true)
    val aggre = new Aggregator[String, Int, Int](
      f1, f2, f3
    )
    shuffledRDD.setAggregator(aggre)

    val res: Array[(String, Int)] = shuffledRDD.collect()
    println(res.toBuffer)
    sc.stop()*/
  }
}
