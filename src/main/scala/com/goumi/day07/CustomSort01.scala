package com.goumi.day07

import com.goumi.utils.SparkUtils
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object CustomSort01 {
  def main(args: Array[String]): Unit = {
    //先按照颜值的从高到底排序，如果颜值相等，再按照年龄的升序排序
    val sc: SparkContext = SparkUtils.createContext(true)


    //创建
    val lst = List("老段,38,99.99", "念行,30,99.99", "老赵,36,9999.99")
    //注意元组的比较方式是依次比较，这是Ordering这个类实现tuple的比较规则
    val data: RDD[String] = sc.parallelize(lst)
    val tpRDD: RDD[(String, Int, Double)] = data.map(e => {
      val fields: Array[String] = e.split(",")
      (fields(0), fields(1).toInt, fields(2).toDouble)
    })

    //SortBy里面需要传一个函数，这个函数表示要排序的Key，也就是按照什么进行排序，以及是升序还是降序
    //并且返回值类型需要和原RDD中Key的类型一致，因为spark排序就是按照Key来排的
    //负号表示降序排序
    //所以像这种先比较第一个，再比较第二个，就可以用元组来处理

    println(tpRDD.sortBy(t => (-t._3, t._2)).collect().toBuffer)
  }
}
