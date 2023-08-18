package com.goumi.scala.test

import scala.collection.mutable

object QueueTest {
  def main(args: Array[String]): Unit = {
    val ints: mutable.Queue[Int] = mutable.Queue(1, 2, 3)

    val ints2: mutable.Queue[Int] = mutable.Queue(1, 2, 3)

    ints++=ints2

    println(ints)
  }

}
