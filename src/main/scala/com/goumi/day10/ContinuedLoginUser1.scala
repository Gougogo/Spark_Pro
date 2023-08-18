package com.goumi.day10

import com.goumi.utils.SparkUtils
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

object ContinuedLoginUser1 {
  def main(args: Array[String]): Unit = {
    val sc: SparkContext = SparkUtils.createContext()
    val lines: RDD[String] = sc.textFile("in/data1.csv")
    val grouped: RDD[(String, Iterable[String])] = lines.map(e => {
      val fields: Array[String] = e.split(",")
      val uid: String = fields(0)
      val dt: String = fields(1)
      (uid, dt)
    }).distinct()
      .groupByKey()

    //println(grouped.collect().toBuffer)
    val idAndDiffDate: RDD[(String, (String, String))] = grouped.flatMapValues(e => {
      val sorted: List[String] = e.toList.sortBy(e => e)
      var num = 0
      val dateFormat = new SimpleDateFormat("yyyy-MM-dd")
      val calendar: Calendar = Calendar.getInstance()
      sorted.map(dt => {
        val date: Date = dateFormat.parse(dt)
        calendar.setTime(date)
        calendar.add(Calendar.DATE, -num)
        val timeDif: Date = calendar.getTime
        val dateDif: String = dateFormat.format(timeDif)
        num += 1
        (dt, dateDif)
        //差值相等的就可以分到一起
      })
    })

    val startEndTotal: RDD[(String, Int, String, String)] = idAndDiffDate.map {
      case (uid, (dt, dif)) => ((uid, dif), (dt, "", 1))
    }.reduceByKey(
      (a, b) => {//a,b就表示上一个value和下一个value，
        //传入的是2个value值，返回的就是你想要聚合是成什么样子
        //是2个值相加，比如WordCount，还是别的
        //这里我不仅要求个数和(也就是a._3 + b._3)，而且还要选出a和b中谁大谁小
        //当然也可以用if判断，但比较简洁的是用排序类Ordering
        val start = Ordering[String].min(a._1, b._1)
        val end = Ordering[String].max(a._1, b._1)
        val total = a._3 + b._3
        (start, end, total)
    }).map{
      case ((uid, dif), (start, end, times)) => (uid, times, start, end)
    }.filter(_._2 >= 3)

    println(startEndTotal.collect().toBuffer)
  }
}
