package com.goumi.scala.test

import scala.io.Source


object RDDTest2 {
  def main(args: Array[String]): Unit = {
    val lines: Iterator[String] = Source.fromFile("in/lazy2.txt").getLines()


    val ints1: Iterator[String] = lines.filter(
      e => {
        println("to Filter")
        e.startsWith("h")
      }
    )

    val ints2: Iterator[String] = ints1.map(
      e => {
      println("to *10")
      e.toUpperCase
    })

    while (ints2.hasNext){
      val i: String = ints2.next()
      println(i)
    }
  }
}
