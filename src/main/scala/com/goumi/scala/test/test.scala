package com.goumi.scala.test

class Yse(){

}

class Student(s:String, ss:String) {
  val sname = "zhangsan"

  def test(): Unit ={
    println(s, ss)
  }
}

object Student {
  def apply : Student = new Student("asd", "qwe")

  def test() = {
    apply.sname
  }
}

object test {
  def main(args: Array[String]): Unit = {

    val student = Student
    println(student.test())

    println(Student.apply)

    def test() {
      "asd"
    }

    println(test)

    def f7(f : (Int)=>Unit):Unit = {
        f(20)
    }

    /*def f8(i : Int) ={
      println(i)
    }

    f7(f8)*/

    f7((i : Int)=>{
      println(i)
    })

    f7(println(_))

    def testAdd(f : (Int, Int)=> Int): Int={
      f(10, 100)
    }

    def f10(x: Int, y: Int)={
      x+y
    }

    println(testAdd(f10))

    println(testAdd((x: Int, y: Int)=>{x+y}))
    println(testAdd((x, y)=>{x+y}))
    println(testAdd((x, y)=>x+y))

    println(testAdd(_+_))

    def f1 = "asd"
    println(f1)

    def f2() :String={
      "asd"
    }
    println(f2())

    var num = 2.8.toInt
    println(num)

    def test1(name: String*) = {

    }

    def f11(i : Int) ={
      def f2(j : Int) = {
        i * j
      }

      f2 _
    }

    val intToInt : (Int) => Int = f11(10)

    val id: Int = intToInt(10)

    println(id)



  }
}
