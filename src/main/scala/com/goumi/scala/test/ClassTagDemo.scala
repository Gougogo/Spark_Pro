package com.goumi.scala.test

class Animal
object ClassTagDemo {
  def main(args: Array[String]): Unit = {
    val myMap: collection.Map[String, Any] = Map("Number" -> 1, "Greeting" -> "Hello World",      "Animal" -> new Animal)
    //val number:Int = myMap("Number")
    val number:Any = myMap("Number")
    println("number is " + number)

    val number1: Int = myMap("Number").asInstanceOf[Int]
    println("number  is " + number1)
    val greeting: String = myMap("Number").asInstanceOf[String]
  }

}
/* 下面注释的代码将会不通过编译     * Any不能被当时Int使用     */
//val number:Int = myMap("Number")
// println("number is " + number)
// 使用类型转换，可以通过编译    val number: Int = myMap("Number").asInstanceOf[Int]    println("number  is " + number)
// 下面的代码将会抛出ClassCastException    val greeting: String = myMap("Number").asInstanceOf[String]  }
//class Animal {  override def toString = "I am Animal"}
// getValueFromMap for the Int, String and Animaldef getValueFromMapForInt(key: String, dataMap: collection.Map[String, Any]):Option[Int] =  dataMap.get(key) match {    case Some(value: Int) => Some(value)
// case _ => None  }def getValueFromMapForString(key: String, dataMap: collection.Map[String, Any]: Option[String] =  dataMap.get(key) match {    case Some(value: String) => Some(value)
// case _ => None  }def getValueFromMapForAnimal(key: String, dataMap: collection.Map[String, Any]: Option[Animal] =  dataMap.get(key) match {    case Some(value: Animal) => Some(value)
// case _ => None  }def main(args: Array[String]): Unit = {  val myMap: collection.Map[String, Any] = Map("Number" -> 1, "Greeting" -> "Hello World", "Animal" -> new Animal)
// returns Some(1)  val number1: Option[Int] = getValueFromMapForInt("Number", myMap)  println("number is " + number1)
// returns None  val numberNotExists: Option[Int] = getValueFromMapForInt("Number2", myMap)  println("number is " + numberNotExists)  println
// returns Some(Hello World)  val greeting: Option[String] = getValueFromMapForString("Greeting", myMap)  println("greeting is " + greeting)
// returns None  val greetingDoesNotExists: Option[String] = getValueFromMapForString("Greeting1", myMap)  println("greeting is " + greetingDoesNotExists)  println()
// returns Some[Animal]  val animal: Option[Animal] = getValueFromMapForAnimal("Animal", myMap)  println("Animal is " + animal)
// returns None  val animalDoesNotExist: Option[Animal] = getValueFromMapForAnimal("Animal1", myMap)  println("Animal is " + animalDoesNotExist)}