package com.goumi.utils

object UtilsTest {
  def main(args: Array[String]): Unit = {
    val utils = new DateUtils("yyyy-MM-dd")
    val l: Long = utils.parseDateToNums("2020-01-01")

    println(l)
  }

}
