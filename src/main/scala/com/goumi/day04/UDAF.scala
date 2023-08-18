package com.goumi.day04

import org.apache.spark.SparkConf
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, DoubleType, LongType, StructType}

object UDAF {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkSQL01_Demo")
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()

    import spark.implicits._

    //自定义聚合函数
    //创建聚合函数对象
    val udaf = new MyAgeAvgFunction
    spark.udf.register("avgAge", udaf)
    val frame = spark.read.json("in/user.json")
    frame.createOrReplaceTempView("user")

    spark.sql("select avgAge(age) from user").show()

    spark.stop()
  }

}

class MyAgeAvgFunction extends UserDefinedAggregateFunction{
  //函数输入的数据结构
  override def inputSchema: StructType = {
    new StructType().add("age", LongType)
  }

  //计算时的数据结构
  override def bufferSchema: StructType = {
    new StructType().add("sum", LongType).add("count", LongType)
  }

  //函数返回的数据结构，sum/count
  override def dataType: DataType = DoubleType

  //函数是否稳定
  override def deterministic: Boolean = true

  //当前函数计算时，缓冲区的初始化，buffer其实是一个数组，不考虑类型，只考虑结构，结构就有顺序。那么就没办法通过名字将
  //初始变量拿到，只能通过数组位置拿
  override def initialize(buffer: MutableAggregationBuffer): Unit = {
    buffer(0) = 0L
    buffer(1) = 0L
  }

  //根据查询结果更新缓冲区数据，这里的input就是前面的inputSchema
  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    buffer(0) = buffer.getLong(0) + input.getLong(0)
    buffer(1) = buffer.getLong(1) + 1
  }

  //将多个节点的缓冲区合并，因为每一个节点在运行的时候都有其缓冲区，也就是Sum和Count
  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    buffer1(0) = buffer1.getLong(0) + buffer2.getLong(0)

    buffer1(1) = buffer1.getLong(1) + buffer2.getLong(1)
  }

  //将最终的结果计算出来
  override def evaluate(buffer: Row): Any = {
    buffer.getLong(0).toDouble / buffer.getLong(1)
  }
}
