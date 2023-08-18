package com.goumi.day04

import com.goumi.utils.SparkUtils
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

import scala.collection.mutable

object FavTeacher05 {
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

    val topN = 3


    val reduced: RDD[((String, String), Int)] = subjectAndNameAndOne.reduceByKey(_ + _)

    //reduced.groupByKey()
    val grouped: RDD[(String, Iterable[((String, String), Int)])] = reduced.groupBy(t => t._1._1)

    val res: RDD[(String, mutable.TreeSet[((String, String), Int)])] = grouped.mapValues(it => {
      //it.toList.sortBy(-_._2).map(t => (t._1._2, t._2)).take(2)
      //这个TreeSet就类似于有界优先队列，在将数据放进来的时候，就会按照大小顺序放置
      //并且可以自定义排序规则，如果是int类型，可以加负号表示降序，如果是字符串，没法加负号，所以需要调用reverse方法
      implicit val ord = Ordering[Int].on[((String, String), Int)](t => t._2).reverse
      val sorter: mutable.TreeSet[((String, String), Int)] = new mutable.TreeSet[((String, String), Int)]()

      //将迭代器中的数据一条条添加到TreeSet中，it就表示同一个组内的数据，现在就要将这个it放到sorter里去
      it.foreach(e => {
        sorter += e
        if (sorter.size > topN)
          sorter -= sorter.last
      })
      sorter
    })

    println(res.collect().toBuffer)

    sc.stop()
  }
}
