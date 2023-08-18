package com.goumi.day04

import com.goumi.utils.SparkUtils
import org.apache.spark.rdd.{RDD, ShuffledRDD}
import org.apache.spark.{Aggregator, Partitioner, SparkContext}

import scala.collection.mutable

object FavTeacher10 {

  def sortByKeyInPart(self:RDD[((String, String), Int)], subjects: Array[String]) ={
    val tmpRDD: RDD[((Int, String, String), Null)] = self.map(e => {
      ((e._2, e._1._1, e._1._2), null)
    })

    val shuffledRDD = new ShuffledRDD[(Int, String, String), Null, Int](tmpRDD, new SubjectPartitioner10(subjects))
    //Ordering[Int]表示按照Int来排，On后面表示的是原来的数据是什么类型
    //排序必须是针对Key，但是这里的需求是对value进行排序
    implicit val ord = Ordering[Int].on[(Int, String, String)](t => t._1)
    shuffledRDD.setKeyOrdering(ord)
  }


  def main(args: Array[String]): Unit = {

    val sc: SparkContext = SparkUtils.createContext(true)

    val lines: RDD[String] = sc.textFile("in/teacher3.log")

    //((bigdata, laozhang), 1)
    val subjectAndNameAndOne: RDD[((String, String), Int)] = lines.map(e => {
      val fields: Array[String] = e.split("/")
      val subject: String = fields(2).split("[.]")(0)
      val name: String = fields(3)
      ((subject, name), 1)
    }).reduceByKey(_+_)

    val subjects: Array[String] = subjectAndNameAndOne.map(_._1._1).distinct().collect()

    val sortRDD: ShuffledRDD[(Int, String, String), Null, Int] = sortByKeyInPart(subjectAndNameAndOne, subjects)
    /*val res: RDD[(Int, String, String)] = sortRDD.map(e => {
      (e._2, e._1._1, e._1._2)
    }).sortBy(x => x._1)*/
    //
    //这个做法是不对的，因为SortBy本身就有shuffle，相当于又打乱了
    sortRDD.saveAsTextFile("out/out6")
    //println(res.collect().toBuffer)

    sc.stop()
  }

  //通过主构造器可以将学科信息传到分区器中
  //分区器是在Driver端创建的
  class  SubjectPartitioner10(val subjects: Array[String]) extends Partitioner{
    override def numPartitions: Int = subjects.length

    val rules = new mutable.HashMap[String, Int]()
    var index = 0
    for (elem <- subjects) {
      rules(elem) = index
      index = index+1
    }

    override def getPartition(key: Any): Int = {
      //需要先将key的类型从Any转为所属类型
      val tuple: String = key.asInstanceOf[(Int, String, String)]._2
      rules(tuple)
    }
  }
}
