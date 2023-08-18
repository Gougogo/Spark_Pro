package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object aggregateByKeyDemo {
  def main(args: Array[String]): Unit = {
    val config1: SparkConf = new SparkConf().setMaster("local[*]").setAppName("DistinctDemo")
    val sc = new SparkContext(config1)

    val tuples = List(("cat", 2), ("cat", 2), ("ca", 2), ("ca", 20), ("cat", 200))

    val pairRDD: RDD[(String, Int)] = sc.parallelize(tuples, 2)
    pairRDD.collect().foreach(println)

    //pairRDD.combineByKey((x=>x),(acc:(Int),v)=>math.max(acc, v),(acc:(Int),v:(Int))=>math.max(acc, v)).collect().foreach(println)

    //pairRDD.reduceByKey((a,b )=>math.max(a,b)).collect().foreach(println)

    val rdd2: RDD[(String, Iterable[Int])] = pairRDD.groupByKey()
    rdd2.collect().foreach(println)
    val value: RDD[(String, Iterable[Int])] = rdd2.mapValues(e => e)
    //rdd2.reduceByKey((a, b)=>Iterator.iterate(a=>a))
    //rdd3.aggregateByKey(0)((u, v)=>u, _+_)


    val rdd1: RDD[(String, Int)] = pairRDD.aggregateByKey(0)((A: Int, B: Int) => {
      math.max(A, B)
    }, (A: Int, B: Int) => {
      A + B
    })

    //rdd1.collect.foreach(println)

    Thread.sleep(10000000)
  }

}
