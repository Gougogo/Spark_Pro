package com.goumi.scala.test

import scala.collection.mutable.ArrayBuffer

object ArrayTest {
  def main(args: Array[String]): Unit = {
    val ints: Array[Int] = Array.tabulate(3)(a => a + 5)

    val ints1: Array[Int] = Array(1, 2, 3, 4, 4)

    //println(ints1.head)

    //println(ints1)

    for(e <- ints1){
      //print(e)
    }

    //ints1.foreach(println)
    //ints1.foreach(x=>println(x))

    ints1 :+ (10)

    //ints2.foreach(println)
    ints1.foreach(println)
    //println(ints1)
    //println(ints2)

    val ints3: ArrayBuffer[Int] = ArrayBuffer(1, 2, 3, 4, 4)
    //ints3.foreach(println)

    val ints4: ints3.type = ints3 += (9)
    //println(ints3 == ints4)
    //println(ints3)
    //println(ints4)
  }
}
