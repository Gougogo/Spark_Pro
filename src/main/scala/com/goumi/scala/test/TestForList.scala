package com.goumi.scala.test

import scala.collection.{immutable, mutable}
import scala.collection.mutable.{Builder, ListBuffer}

object TestForList {
  def main(args: Array[String]): Unit = {
    val ints = List(1, 2, 3, 3)
    //ints.foreach(println)
    val ints1: List[Int] = ints :+ 1

    //ints1.foreach(println)

    //println(ints == ints1)
    //println(ints)
    //println(ints1)

    val ints2: ListBuffer[Int] = ListBuffer(1, 2, 3, 4, 4)
    val ints3: ListBuffer[Int] = ints2 :+ 8
    //ints3.foreach(println)

    //println(ints2 == ints3)


    //ints.groupBy(x=>x).foreach(println)

    ints.sortBy(x=>x)

    val strs = List("13", "22", "31")
    //strs.sortBy(x=>x.substring(1)).foreach(println)

    val words = "The quick brown fox jumped over the lazy dog".split(' ')
    // this works because scala.Ordering will implicitly provide an Ordering[Tuple2[Int, Char]]
    //words.sortBy(x => (x.length, x.head)).foreach(println)
    //res0: Array[String] = Array(The, dog, fox, the, lazy, over, brown, quick, jumped)

    //List(1,2,3,4).sortWith(_.compareTo(_) > 0).foreach(print)

    //List("Steve", "Tom", "John", "Bob").sortWith((e1,e2)=> e1<e2).foreach(print)
      //= List("Bob", "John", "Steve", "Tom")

    //words.sortWith()

    val list = List(1, List(2, 3), List(2, 3), List(2, 3))

    list.flatMap(x=>{
      if(x.isInstanceOf[List[Int]])
        x.asInstanceOf[List[Int]]
      else
        List(x)
    })

    val list1: List[Any] = list.flatMap(x => {
      x match {
        case ints4: List[Int] => ints4
        case _ => List(x)
      }
    })

    val list2 = List(List(2, 3), List(2, 3), List(2, 3))
    //list2.foreach(println)

    list2.flatMap(x=>x).foreach(println)

    val flatten: List[Int] = list2.flatten

    val strings = List("asd asd", "asd sdw")
    val stringses: List[Array[String]] = strings.map(x => x.split(" "))
    stringses.foreach(x=>x.foreach(println))
    val flatten1: List[String] = stringses.flatten
    flatten1.foreach(println)



    Thread.sleep(100000)


  }
}
