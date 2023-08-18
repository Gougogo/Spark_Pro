package com.goumi.sparksql
import org.apache.spark.sql.{DataFrame, Encoder, Encoders, Row, SparkSession}
import org.apache.spark.sql.functions.{col, udf}
import org.apache.spark.sql.expressions.{Aggregator, MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, DoubleType, StructField, StructType}

case class Sale(id: Int, product: String, amount: Double)

object AvgAgeWithUDAF extends App {
  val spark = SparkSession.builder()
    .appName("Scala UDAF Example")
    .master("local[*]")
    .getOrCreate()

  private val df: DataFrame = spark.read.json("in/user1.json")

  // 定义UDAF函数
  val calculateAverage = new UserDefinedAggregateFunction {
    // 定义输入数据类型
    def inputSchema: StructType = StructType(Seq(
      StructField("amount", DoubleType)
    ))

    // 定义中间缓冲区数据类型
    def bufferSchema: StructType = StructType(Seq(
      StructField("sum", DoubleType),
      StructField("count", DoubleType)
    ))

    // 定义输出数据类型
    def dataType: DataType = DoubleType

    // 定义是否是确定性的函数
    def deterministic: Boolean = true

    // 初始化中间缓冲区
    def initialize(buffer: MutableAggregationBuffer): Unit = {
      buffer(0) = 0.0 // sum
      buffer(1) = 0.0 // count
    }

    // 更新中间缓冲区
    def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
      if (!input.isNullAt(0)) {
        buffer(0) = buffer.getDouble(0) + input.getDouble(0)
        buffer(1) = buffer.getDouble(1) + 1.0
      }
    }

    // 合并两个中间缓冲区
    def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
      buffer1(0) = buffer1.getDouble(0) + buffer2.getDouble(0)
      buffer1(1) = buffer1.getDouble(1) + buffer2.getDouble(1)
    }

    // 计算最终结果
    def evaluate(buffer: Row): Any = {
      buffer.getDouble(0) / buffer.getDouble(1)
    }
  }

  // 注册UDAF函数
  spark.udf.register("calculate_average", calculateAverage)

  // 应用UDAF函数
  df.createTempView("user")
  spark.sql("select calculate_average(age) from user").show()
}

class MyAvg2 extends Aggregator[Double, (Double, Int), Double] {

  //每个分区初始值
  override def zero: (Double, Int) = {
    (0.0, 0)
  }

  //每个分区，同一组输入数据如何运算
  override def reduce(b: (Double, Int), a: Double): (Double, Int) = {
    (b._1 + a, b._2 + 1)
  }

  //全局计算聚合的运算逻辑
  override def merge(b1: (Double, Int), b2: (Double, Int)): (Double, Int) = {
    (b1._1 + b2._1, b1._2 + b2._2)
  }

  //计算最终的结果
  override def finish(reduction: (Double, Int)): Double = {
    reduction._1 / reduction._2
  }

  override def bufferEncoder: Encoder[(Double, Int)] = {
    Encoders.tuple(Encoders.scalaDouble, Encoders.scalaInt)
  }

  override def outputEncoder: Encoder[Double] = {
    Encoders.scalaDouble
  }
}

