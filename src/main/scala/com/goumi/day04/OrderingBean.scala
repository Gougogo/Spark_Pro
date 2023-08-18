package com.goumi.day04

import scala.collection.mutable.ArrayBuffer


class OrderingBean(val subject:String, val name:String, val count:Int) extends Ordered[OrderingBean] with Serializable {
  //装的数据就是学科，老师，和次数
  val equiv = new ArrayBuffer[(String, String, Int)]()

  equiv += ((subject, name, count))

  //println("OrderingBeanSize= " + equiv+ "," +equiv.size)

  override def compare(that: OrderingBean): Int = {
    //如果次数相等了，那么就将多个科目的信息都保存起来
    //就是说次数只保留一次，而学科和老师拼接在一起
    println("this.count ="+this.name+this.count + "that.count="+that.name+ that.count)
    if (this.count == that.count){
      println("equiv=" + equiv)
      //这里问题很大，应该取that里面ArrayBuffer中的东西，而不是直接取三个参数
      //equiv += ((that.subject, that.name, that.count))

      //之所以要
      for(i <- 0 until that.equiv.size){
          equiv += that.equiv(i)
      }

      println("OrderingBeanSize2222= " + equiv.size+"," + equiv)
      0
    }else{
      //println("OrderingBeanSize= " + equiv+ "," +equiv.size)
      -(this.count - that.count)
    }
  }

  override def toString: String = {
    if (equiv.size > 1)
      equiv.toString()
    else{
      s"($subject, $name, $count)"
    }
  }
}
