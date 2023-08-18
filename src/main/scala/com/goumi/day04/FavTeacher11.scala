package com.goumi.day04

import com.goumi.utils.SparkUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkContext}

import scala.collection.mutable

object FavTeacher11 {
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
    }).reduceByKey(_+_)

    val reduced: RDD[((String, String), Int)] = subjectAndNameAndOne.reduceByKey(_ + _)
    val changeKeyRDD: RDD[((Int, String, String), Null)] = reduced.map(e => {
      ((e._2, e._1._1, e._1._2), null)
    })

    //要创建分区器首先要知道有多少个分区，分区是按照学科进行划分的
    //去重是因为同一个老师可能在多个学科中任职
    //收集到Driver端是因为后面可以用
    val subjects: Array[String] = reduced.map(_._1._1).distinct().collect()
    implicit val ord = Ordering[Int].on[(Int, String, String)](t=>t._1).reverse
    val repartRDD: RDD[((Int, String, String), Null)] =
      changeKeyRDD.repartitionAndSortWithinPartitions(new SubjectPartitioner(subjects))

    repartRDD.saveAsTextFile("out/out7")
    sc.stop()
  }

  //通过主构造器可以将学科信息传到分区器中
  //分区器是在Driver端创建的
  class  SubjectPartitioner(val subjects: Array[String]) extends Partitioner{
    override def numPartitions: Int = subjects.length

    //重点是这个分区方法，传入的是要被分区的数据的key，返回值是该key对应数据的分区编号，而不是所有数据的分区编号
    //这里比较巧妙，比如学科就3种，语文，数学，英语，全在subjects里面
    //现在需要根据学科进行分区，并且分区编号随便设置就可以，比如语文为0号
    //数学为1号，英语为2号。其实就相当于怎么给一个数组中的数据，从0开始编号。
    //说明需要先将所有数据的分区编号计算出来，然后再传入key，获取该key对应的value值对应的分区编号
    //就相当于需要将学科和对应的分区编号装到一个数据结构中
    val rules = new mutable.HashMap[String, Int]()
    var index = 0
    for (elem <- subjects) {
      rules(elem) = index
      index = index+1
    }

    override def getPartition(key: Any): Int = {
      //需要先将key的类型从Any转为所属类型
      val tuple: (Int, String, String) = key.asInstanceOf[(Int, String, String)]
      rules(tuple._2)
    }
  }
}
