package com.goumi.day04


import com.goumi.utils.SparkUtils
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object FavTeacher01 {
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

    //reduced.groupByKey()
    val grouped: RDD[(String, Iterable[((String, String), Int)])] = reduced.groupBy(t => t._1._1)

    val res: RDD[(String, List[(String, Int)])] = grouped.mapValues(it => {
      it.toList.sortBy(-_._2).map(t => (t._1._2, t._2)).take(2)
    })

    println(res.collect().toBuffer)

    sc.stop()

  }

}
