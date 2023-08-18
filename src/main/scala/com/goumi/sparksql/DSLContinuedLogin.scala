package com.goumi.sparksql

import com.goumi.utils.SparkSqlUtils
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object DSLContinuedLogin {
  def main(args: Array[String]): Unit = {
    val sparkSession: SparkSession = SparkSqlUtils.createSparkSession()
    val df: DataFrame = sparkSession.read
      .option("header", "true")
      .csv("in/dataforsql.txt")

    import sparkSession.implicits._
    import org.apache.spark.sql.functions._


    val res: DataFrame = df.select(
      $"uid",
      $"dt",
      (row_number() over (Window.partitionBy("uid").orderBy("dt"))).as("rn"))
      .distinct()
      .select($"uid", $"dt",
        expr("date_sub(dt, rn) as datesub"))
      .groupBy("uid", "datesub")
      .agg(
        count("*") as "times",
        min("dt") as "start_date",
        max("dt") as "end_date"
      )
    res.show()
  }
}
