package com.goumi.sparksql

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SparkSession}

object WriteToParquet {
  def main(args: Array[String]): Unit = {
    //创建SparkSQL的运行环境
    val spark: SparkSession = SparkSession.builder()
      .appName("testSparkSql")
      .master("local[*]")
      .getOrCreate()

    val sc: SparkContext = spark.sparkContext
    val rdd1: RDD[String] = sc.textFile("in/person1.txt")
    val rdd2: RDD[Row] = rdd1.map(e => {
      val feilds: Array[String] = e.split(",")
      Row(feilds(0).toLong, feilds(1), feilds(2).toInt, feilds(3).toDouble)
    })

    val schema: StructType = StructType(
      Array(
        StructField("id", LongType),
        StructField("name", StringType),
        StructField("age", IntegerType),
        StructField("fv", DoubleType)
      )
    )

    spark.createDataFrame(rdd2, schema).write.parquet("out/outparquet")

    Thread.sleep(210000000)

  }
}
