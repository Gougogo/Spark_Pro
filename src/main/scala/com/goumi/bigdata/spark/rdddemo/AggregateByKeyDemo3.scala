package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object AggregateByKeyDemo3 {
  def main(args: Array[String]): Unit = {
    val config1: SparkConf = new SparkConf().setMaster("local[*]").setAppName("DistinctDemo")
    val sc = new SparkContext(config1)

    val tuples = List(("cat", 2), ("mouse", 4), ("cat", 5),
      ("dog", 12), ("dog", 15), ("mouse", 7))
    val rdd1: RDD[(String, Int)] = sc.parallelize(tuples, 2)

    val rdd2: RDD[(String, String)] = rdd1.aggregateByKey("$")((u, v: Int) => {
      v.toString.concat(u)

      /*
      v = 2
      u = $
      res = 2$
      u = 2$
      v = 5
      res = 52$
       */
      //u.concat(v.toString)
    },
      (u1, u2: String) => {
        u1.concat(u2)
      }
    )

    println(rdd2.collect().toBuffer)

    Thread.sleep(10000000)
  }

}
