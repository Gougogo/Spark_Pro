package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.{RDD, ShuffledRDD}
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

object NoShuffleDemo {
  def main(args: Array[String]): Unit = {
    val config1: SparkConf = new SparkConf().setMaster("local[*]").setAppName("DistinctDemo")
    val sc = new SparkContext(config1)

    val tuples = List(("cat", 2), ("cat", 2), ("ca", 2), ("ca", 2), ("mouse", 2))

    val pairRDD: RDD[(String, Int)] = sc.parallelize(tuples, 2)

    val rdd2: RDD[(String, Int)] = pairRDD.partitionBy(new HashPartitioner(pairRDD.partitions.length))

    //pairRDD.saveAsTextFile("out/out1")

    val rdd3 = new ShuffledRDD[String, Int, Null](pairRDD, new HashPartitioner(pairRDD.partitions.length))

    val rdd4: RDD[(String, Int)] = rdd2.partitionBy(new HashPartitioner(rdd2.partitions.length))

    val rdd5: RDD[(String, Int)] = rdd2.reduceByKey(new HashPartitioner(3), _ + _)

    //rdd3.saveAsTextFile("out/out3")
    //rdd2.saveAsTextFile("out/out2")
    //rdd5.saveAsTextFile("out/out5")

    Thread.sleep(10000)


  }

}
