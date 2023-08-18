package com.goumi.scala.test

object Interface {
  def main(args: Array[String]): Unit = {
    val user1 = new User15()

    println(user1.isInstanceOf[User15])
    true.asInstanceOf[User15]
  }

}

trait TestTrait15{
  println("1111")
}

class Person15 extends TestTrait15{
  println("22222")
}

class User15 extends Person15 with TestTrait15{
  println("3333")
}
