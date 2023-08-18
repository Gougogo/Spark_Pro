package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.util.control.Breaks.break

object GroupByDemo {
  def main(args: Array[String]): Unit = {
    val config: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCount")
    val sc = new SparkContext(config)

    val lst = List("辽宁省,沈阳市,200", "辽宁省,本溪市,300")
    val rdd1: RDD[String] = sc.parallelize(lst)
    val rdd2: RDD[(String, (String, Double))] = rdd1.map(e => {
      val fields: Array[String] = e.split(",")
      (fields(0), (fields(1), fields(2).toDouble))
    }
    )

    rdd1.coalesce(1)
    val rdd3: RDD[(String, Iterable[(String, Double)])] = rdd2.groupByKey()
    val res: Array[(String, Iterable[(String, Double)])] = rdd3.collect()
    //println(res.toBuffer)
    //ArrayBuffer((辽宁省,CompactBuffer((沈阳市,200.0), (本溪市,300.0))))

    val Int: Int = 20

    break()

    sc.stop()
  }
}
