package com.goumi.bigdata.spark.rdddemo

object TestScala {
  def main(args: Array[String]): Unit = {
    var s: Short = 5

    var c: Char = 'a'
    var i: Int = 5
    var d: Float = .314F
    var result: Double = c + i + d

    val days = Array("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    days.zipWithIndex.foreach {
      case (day, count) => println(s"$count is $day")
    }


  }
}
