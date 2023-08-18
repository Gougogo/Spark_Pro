package com.goumi.day10

import com.goumi.utils.SparkUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkContext}
import scala.collection.mutable

object ContinuedLoginUser3 {
  def main(args: Array[String]): Unit = {
    val sc: SparkContext = SparkUtils.createContext()
    val lines: RDD[String] = sc.textFile("in/data2.csv")

    //数据预处理
    val uidAndDt: RDD[(String, String)] = lines.map(e => {
      val fields: Array[String] = e.split(",")
      val uid: String = fields(0)
      val dt: String = fields(1)
      (uid, dt)
    }).distinct()

    val uidArr: Array[String] = uidAndDt.keys.distinct().collect()

    val rdd1: RDD[((String, String), Null)] = {
      uidAndDt.map(t => (t.swap, null)).repartitionAndSortWithinPartitions(new UidPartitioner02(uidArr))
    }

    rdd1.saveAsTextFile("out/out12")
  }

  class UidPartitioner02(uidArr:Array[String]) extends Partitioner {

    //如果有10-1000个用户，就分为10等份，如果有1000-10万个用户，就分为100等份
    //1-100                             10-1000

    private val uids: List[String] = uidArr.toList.sorted

    private val uidToIndex = new mutable.HashMap[String, Int]()

    if(10 < uids.length && uids.length <= 1000){
      //有98个用户，除以10份，每份9个，最后再加一份8个，一共11份
      //有988个用户，除以10份，每份98个，最后再加一份8个，一共11份
      var index:Int = 1

      var partitionNum:Int = 0
      var jumpPosition:Int = 1

      for (elem <- uids) {
        if(jumpPosition <= uids.size/10*10) {
          if (index % (uids.size / 10 + 1) == 0) {
            index = 1
            partitionNum += 1
          }

          uidToIndex(elem) = partitionNum
          index += 1
        }else{
          uidToIndex(elem) = partitionNum + 1
        }

        jumpPosition += 1
      }
    } else if (uids.length > 1000 && uids.length <= 100000){

    } else if(uids.length >= 0 && uids.length < 10){

    } else{

    }

    for (elem <- uidToIndex) {
      println(elem._1 + "," + elem._2)
    }

    override def numPartitions: Int =
      if(10 < uids.length && uids.length <= 1000){
      if(uids.length % 10 == 0)
        10
      else{
        11
      }

    } else if (uids.length > 1000 && uids.length <= 100000){
      if(uids.length % 100 == 0)
        100
      else{
        101
      }
    } else if(uids.length >= 0 && uids.length < 10){
      uids.length
    } else{
      1
    }

    override def getPartition(key: Any): Int = {
      uidToIndex(key.asInstanceOf[(String,String)]._2)
    }
  }
}