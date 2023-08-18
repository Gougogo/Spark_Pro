package com.goumi.day10

import com.goumi.utils.{SparkUtils, UidPartitioner}
import org.apache.spark.{Partitioner, SparkContext}
import org.apache.spark.rdd.RDD

import java.text.SimpleDateFormat
import java.util
import java.util.{Calendar, Date}
import scala.collection.mutable

object ContinuedLoginUser2 {
  def main(args: Array[String]): Unit = {
    val sc: SparkContext = SparkUtils.createContext()
    val lines: RDD[String] = sc.textFile("in/data1.csv")

    //数据预处理
    val uidAndDt: RDD[(String, String)] = lines.map(e => {
      val fields: Array[String] = e.split(",")
      val uid: String = fields(0)
      val dt: String = fields(1)
      (uid, dt)
    }).distinct()

    //这里去重的的原因是前面的去重是针对(uid,dt)去重的，这里想要的是所有的用户
    val uids: Array[String] = uidAndDt.keys.distinct().collect()
    UidSortPartitioner.apply(uids)

    val rdd1: RDD[((String, String), Null)] = {
      //本来可以直接uidAndDt.repartitionAndSortWithinPartitions(UidPartitioner)
      //这就是按照uid进行分区，但如果单独uid作为key，就没法对dt进行排序
      //因为spark的排序是针对key的，那说明需要将key，value合在一起
      //然后swap是因为元组的排序是依次排序的
      uidAndDt.map(t => (t.swap, null)).repartitionAndSortWithinPartitions(UidSortPartitioner)
    }

    //rdd1.saveAsTextFile("out/out11")
    //println(rdd1.collect().toBuffer)

    val rdd2: RDD[(String, String)] = rdd1.map(e => (e._1._2, e._1._1))
    var num = 0
    val rdd3 = rdd2.map(e => {
      val dateFormat = new SimpleDateFormat("yyyy-MM-dd")
      val calendar: Calendar = Calendar.getInstance()
      val date: Date = dateFormat.parse(e._2)
      calendar.setTime(date)
      calendar.add(Calendar.DATE, -num)
      val timeDif: Date = calendar.getTime
      val dateDif: String = dateFormat.format(timeDif)
      num += 1
      (e._1, e._2, dateDif)
    })

    rdd3.map {
      case (uid, dt, dif) => ((uid, dif), (dt, "", 1))
    }.reduceByKey(
      (a, b) => {//a,b就表示上一个value和下一个value，
        //传入的是2个value值，返回的就是你想要聚合是成什么样子
        //是2个值相加，比如WordCount，还是别的
        //这里我不仅要求个数和(也就是a._3 + b._3)，而且还要选出a和b中谁大谁小
        //当然也可以用if判断，但比较简洁的是用排序类Ordering
        val start = Ordering[String].min(a._1, b._1)
        val end = Ordering[String].max(a._1, b._1)
        val total = a._3 + b._3
        (start, end, total)
      }).map{
      case ((uid, dif), (start, end, times)) => (uid, times, start, end)
    }.filter(_._2 >= 3).map(e=>(e._1,(e._2,e._3,e._4)))
      .partitionBy(new UidPartitioner(uids)).saveAsTextFile("out/out11")
  }

  object UidSortPartitioner extends Partitioner{
    //由于object不能实例化，所以成员变量，就要通过函数参数的方式传递进来
    private val uidToIndex = new mutable.HashMap[String, Int]()

    def apply(uids:Array[String]): Unit ={
      var index:Int = 0
      for (elem <- uids) {
        uidToIndex(elem) = index
        index+=1
      }
    }

    override def numPartitions: Int = uidToIndex.size

    override def getPartition(key: Any): Int = {
      uidToIndex(key.asInstanceOf[(String,String)]._2)
    }
  }
}