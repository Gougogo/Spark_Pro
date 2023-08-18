package com.goumi.scala.test

object OptionTest {
  def main(args: Array[String]): Unit = {
    val a:Option[Int] = Some(5)
    val b:Option[Int] = Some(100)

    val myMap: Map[String, String] = Map("key1" -> "value")

    println("a.getOrElse(0): " + a.getOrElse(10))
    println(b.getOrElse("None Data"))
    println(myMap.get("22") )

    val sites = Map("runoob" -> "www.runoob.com", "google" -> "www.google.com")

    println("show(sites.get( \"runoob\")) : " +
      show(sites.get( "runoob")) )
    println("show(sites.get( \"baidu\")) : " +
      show(sites.get( "baidu")) )

  }

  def show(x: Option[String]) = x match {
    case Some(s) => s
    case None => "?"
  }

}
