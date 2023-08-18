package org.apache.spark

import org.apache.spark.rdd.{MapPartitionsRDD, RDD}
import org.apache.spark.{Partition, SparkConf, SparkContext}

object MapPartitionsWithIndexDemo12 {
  def main(args: Array[String]): Unit = {
    val config: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCount")
    val sc = new SparkContext(config)
    val listRDD: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4), 4)

    val rdd2 = new MapPartitionsRDD[String, Int](listRDD, (tc, index, iter) => {
      val i: Int = tc.partitionId()
      iter.map(
        e => s"partition:$index, value:$e"
        //e => e.toString
      )
    })
    println(rdd2.collect().toBuffer)
  }
}
