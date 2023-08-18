package com.goumi.day08

import com.goumi.utils.SparkUtils
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

import java.net.InetAddress

object SerTest02 {
  def main(args: Array[String]): Unit = {
    val sc: SparkContext = SparkUtils.createContext(true)
    val lines: RDD[String] = sc.textFile("in/ser.txt")

    val res = lines.map(e => {
      val fields: Array[String] = e.split(",")
      val code: String = fields(0)
      val money: Double = fields(1).toDouble
      val name: String = RulesMapObjectNotSer.rulesMap.getOrElse(code, "未知")
      val threadId: Long = Thread.currentThread().getId
      val hostName: String = InetAddress.getLocalHost.getHostName
      (code, name, money, threadId, hostName, RulesMapObjectNotSer)
    })

    res.saveAsTextFile("out/outgoumi")
  }
}
