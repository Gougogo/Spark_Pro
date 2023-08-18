package com.goumi.day12

import com.goumi.utils.{DateUtils, SparkUtils}
import org.apache.spark.{Partition, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.util.LongAccumulator

import java.text.ParseException

object FlowCount1 {
  /*
  uid,start,end,flow
  1,2020-02-18 14:20:30,   2020-02-18 14:46:30,  2020-02-18 14:20:30    ,20     ,0
  1,2020-02-18 14:47:20,   2020-02-18 15:20:30,  2020-02-18 14:46:30    ,30     ,0
  1,2020-02-18 15:37:23,   2020-02-18 16:05:26,  2020-02-18 15:20:30    ,40     ,1
  1,2020-02-18 16:06:27,   2020-02-18 17:20:49,  2020-02-18 16:05:26    ,50     ,0
  1,2020-02-18 17:21:50,   2020-02-18 18:03:27,  2020-02-18 17:20:49    ,60.0   ,0

  2,2020-02-18 14:18:24,2020-02-18 15:01:40,20
  2,2020-02-18 15:20:49,2020-02-18 15:30:24,30
  2,2020-02-18 16:01:23,2020-02-18 16:40:32,40
  2,2020-02-18 16:44:56,2020-02-18 17:40:52,50
  3,2020-02-18 14:39:58,2020-02-18 15:35:53,20
  3,2020-02-18 15:36:39,2020-02-18 15:24:54,30

  tmp = 0

  0     0
  0     0
  1     1
  0     1
  0     1
  */

  val sc: SparkContext = SparkUtils.createContext()

  //首先对于原始数据，应该有个数据校验
  //只有处理的目标数据格式正确之后，才能进一步处理
  def checkLines(lines: RDD[String]) ={

    println("lines's partitions = " + lines.partitions.length)
    //println(lines.collect().toBuffer)

    val linesNum: LongAccumulator = sc.longAccumulator("linesNum")

    val linesOk: RDD[Any] = lines.map(e => {
      println("enter")
      if (!e.matches(".*?(,).*?(,).*?(,).*?"))
      {
        println("false")
        linesNum.add(1)
        println("println1="+linesNum)
        linesNum
      }
      else {
        val dateObj = new DateUtils("yyyy-MM-dd HH:mm:ss")

        val start: String = e.split(",")(1)
        val end: String = e.split(",")(2)
        var startNum: Long = 0
        var endNum: Long = 0

        try {
          startNum = dateObj.parseDateToNums(start)
        } catch {
          case e:ParseException => {
            startNum = 0
          }

        }

        try {
          endNum = dateObj.parseDateToNums(end)
        } catch {
          case  e:ParseException => {
            endNum = 0
          }
        }

        linesNum.add(1)
        println("println2="+linesNum)

        if (startNum < endNum && startNum != 0 && endNum != 0) {
          println("true")
          -1
        } else{
          println("parse false")
          linesNum.value
        }
      }
    })

    println(linesOk.collect().toBuffer)

    var isOk:Boolean = true
    linesOk.map(e=>{
      if (e == -1){
        isOk = false
      }
    })

    println(linesNum.value)
    isOk
  }
  def main(args: Array[String]): Unit = {
    val lines: RDD[String] = sc.textFile("in/dataWithHeader.csv")
    if(!checkLines(lines))
      println("error lines")

    //Thread.sleep(1000000000)

  }

}
