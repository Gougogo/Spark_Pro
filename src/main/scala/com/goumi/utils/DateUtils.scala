package com.goumi.utils

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}
object DateUtils {
  //多个方法共用一个对象
  val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  def parse(str: String):Long={
    //有个问题是，每来一条数据，都会new一个工具类对象
    //val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    synchronized{
      val date: Date = dateFormat.parse(str)
      date.getTime
    }
  }
}

class DateUtils(getDateFormat:String) {
  //多个方法共用一个对象
  val dateFormat = new SimpleDateFormat(getDateFormat)

  def parseDateToNums(str: String):Long={
    //有个问题是，每来一条数据，都会new一个工具类对象
    //val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date: Date = dateFormat.parse(str)
    date.getTime
  }

  def parseDateFromNum(dt : String, num:Int): String ={
    val calendar: Calendar = Calendar.getInstance()
    val date: Date = dateFormat.parse(dt)
    calendar.setTime(date)
    calendar.add(Calendar.DATE, -num)
    val timeDif: Date = calendar.getTime
    dateFormat.format(timeDif)
  }
}
