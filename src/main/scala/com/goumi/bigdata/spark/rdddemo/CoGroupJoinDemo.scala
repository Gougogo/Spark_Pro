package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

object CoGroupJoinDemo {
  def main(args: Array[String]): Unit = {
    val config: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCount")
    val sc = new SparkContext(config)

    val lst1 = List(("a", 1), ("b", 2))
    val lst2 = List(("a", 11), ("b", 22), ("c", 33))
    val rdd1: RDD[(String, Int)] = sc.parallelize(lst1)
    val rdd2: RDD[(String, Int)] = sc.parallelize(lst2)

    val value: RDD[(String, (Int, Int))] = rdd1.join(rdd2)
    //value.foreach(println)
    println("============")
    val listTest: mutable.Set[(Int, Int)] = mutable.Set()

    val rdd3: RDD[(String, (Iterable[Int], Iterable[Int]))] = rdd1.cogroup(rdd2)
    rdd3.foreach(println)


    val rdd4: RDD[Iterable[(String, (Int, Int))]] = rdd3.map {
      case (key, (buffer1, buffer2)) => {
        val tuples: Iterable[(Int, Int)] =
          for (value1 <- buffer1; value2 <- buffer2) yield (value1, value2)
        //println((key, tuples))
        tuples.map {
          values => (key, values)
        }
      }
    }
    //rdd4.foreach(println)

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
