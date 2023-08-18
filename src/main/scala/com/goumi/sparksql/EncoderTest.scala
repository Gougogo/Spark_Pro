package com.goumi.sparksql

import com.goumi.utils.SparkSqlUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{DoubleType, IntegerType, StringType, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

object EncoderTest {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSqlUtils.createSparkSession()
    val lines: RDD[String] = spark.sparkContext.textFile("in/userdata")
    val rowRDD: RDD[Row] = lines.map(e => {
      val fields: Array[String] = e.split(",")
      val f1: String = fields(0)
      val f2: Int = fields(1).toInt
      val f3: Double = fields(2).toDouble
      Row(f1, f2, f3)
    })

    val schema: StructType = new StructType()
      .add("name", StringType)
      .add("age", IntegerType)
      .add("fv", DoubleType)

    val frame: DataFrame = spark.createDataFrame(rowRDD, schema)
    frame.show()
  }

}
