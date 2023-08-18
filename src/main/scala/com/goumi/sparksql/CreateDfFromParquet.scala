package com.goumi.sparksql

import org.apache.spark.sql.{DataFrame, SparkSession}

object CreateDfFromParquet {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder()
      .appName(this.getClass.getSimpleName)
      .master("local[*]")
      .getOrCreate()

    //调read还没有读数据，只是为了获取schema信息构建DF
    val df: DataFrame = spark.read.parquet("out/outparquet")

    //show才是读数据
    df.show()
    df.printSchema()

    Thread.sleep(10000000)
  }
}
