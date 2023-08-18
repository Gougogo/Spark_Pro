package com.goumi.sparksql

import com.goumi.utils.SparkSqlUtils
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object SQLShopIncomeDemo1 {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSqlUtils.createSparkSession()
    val df: DataFrame = spark.read
      .option("header", "true")
      .csv("in/shop2.csv")
    import spark.implicits._
    val rdd1: Dataset[RowBean] = df.map(row => {
      /*val sid1: String = row.getString(0)
      val dt1: String = row.getString(1)
      val money1: String = row.getString(2)*/

      val sid: String = row.getAs[String]("sid")
      val dt: String = row.getAs[String]("dt")
      val month: String = dt.substring(0, 7)
      val money: Double = row.getAs[String]("money").toDouble

      new RowBean(sid, month, money)
    })
    rdd1.printSchema()

    rdd1.show()
  }

}
