package com.goumi.day04

import com.goumi.utils.SparkUtils
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object FavTeacher03 {
  def main(args: Array[String]): Unit = {

    val sc: SparkContext = SparkUtils.createContext(true)

    val lines: RDD[String] = sc.textFile("in/teacher2.log")

    //((bigdata, laozhang), 1)
    val subjectAndNameAndOne: RDD[((String, String), Int)] = lines.map(e => {
      val fields: Array[String] = e.split("/")
      //fields.foreach(println)
      val subject: String = fields(2).split("[.]")(0)
      //subject.foreach(println)
      val name: String = fields(3)
      ((subject, name), 1)
    })

    val reduced: RDD[((String, String), Int)] = subjectAndNameAndOne.reduceByKey(_ + _)

    //筛选之后调SortBy再take效率低，因为有shuffle，并且是内存加磁盘的方式进行排序，RangePartitioner，桶排序嘛
    val filtered: RDD[((String, String), Int)] = reduced.filter(_._1._1.equals("bigdata"))

    //可以将int数据放到前面，然后用top或者takeOrdered算子来取，不用额外定义排序规则
    //reduced.top()
    //如果不加implicit的话，就需要明确传进去，如果加了的话，就不用传，因为参数是隐式参数,就是说可传也可不传
    implicit val ord: Ordering[((String, String), Int)] = new Ordering[((String, String), Int)] {
      override def compare(x: ((String, String), Int), y: ((String, String), Int)) = {
        -(x._2 - y._2)
      }
    }

    val res: Array[((String, String), Int)] = filtered.takeOrdered(2)

    println(res.toBuffer)

    sc.stop()
  }
}
