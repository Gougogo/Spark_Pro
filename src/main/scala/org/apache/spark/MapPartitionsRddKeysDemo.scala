package org.apache.spark

import org.apache.spark.rdd.{MapPartitionsRDD, RDD}
import org.apache.spark.{Partition, SparkConf, SparkContext}

object Spark01_RDD_Map {
  def main(args: Array[String]): Unit = {
    val config: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCount")
    val sc = new SparkContext(config)
    val arr: Array[(String, Int)] = Array(("spark", 1), ("hadoop", 2), ("spark", 1), ("hadoop", 2))

    val rdd2: RDD[(String, Int)] = sc.makeRDD(arr, 2)

    rdd2.keys

    val func2 = (e:Int) => e*10

    rdd2.map(t=>{
      (t._1, t._2*10)
    })

    rdd2.map{
      case (t, num) => (t, num*10)
    }

    rdd2.mapValues(_ * 10)

    new MapPartitionsRDD[(String, Int), (String, Int)](rdd2,
      (context, pid, iter) => iter.map { case (k, v) => (k, func2(v)) },
      preservesPartitioning = true)


  }
}
