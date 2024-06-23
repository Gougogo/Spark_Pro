package com.goumi.sparksql

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

/**
 * @auther GouMi
 * @version 1.0
 */
object TestDriverExecutor {
  def main(args: Array[String]): Unit = {
    val config: SparkConf = new SparkConf().setMaster("spark://192.168.146.141:7077").setAppName("TestDriverExecutor")
    val sc = new SparkContext(config)
    val listRdd: RDD[Int] = sc.makeRDD(1 to 4)
    val i = 10

    val mapRDD = listRdd.map(_*i)
    mapRDD.collect().foreach(println)
  }

}
