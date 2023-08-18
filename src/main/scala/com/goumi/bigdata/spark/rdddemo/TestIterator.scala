package com.goumi.bigdata.spark.rdddemo

import scala.io.Source


/**
 * @auther GouMi
 * @version 1.0
 */
object TestIterator {
  def main(args: Array[String]): Unit = {
    val lines: Iterator[String] = Source.fromFile("in/lazy.txt").getLines()

    val nums: Iterator[Int] = lines.map(e => {
      println("to int")
      val r: Int = e.toInt
      println(r)
      r
    })

    val even: Iterator[Int] = nums.filter(e => {
      println("filter")
      println(e)
      e % 2 == 0
    })

    val res: Iterator[Int] = even.map(e => {
      println("to product")
      e * 10
    })

    while (res.hasNext){
      val r: Int = res.next()
      //println("szie=" + res.size)
      println(r)
    }
  }
}
