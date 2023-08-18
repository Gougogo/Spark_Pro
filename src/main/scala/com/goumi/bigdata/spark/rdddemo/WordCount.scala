package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.{Partitioner, SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object WordCount {

  class MyPartitioner(i: Int) extends Partitioner {
    override def numPartitions: Int = {
      i
    }

    override def getPartition(key: Any): Int = {
      1
    }
  }


  def main(args: Array[String]): Unit = {
    val config: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCount")
    val sc = new SparkContext(config)

    val lines: RDD[String] = sc.textFile("in/test.txt")
    lines.map(_.charAt(2))

    val words = lines.flatMap(_.split(" "))


    val a = sc.parallelize(List("dog", "tiger", "lion", "cat", "spider", "eagle"), 2)
    val b: RDD[(Int, String)] = a.keyBy(_.length) //将字符串的长度作为key值。
    b.groupByKey.collect //根据相同key值来进行group操作
    val wordToOne: RDD[(String, Iterable[String])] = words.groupBy(x => x)

    val value: RDD[(String, Iterable[Iterable[String]])] = wordToOne.groupByKey()


    val result = value.collect()

    result.foreach(print)


    val listRDD = sc.makeRDD(List(("a", 1), ("b", 2), ("c", 3)))

    val partRDD = listRDD.partitionBy(new MyPartitioner(3))

    partRDD.saveAsTextFile("output")


    //    val tupleRDD = listRDD.mapPartitionsWithIndex {
    //      case (num, datas) => {
    //        val ints = datas.map(data => data * 2)
    //
    //        val tuples = ints.map((_, "partition: " + num))
    //        ints
    //        tuples
    //      }
    //    }
    //
    //    tupleRDD.collect().foreach(println)


    //    val value = sc.makeRDD(List(1, 5, 5))
    //val lines:RDD[String] = sc.textFile("in")
    //
    //    lines.distinct()
    //
    /*val words: RDD[String] = lines.flatMap(_.split(" "))

    val wordToOne: RDD[(String, Int)] = words.map((_, 1))

    val wordToSum = wordToOne.reduceByKey(_ + _, 12)
//
    val result = wordToSum.collect()*/
    //
    //    result.foreach(print)
    //
    //    val listRDD = sc.makeRDD(List(("a", 1), ("b", 2), ("c", 3)))
    //    val partRDD = listRDD.partitionBy(new HashPartitioner(2))
    //
    //    val cacahe = listRDD.persist(StorageLevel.MEMORY_ONLY)
    //
    //    val value1 = words.map(x => for (i <- 1 to 10000) yield i.toString)
    //
    //    value1.foreach(print)
    //
    //
    //println(result); println(result)
    //
    //    result.foreach(print)
  }

}
