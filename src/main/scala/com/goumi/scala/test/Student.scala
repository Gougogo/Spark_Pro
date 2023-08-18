package com.goumi.scala.test

/**
 * @auther GouMi
 * @version 1.0
 */
class Stu(s:String, ss:String) {
  val sname = "zhangsan"

  def test1(): Unit ={
    println(this.s, this.ss)
  }

  def apply(s1: String) = {
    println(s1)
  }
}

object Stu {
  def apply(s:String, ss: String) : Stu = new Stu(s, ss)

  def test2() = {
    apply("asd", "qwe").test1()
    println("aaaa")
  }
}

object Student01 {
  def main(args: Array[String]): Unit = {
    val student: Stu.type = Stu

    //student.test1()
    student.test2()
    Stu.test2()

    val stu = new Stu("asd", "ewrt")
    stu.apply("asd")
    stu("goumi")
    stu.test1()
  }
}
