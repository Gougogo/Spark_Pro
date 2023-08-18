package com.goumi.day04

import com.goumi.utils.SparkUtils
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

import scala.collection.mutable

object FavTeacher06 {
  def main(args: Array[String]): Unit = {
    //将数据变为(bigdata, laozhang, 4)，(bigdata, laoli, 4)，(bigdata, zhao, 4)，(bigdata, laohu, 3)，(bigdata, laodu, 2)
    val sc: SparkContext = SparkUtils.createContext(true)
    val lines: RDD[String] = sc.textFile("in/teacher3.log")
    val subjectAndNameAndOne: RDD[((String, String), Int)] = lines.map(e => {
      val fields: Array[String] = e.split("/")
      val subject: String = fields(2).split("[.]")(0)
      val name: String = fields(3)
      ((subject, name), 1)
    })
    val topN = 3
    val reduced: RDD[((String, String), Int)] = subjectAndNameAndOne.reduceByKey(_ + _)

    //将数据封装为Bean，并分组
    val OrderingBeans: RDD[OrderingBean] = reduced.map(e => {
      new OrderingBean(e._1._1, e._1._2, e._2)
    })
    val grouped: RDD[(String, Iterable[OrderingBean])] = OrderingBeans.groupBy(t => t.subject)

    //分好组之后，就是对组内数据进行排序，也就是使用TreeSet这种数据结构排序
    //实现方式就是将一个个的Bean放到TreeSet中就会被自动排序
    val res = grouped.mapValues(it => {
      //这个TreeSet就类似于有界优先队列，在将数据放进来的时候，就会按照大小顺序放置
      var sorter: mutable.TreeSet[OrderingBean] = new mutable.TreeSet[OrderingBean]()

      //将迭代器中的数据一条条添加到TreeSet中，it就表示同一个组内的数据，现在就要将这个it放到sorter里去
      it.foreach(e => {
        println("e="+e)
        sorter = sorter + e
      })
      sorter
    })

    println(res.collect().toBuffer)
    sc.stop()
  }
}
