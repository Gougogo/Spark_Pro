package com.goumi.scala.test

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

object DateTest {
  def main(args: Array[String]): Unit = {
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd")
    val calendar: Calendar = Calendar.getInstance()
    val date: Date = dateFormat.parse("2023-02-15")
    calendar.setTime(date)
    calendar.add(Calendar.DATE, -1)
    val timeDif: Date = calendar.getTime
    val dateDif: String = dateFormat.format(timeDif)
    println(dateDif)
  }

}
