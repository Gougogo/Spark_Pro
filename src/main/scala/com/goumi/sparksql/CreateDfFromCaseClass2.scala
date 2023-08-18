package com.goumi.sparksql

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

object CreateDfFromCaseClass2 {
  case class User(id:Long, name:String, age:Int, fv:Double)

  def main(args: Array[String]): Unit = {
    //创建SparkSQL的运行环境
    val spark: SparkSession = SparkSession.builder()
      .appName("testSparkSql")
      .master("local[*]")
      .getOrCreate()

    val sc: SparkContext = spark.sparkContext
    val rdd1: RDD[String] = sc.textFile("in/person.txt")
    val rdd2: RDD[User] = rdd1.map(e => {
      val feilds: Array[String] = e.split(",")
      User(feilds(0).toLong, feilds(1), feilds(2).toInt, feilds(3).toDouble)
    })

    val df: DataFrame = spark.createDataFrame(rdd2)
    df.createTempView("v_user")
    spark.sql("select * from v_user").show()
  }
}
