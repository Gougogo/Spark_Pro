package com.goumi.day04

import com.goumi.utils.SparkUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkContext}

import scala.collection.mutable

object FavTeacher09 {
  def main(args: Array[String]): Unit = {

    val sc: SparkContext = SparkUtils.createContext(true)

    val lines: RDD[String] = sc.textFile("in/teacher3.log")

    //((bigdata, laozhang), 1)
    val subjectAndNameAndOne: RDD[((String, String), Int)] = lines.map(e => {
      val fields: Array[String] = e.split("/")
      //fields.foreach(println)
      val subject: String = fields(2).split("[.]")(0)
      //subject.foreach(println)
      val name: String = fields(3)
      ((subject, name), 1)
    })

    val subjects: Array[String] = subjectAndNameAndOne.map(_._1._1).distinct().collect()

    //不加自定义分区器，那么使用的就是HashPartitioner
    val partitioned: RDD[((String, String), Int)] = subjectAndNameAndOne.reduceByKey(new SubjectPartitioner(subjects),_ + _)

    val topN: Int = 3
    partitioned.foreachPartition(it=>{
      //将数据分区之后，就是将各个分区中的数据进行排序并取topN，这里排序是使用的一个数据结构，TreeSet
      //只要将数据一条条的添加到TreeSet中，里面的数据就排好序了
      implicit val ord = Ordering[Int].on[((String, String), Int)](t => -t._2)
      val sorter = new mutable.TreeSet[((String, String), Int)]()

      it.foreach(t=>{
        sorter += t
        if (sorter.size > topN){
          sorter -= sorter.last
        }
      })

      println(sorter.toBuffer)
      sorter.iterator
    })
    //Thread.sleep(1000000)
    sc.stop()
  }

  //通过主构造器可以将学科信息传到分区器中
  //分区器是在Driver端创建的
  class  SubjectPartitioner(val subjects: Array[String]) extends Partitioner{
    override def numPartitions: Int = subjects.length

    val rules = new mutable.HashMap[String, Int]()
    var index = 0
    for (elem <- subjects) {
      rules(elem) = index
      index = index+1
    }

    override def getPartition(key: Any): Int = {
      //需要先将key的类型从Any转为所属类型
      val tuple: (String, String) = key.asInstanceOf[(String, String)]
      rules(tuple._1)
    }
  }
}
