package com.goumi.day07

import com.goumi.utils.SparkUtils
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object CustomSort04 {
  def main(args: Array[String]): Unit = {
    //先按照颜值的从高到底排序，如果颜值相等，再按照年龄的升序排序
    val sc: SparkContext = SparkUtils.createContext(true)

    //创建
    val lst = List("老段,38,99.99", "念行,30,99.99", "老赵,36,9999.99")
    //注意元组的比较方式是依次比较，所以想要先比较颜值，就需要将颜值放到第一位
    val data: RDD[String] = sc.parallelize(lst)
    val tpRDD: RDD[Man1] = data.map(e => {
      val fields: Array[String] = e.split(",")
      //都不用new，因为Man1是一个样例类
      Man1(fields(0), fields(1).toInt, fields(2).toDouble)
    })

    import OrderContext.OrderingMan
    println(tpRDD.sortBy(e => e).collect().toBuffer)

  }
}

case class Man1(val name: String, val age: Int, val fv:Double)
