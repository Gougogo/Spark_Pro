package com.goumi.day06

import com.goumi.utils.{IpUtils, SparkUtils}
import org.apache.spark.SparkContext
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD

object IpLocationDemo {
  def main(args: Array[String]): Unit = {
    val sc: SparkContext = SparkUtils.createContext(true)

    //ip地址起始对应的十进制，尾巴对应的十进制，省市
    //这些数据可以直接用元组保存起来，也可以定义一个Bean对象存起来
    val ipRDD: RDD[String] = sc.textFile("in/ip.txt")
    val ipRulesRDD: RDD[(Long, Long, String, String)] = ipRDD.map(e => {
      val fields: Array[String] = e.split("[|]")
      val startNum: Long = fields(2).toLong
      val endNum: Long = fields(3).toLong
      val province: String = fields(6)
      val city: String = fields(7)
      (startNum, endNum, province, city)
    })

    val ipRulesInDriver: Array[(Long, Long, String, String)] = ipRulesRDD.collect()
    //然后将ip规则广播到Executor上,这里要注意广播的时候，要使用同步，也就是广播完之后，才能执行任务
    //而broadcast本身就是同步方法,返回的是一个Broadcast的引用，表示将数据广播到了哪些节点上去了
    //引用是在driver端，有了这个引用，task就能知道到哪里去找广播变量的数据
    //所以现在就需要将Driver端的数据伴随着Task发送到Executor端
    //这个需求就跟之前数据库链接相似，都需要通过闭包将Driver端的数据发送到Executor端
    //函数内部引用了函数外部的变量，但函数内部是在Executor中执行的
    //所有有很多算子里面都有clean这个方法，这个方法就是用来检测函数里面是不是引用到了外部的变量
    //如果该变量能序列化，没问题，不能序列化就会报task 不能被序列化
    val broadcastRef: Broadcast[Array[(Long, Long, String, String)]] = sc.broadcast(ipRulesInDriver)

    //然后就可以通过这个返回的引用来读取广播变量并跟日志数据进行关联
    val accessLog: RDD[String] = sc.textFile("in/ipaccess.log")
    val provinceAndOne: RDD[(String, Int)] = accessLog.map(e => {
      //map里面这个方法最后会发送到executor中执行，也就是闭包
      val fields: Array[String] = e.split("[|]")
      val ip: String = fields(1)
      //将ip地址转为十进制
      val ipNum: Long = IpUtils.ip2Long(ip)
      val ipRulesInExecutor: Array[(Long, Long, String, String)] = broadcastRef.value
      var province = "未知"
      val index: Int = IpUtils.binarySearch(ipRulesInExecutor, ipNum)
      if (index > 0) {
        province = ipRulesInExecutor(index)._3
      }else{
        println("xxxxx")
      }

      (province, 1)
    })

    val reduced = provinceAndOne.reduceByKey(_ + _)

    println(reduced.collect().toBuffer)
  }
}
