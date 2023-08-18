package com.goumi.day04

import com.goumi.utils.SparkUtils
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

import scala.collection.mutable

object FavTeacher07 {
  def main(args: Array[String]): Unit = {
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
    val OrderingBeans: RDD[OrderingBean] = reduced.map(e => {
      new OrderingBean(e._1._1, e._1._2, e._2)
    })
    val grouped: RDD[(String, Iterable[OrderingBean])] = OrderingBeans.groupBy(t => t.subject)


    val res = grouped.mapValues(it => {
      //这个TreeSet就类似于有界优先队列，在将数据放进来的时候，就会按照大小顺序放置
      val sorter: mutable.TreeSet[OrderingBean] = new mutable.TreeSet[OrderingBean]()

      //println("it="+it)
      //将迭代器中的数据一条条添加到TreeSet中，it就表示同一个组内的数据，现在就要将这个it放到sorter里去
      /*it.foreach(e => {
        println("e=" + e)
        sorter += e
        if (sorter.size > topN)
          sorter -= sorter.last
      })*/
      it.foreach(e => {
        sorter += e
      })

      val sorterRes: mutable.TreeSet[OrderingBean] = new mutable.TreeSet[OrderingBean]()

      val beans = new Array[OrderingBean](3)
      sorter.copyToArray(beans,0,3)

      beans.foreach(e=>{
          sorterRes+=e
      })
      sorterRes
    })

    println(res.collect().toBuffer)

    sc.stop()
  }
}
