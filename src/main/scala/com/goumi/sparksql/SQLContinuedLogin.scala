package com.goumi.sparksql

import com.goumi.utils.SparkSqlUtils
import org.apache.spark.sql.{DataFrame, SparkSession}

object SQLContinuedLogin {
  def main(args: Array[String]): Unit = {
    val sparkSession: SparkSession = SparkSqlUtils.createSparkSession()
    val df: DataFrame = sparkSession.read
      .option("header", "true")
      .csv("in/dataforsql.txt")

    df.createTempView("v_user_login")

    sparkSession.sql(
      s"""

         |select
         |uid,
         |min(dt) as start_dt,
         |max(dt) as end_dt,
         |count(1) as counts
         |from(
         |select
         |uid,
         |dt,
         |row_number,
         |date_sub(dt, row_number) as date_sub
         |from(
         |  select
         |  uid,
         |  dt,
         |  row_number() over(partition by uid order by dt asc) as row_number
         |from (
         |  select
         |    t1.dis.uid uid,
         |    t1.dis.dt dt
         |  from (
         |    select
         |      distinct(uid, dt) dis
         |    from
         |      v_user_login
         |  )t1
         |)t2
         |order by uid, row_number
         |)t3
         |)t4
         |group by uid, date_sub having counts >= 3
         |""".stripMargin).show()
  }
}
