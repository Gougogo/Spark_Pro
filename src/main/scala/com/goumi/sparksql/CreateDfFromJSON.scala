package com.goumi.sparksql

import org.apache.spark.sql.{DataFrame, SparkSession}

object CreateDfFromJSON {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder()
      .appName(this.getClass.getSimpleName)
      .master("local[*]")
      .getOrCreate()

    //json文件中自带schema信息
    //读json是通过read来读的
    val df: DataFrame = spark.read.json("in/user.json")
    df.createTempView("v_user")

    spark.udf.register("addName", (x:String) => "Name=" + x)


    spark.sql("select addName(name) from v_user").show()

    //Thread.sleep(1000000)
  }

}
