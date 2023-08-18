package com.goumi.scala.test

import scala.io.Source


object RDDTest {
  def main(args: Array[String]): Unit = {
    val lines: Iterator[String] = Source.fromFile("in/lazy.txt").getLines()

    val ints: Iterator[Int] = lines.map(
      e => {
        println("to Int")
        val int: Int = e.toInt
        int
      }
      )

    val ints1: Iterator[Int] = ints.filter(
      e => {
        println("to Filter")
        e % 2 == 0
      }
    )

    val ints2: Iterator[Int] = ints1.map(
      e => {
      println("to *10")
      e * 10
    })

    while (ints2.hasNext){
      val i: Int = ints2.next()
      println(i)
    }
  }
}
