package com.goumi.sparksql

import org.apache.spark.sql.{DataFrame, SparkSession}

import java.util.Properties

object CreateDfFromJdbc {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder()
      .appName(this.getClass.getSimpleName)
      .master("local[*]")
      .getOrCreate()

    val properties: Properties = new Properties()
    properties.setProperty("user", "root")
    properties.setProperty("password", "studymysql")
    //properties.getProperty("src/mysql.properties")

    //这里的schema信息就是从数据库中来
    val df: DataFrame = spark.read.jdbc("jdbc:mysql://localhost:3306/bigdata?characterEncoding=utf8"
      , "tb_daily_category_income", properties)

    df.show()

    Thread.sleep(1000000)
  }

}
