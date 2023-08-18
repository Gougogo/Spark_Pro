package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object CoGroupJoinDemo2 {
  def main(args: Array[String]): Unit = {
    val config: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCount")
    val sc = new SparkContext(config)

    val lst = List(("a", 1), ("b", 2), ("a", 1), ("b", 2))
    val rdd1: RDD[(String, Int)] = sc.parallelize(lst)
    val rdd2: RDD[(String, Int)] = sc.parallelize(lst)

    val value: RDD[(String, (Int, Int))] = rdd1.join(rdd2)
    //value.foreach(println)

    val listTest: ListBuffer[(Int, Int)] = ListBuffer()
    val rdd3: RDD[(String, (Iterable[Int], Iterable[Int]))] = rdd1.cogroup(rdd2)
    rdd3.map {
      case (key, (buffer1, buffer2)) => {
        for (value1 <- buffer1) {
          for (value2 <- buffer2) {
            listTest.append((value1, value2))
          }
        }
      }
        listTest.map {
          values => (key, values)
        }
    }.foreach(println)

    val tuples1 = Set(1, 2)
    val tuples2 = Set("a", "b")
    val list: mutable.Set[(Int, String)] = mutable.Set()
    for (value1 <- tuples1) {
      for (value2 <- tuples2) {
        val tuple: (Int, String) = (value1, value2)
        list.add(tuple)
      }
    }

    //list.foreach(println)


  }
}
