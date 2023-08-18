package com.goumi.scala.test

object WordCountDemo {
  def main(args: Array[String]): Unit = {
    val strs = List("a", "b", "s", "b", "s")
    val stringToStrings: Map[String, List[String]] = strs.groupBy(x => x)
    stringToStrings.foreach(println)

    val stringToInt: Map[String, Int] = stringToStrings.map(x => (x._1, x._2.size))
    stringToInt.foreach(println)

    stringToInt.toList.sortWith((e1, e2)=>{
      e1._1>e2._1
    }).foreach(println)
  }

}
