package com.goumi.sparksql

import com.goumi.utils.{SparkSqlUtils, SparkUtils}
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.types.DataTypes
import org.apache.spark.sql.{DataFrame, SparkSession}

object DSLShopIncomeDemo {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSqlUtils.createSparkSession()
    val df: DataFrame = spark.read
      .option("header", true)
      .csv("in/shop2.csv")
    import spark.implicits._
    import org.apache.spark.sql.functions._

    df.select($"sid",
      date_format($"dt", "yyyy-MM") as("month"),
      $"money".cast(DataTypes.DoubleType) as "money"
    ).groupBy("sid", "month"
    ).agg(sum("money") as "sum_money"
    ).select(
      'sid,
      'month,
      'sum_money,
      sum("sum_money") over(Window.partitionBy("sid")
        .orderBy("month"))
        //.rowsBetween()
        as("asd")
    ).show()
  }
}
