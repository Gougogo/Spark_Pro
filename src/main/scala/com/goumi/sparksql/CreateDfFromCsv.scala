package com.goumi.sparksql

import org.apache.spark.sql.{DataFrame, SparkSession}

object CreateDfFromCsv {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder()
      .appName(this.getClass.getSimpleName)
      .master("local[*]")
      .getOrCreate()

    //json文件中自带schema信息
    //读json是通过read来读的
    val df: DataFrame = spark.read
      .option("header", "true")
      .option("inferSchema","true")
      .csv("in/person.csv")
    df.show()


    df.printSchema()
    //可以指定表头，但数据类型还是String

    Thread.sleep(1000000)
  }

}
