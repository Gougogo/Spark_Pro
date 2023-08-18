package com.goumi.day08

import com.goumi.utils.{DateUtils, SparkUtils}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object ThreadNotSafeDemo {
  def main(args: Array[String]): Unit = {
    //需求是将普通字符串，转为Long类型的时间戳
    val sc: SparkContext = SparkUtils.createContext()
    //字符串转Date格式，一般是使用工具类，提供要转的Date格式
    //以后有很多的日期类格式，所以不能简单的在函数内部就直接new一个Date工具类
    val lines: RDD[String] = sc.textFile("in/date.txt")
    val res: RDD[Long] = lines.map(e => {
      //new DateUtils("yyyy-MM-dd").parseDateToNums(e)
      DateUtils.parse(e)
    })
    println(res.collect().toBuffer)
  }
}
