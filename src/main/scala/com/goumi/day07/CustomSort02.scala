package com.goumi.day07

import com.goumi.utils.SparkUtils
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object CustomSort02 {
  def main(args: Array[String]): Unit = {
    //先按照颜值的从高到底排序，如果颜值相等，再按照年龄的升序排序
    val sc: SparkContext = SparkUtils.createContext(true)

    //创建
    val lst = List("老段,38,99.99", "念行,30,99.99", "老赵,36,9999.99")
    //注意元组的比较方式是依次比较，所以想要先比较颜值，就需要将颜值放到第一位
    val data: RDD[String] = sc.parallelize(lst)
    val tpRDD: RDD[Boy] = data.map(e => {
      val fields: Array[String] = e.split(",")
      Boy(fields(0), fields(1).toInt, fields(2).toDouble)
    })

    //如果直接放Boy类型的数据进来，就会报没有找到排序规则，也就是说，Boy这个类型的数据没有定义排序规则
    //因为SortBy是一个科里化方法也就是function()()这种，就是说要传2次参数
    //第二个参数要求传一个Ordering[Boy]类型的隐式参数
    //本来应该是这样传参：tpRDD.sortBy(t=>t)(Ordering[Boy], ctag)
    //还有一种方式是Boy这个类型，将排序规则定义清楚，可以实现Comparable接口
    //还有个问题是为什么Boy实现了比较规则后，就可以直接传到这里了，里面的运行逻辑是什么
    println(tpRDD.sortBy(t => t).collect().toBuffer)

    //但有时候数据太多了，不可能都用元组来装，跟之前的案例一样，就可以用Bean来装
  }
}

//这里有个问题是为什么直接实现Comparable接口会报错？
//而是要这么处理：extends Ordered[OrderingBean] with Serializable
//当然序列化是必须的，因为要在不同的机器中进行网络传输对象，就需要进行序列化
//封装数据最好定义成case class，而不是直接定义一个类
case class Boy(val name: String, val age: Int, val fv:Double) extends Ordered[Boy] {
  override def toString = s"Boy($name, $age, $fv)"

  override def compare(that: Boy): Int = {
    if(this.fv == that.fv){
      this.age - that.age
    }else{
      //之所以这样处理是因为双精度做减法有误差
      -java.lang.Double.compare(this.fv, that.fv)
    }
  }
}
