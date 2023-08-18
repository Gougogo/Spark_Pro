package com.goumi.day04

import com.goumi.utils.SparkUtils
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel

object FavTeacher02 {
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


    val subjects: Array[String] = reduced.map(e => {
      e._1._1
    }).distinct().collect()

    reduced.persist(StorageLevel.MEMORY_ONLY)
    for (elem <- subjects) {
      val filterRes: RDD[((String, String), Int)] = reduced.filter(e => {
        if (e._1._1 == elem)
          true
        else
          false
      })
      //val res: Array[((String, String), Int)] = filterRes.sortBy(((e: ((String, String), Int)) => e._2), false).take(3)
      //val res: Array[((String, String), Int)] = filterRes.sortBy((e => e._2), false).take(3)
      val res: Array[((String, String), Int)] = filterRes.sortBy((_._2), false).take(3)
      println(res.toBuffer)
    }

    sc.stop()

  }

}
