package com.goumi.scala.test

object Interface3 {
  def main(args: Array[String]): Unit = {
    val mysql = new MySql()

    mysql.insert()
  }
}

trait Operate {
  println("Operate.......")

  def insert(): Unit ={
    println("插入数据")
  }
}

trait DB extends Operate{
  println("DB.....")

  override def insert(): Unit ={
    print("向数据库")
    super.insert()
  }
}

trait File extends Operate{
  println("File.....")

  override def insert(): Unit ={
    print("向文件")
    super.insert()
  }
}

class MySql extends File with DB{

}


