package com.goumi.sparksql

import org.apache.spark.sql.SparkSession

/**
 * @auther GouMi
 * @version 1.0
 */
object TestHiveSql {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder()
      .master("spark://192.168.146.141:7077")
      .appName(this.getClass.getSimpleName)
      .getOrCreate()

    spark.sql("select * from default.student_table").show()
  }

}
