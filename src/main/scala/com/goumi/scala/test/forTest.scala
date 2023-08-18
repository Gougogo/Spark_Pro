package com.goumi.scala.test

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object forTest {
  def main(args: Array[String]): Unit = {
    val tuples1 = List(1,2,1,2)
    val tuples2 = List("a","b")

    //使用临时变量存储新集合
    //val list: mutable.Set[(Int, String)] = mutable.Set()
    //val list: List[(Int, String)] = List((0, "null"))
    val buffer: ListBuffer[(Int, String)] = ListBuffer()
    for (value1 <- tuples1) {
      for (value2 <- tuples2) {
        //println("a")
        buffer.append((value1, value2))
      }
    }

    val tuples: List[(Int, String)] = for (var1 <- tuples1; var2 <- tuples2) yield (var1, var2)

    tuples.foreach(println)
    //buffer.foreach(println)

    val num = 10

    val range: Range = 0 until num

    val func1: (Int, Int) => Int = (a: Int, b: Int) => {
      println("func1")
      a + b
    }

    val func2: (Int, Int) => Int = (m: Int, n: Int) => {
      println("func2")
      m + n
    }

  }
}
