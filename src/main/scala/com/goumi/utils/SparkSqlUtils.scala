package com.goumi.utils

import org.apache.spark.sql.SparkSession

object SparkSqlUtils {
  def createSparkSession(): SparkSession ={
    SparkSession.builder()
      .appName(this.getClass.getSimpleName)
      .master("local[*]")
      .getOrCreate()
  }
}
