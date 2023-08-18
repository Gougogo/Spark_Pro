package com.goumi.scala.test

import scala.collection.GenTraversableOnce

object Implicit {
  def main(args: Array[String]): Unit = {
    val ints1: List[Int] = 1 :: 2 :: 3 :: Nil

    val ints2 = List(1, 2, 3, 4, 5)

    val ints3: List[Int] = ints2.drop(3)

    //ints3.foreach(println)

    val ints = Set(1, 2, 3)
    ints - 3
    ints.apply(2)

    val tuple: (Int, Int) = (1, 2)
    val intToInt = Map(tuple)
    //println(intToInt.keys)

    //intToInt.foreach((tuple)=>{println(tuple)})


    val ints4 = List(1, 2, 5, 4, 1, 2, 3)
    //println(ints4.reverse)
    //ints4.groupBy()
    val intToInts: Map[Int, List[Int]] = ints4.groupBy(x => x)
    //intToInts.foreach(t=>{println(t._1 + "=" + t._2)})
    //println(ints4.sortBy(x => x))

    val ints5: List[Int] = ints4.map(x => x)
    //println(ints4 == ints5)

    val tuples: List[(Int, Int)] = ints4.map(x => {
      (x, x)
    })

    val strings = List("a", "b", "c", "a", "d")
    val stringToStrings: Map[String, List[String]] = strings.groupBy(x => x)
    //println(stringToStrings)
    val stringToInt: Map[String, Int] = stringToStrings.map(t => (t._1, t._2.size))


    val tuples1: List[(Int, String)] = ints4.zip(strings)

    val i: Int = ints4.reduce((x, x1) => {x+x1})
    ///println(i)

    val arr = List(1, 2, 3, 4)

    val i1: Int = arr.fold(1)((x, y) => (x - y))
    //println(i1)

    val strings1 = List("asd asd", "asd qwe")
    val strings2: List[String] = strings1.flatMap(x => x.split(" "))
    //strings2.foreach(println)
    //println(strings2)

    println(strings1.flatMap(x=>List(1,2,3,4)))

    val list: List[Any] = List(1, List(2, 3), List(4, 5, 6), "asd")

    println(list)

    val list1: List[Any] = list.flatMap(x => {
      if (x.isInstanceOf[List[Int]]) {
        x.asInstanceOf[List[Int]]
      } else {
        List(x)
      }
    })

    println(list1)

    val list2 = List("asd asd", "asd 2we")
    val strings3: List[String] = list2.flatMap(string => string.split(" "))
    println(strings3)

    def test[T <: User](t : T) ={
      println(t)
    }
  }

  class Person{

  }

  class User extends Person{

  }

  class Child extends User{

  }
}
