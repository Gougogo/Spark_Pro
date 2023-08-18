package com.goumi.sparksql

import com.goumi.utils.SparkSqlUtils
import org.apache.spark.sql.catalyst.encoders.{ExpressionEncoder, RowEncoder}
import org.apache.spark.sql.types.{DoubleType, IntegerType, StringType, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, Encoder, Encoders, Row, SparkSession}

import java.lang

object EncoderTest3 {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSqlUtils.createSparkSession()
    val lines: Dataset[String] = spark.read.textFile("in/user.txt")
    /*val schema: StructType = new StructType()
      .add("name", StringType)
      .add("age", IntegerType)
      .add("fv", DoubleType)
    val encoder: ExpressionEncoder[Row] = RowEncoder(schema)*/
    //当然也可以不传，直接定义为隐式参数
    //implicit val encoder: ExpressionEncoder[(String, Int, Double)] = RowEncoder(schema)
    //import spark.implicits._

    val value: Encoder[(String, Int, Double)] =
      Encoders.tuple(Encoders.STRING, Encoders.scalaInt, Encoders.scalaDouble)

    val rowRDDDF = lines.map(e => {
      val fields: Array[String] = e.split(",")
      val f1: String = fields(0)
      val f2: Int = fields(1).toInt
      val f3: Double = fields(2).toDouble
      (f1, f2, f3)
    })(value)

    rowRDDDF.printSchema()
  }

}
