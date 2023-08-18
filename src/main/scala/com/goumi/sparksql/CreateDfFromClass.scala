package com.goumi.sparksql

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.storage.StorageLevel

import scala.beans.BeanProperty

object CreateDfFromClass {
  class User(/*@BeanProperty*/ val id:Long,
             /*@BeanProperty*/ val name:String,
             /*@BeanProperty*/ val age:Int,
             /*@BeanProperty*/ val fv:Double) extends Serializable

  def main(args: Array[String]): Unit = {
    //创建SparkSQL的运行环境
    val spark: SparkSession = SparkSession.builder()
      .appName("testSparkSql")
      .master("local[*]")
      .getOrCreate()

    val sc: SparkContext = spark.sparkContext
    val rdd1: RDD[String] = sc.textFile("in/person.txt")
    val rdd2: RDD[User] = rdd1.map(e => {
      val feilds: Array[String] = e.split(",")
      new User(feilds(0).toLong, feilds(1), feilds(2).toInt, feilds(3).toDouble)
    })

    rdd2.persist(StorageLevel.DISK_ONLY)

    val df: DataFrame = spark.createDataFrame(rdd2, classOf[User])
    df.createTempView("v_user")
    spark.sql("select * from v_user").show()
  }
}
