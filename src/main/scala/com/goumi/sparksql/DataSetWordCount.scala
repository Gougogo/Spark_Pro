package com.goumi.sparksql

import com.goumi.utils.SparkSqlUtils
import org.apache.spark.sql.{Dataset, SparkSession}

object DataSetWordCount {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSqlUtils.createSparkSession()

    //import spark.implicits._
    val lines: Dataset[String] = spark.read.textFile("in/words.txt")
    //lines.flatMap(e=>e.split(" ")).show()
  }

}
