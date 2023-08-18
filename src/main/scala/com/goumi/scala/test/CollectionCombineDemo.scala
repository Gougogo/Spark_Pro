package com.goumi.scala.test

import scala.collection.mutable.ListBuffer

object CollectionCombineDemo {
  def main(args: Array[String]): Unit = {
    val ints1 = List(1, 2, 1, 2)
    val ints2 = List("a", "b")
    val tuples: ListBuffer[(Int, String)] = ListBuffer((0, ""))

    for(value1 <- ints1) {
      for(value2 <- ints2){
        tuples.append((value1, value2))
        val tuples1: ListBuffer[(Int, String)] = tuples :+ (value1, value2)
        //println(tuples :+ (value1, value2))
        println(tuples1 == tuples)
      }
    }
    //println("============")

    println(tuples)

  }
}
