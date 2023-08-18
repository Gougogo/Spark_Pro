package com.goumi.sparksql

import com.goumi.utils.SparkSqlUtils
import org.apache.spark.sql.{DataFrame, SparkSession}

object SQLShopIncomeDemo {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSqlUtils.createSparkSession()
    val df: DataFrame = spark.read
      .option("header", "true")
      .csv("in/shop2.csv")

    df.createTempView("v_shop")

    spark.sql(
      """
        |select
        | sid,
        | mth,
        | sum(money) as mth_income
        |from(
        |   select
        |    sid,
        |    substr(dt, 0, 7) mth,
        |    cast(money as double) money
        |   from v_shop
        |)t1
        |group by sid, mth
        |""".stripMargin).show()
  }
}
