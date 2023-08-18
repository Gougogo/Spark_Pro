package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import java.sql.{DriverManager, PreparedStatement, ResultSet}

object MapAndMapPartitionsDiffDemo {
  def main(args: Array[String]): Unit = {
    val config1: SparkConf = new SparkConf().setMaster("local[*]").setAppName("DistinctDemo")
    val sc = new SparkContext(config1)
    val listRDD: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 1, 2, 3, 4), 2)
    val array: Array[List[Int]] = Array(List(1, 2), List(3, 4))

    listRDD.map(
      e => {
        val connection = DriverManager.getConnection("", "", "")

        val pstm: PreparedStatement = connection.prepareStatement("select name from tb_category where id = 4")
        pstm.setInt(1, e)
        val rs: ResultSet = pstm.executeQuery()
        var name: String = null
        if (rs.next()) {
          name = rs.getString(1)
        }
        rs.close()
        pstm.close()
        connection.close()
        (e, name)
      }
    )

    val rdd2: RDD[(Int, String)] = listRDD.mapPartitions(
      it => {
        val connection = DriverManager.getConnection("", "", "")
        val pst2m2: PreparedStatement = connection.prepareStatement("select name from tb_category where id = 4")

        val niter: Iterator[(Int, String)] = it.map(e => {
          pst2m2.setInt(1, e)
          val rs: ResultSet = pst2m2.executeQuery()
          var name: String = null
          if (rs.next()) {
            name = rs.getString(1)
          }
          rs.close()
          (e, name)
        })
        pst2m2.close()
        connection.close()
        niter
      }
    )

    val res: Array[(Int, String)] = rdd2.collect()
    println(res.toBuffer)
  }
}
