package com.goumi.sparksql

import com.goumi.utils.SparkSqlUtils
import org.apache.spark.sql.{Dataset, Encoder, Encoders, SparkSession}
import org.apache.spark.storage.StorageLevel

import scala.beans.BeanProperty

object EncoderTest4 {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSqlUtils.createSparkSession()
    val lines: Dataset[String] = spark.read.textFile("in/user.txt")


    implicit val value: Encoder[Teacher] = Encoders.bean(classOf[Teacher])

    val rowRDDDF: Dataset[Teacher] = lines.map(e => {
      val fields: Array[String] = e.split(",")
      val f1: String = fields(0)
      val f2: Int = fields(1).toInt
      val f3: Double = fields(2).toDouble
      new Teacher(f1, f2, f3)
    })

    rowRDDDF.persist(StorageLevel.DISK_ONLY)

    rowRDDDF.printSchema()
    rowRDDDF.show()
  }
}

class Teacher(
             @BeanProperty
             var name:String,
             @BeanProperty
             var age:Int,
             @BeanProperty
             var fv:Double
             ){

  override def toString = s"Teacher($name, $age, $fv)"
}
