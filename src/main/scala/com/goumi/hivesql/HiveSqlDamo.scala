package com.goumi.hivesql

import org.apache.spark.sql.SparkSession

object HiveSqlDamo {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().appName(this.getClass.getSimpleName)
      .master("local[*]")
      .enableHiveSupport()
      .getOrCreate()

    spark.sql("show databases").show()
  }

}
