package com.goumi.scala.test

object reduceDemo {
  def main(args: Array[String]): Unit = {
    val ints = List(1, 2, 3, 4, 6)
    val i: Int = ints.reduce(_ + _)

    //reversed.reduceLeft[B]((x, y) => op(y, x))

    println(ints.reverse.reduceLeft((x, y) => x - y))

    println(ints.reduceRight(_ - _))
  }

}
