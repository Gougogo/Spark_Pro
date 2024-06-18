package com.goumi.sparksql

import org.apache.spark.sql.SparkSession

/**
 * @auther GouMi
 * @version 1.0
 */
object TestHiveSql {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder()
      .appName(this.getClass.getSimpleName)
      .enableHiveSupport()
      .getOrCreate()

    spark.sql("select * from default.student_table").show()
  }

}
