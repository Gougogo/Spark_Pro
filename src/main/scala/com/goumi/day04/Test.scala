package com.goumi.day04

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object Test {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkSQL01_Demo")

    val session:SparkSession =  SparkSession
      .builder()
      .appName("Spark SQL basic example")
      .config(sparkConf)
      .getOrCreate()

    val frame = session.read.json("in/user.json")

    frame.show()

    session.stop()
  }

}
