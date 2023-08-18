package com.goumi.day05
import com.alibaba.fastjson.{JSON, JSONException, JSONObject}
import com.goumi.utils.SparkUtils
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

import java.util

object getPairFromJson {
  def main(args: Array[String]): Unit = {
    val sc: SparkContext = SparkUtils.createContext()
    val lines: RDD[String] = sc.textFile("in/order2.json")

    val res: RDD[JSONObject] = lines.map(f = e => {
      println(e)
      val orderPair: JSONObject = JSON.parseObject(e, classOf[JSONObject])
      println(orderPair)
      orderPair
    })

    val value: RDD[(AnyRef, AnyRef)] = res.map(e => {
      (e.get("oid"), e.get("money"))
    })

    println(value.collect().toBuffer)
  }
}
