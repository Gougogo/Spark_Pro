package com.goumi.scala.test

object FlatMapTest {
  def main(args: Array[String]): Unit = {
    val list = List(List(2, 3), List(3, 4))
    val list1: List[Any] = list.flatMap {
      any => {
        if (any.isInstanceOf[List[Int]]) {
          any.asInstanceOf[List[Int]]
        } else {
          List(any)
        }
      }
    }
    println(list1)
    //list1.foreach(println)
  }
}
