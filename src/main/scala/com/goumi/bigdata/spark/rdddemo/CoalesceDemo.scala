package com.goumi.bigdata.spark.rdddemo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/*
  def coalesce(numPartitions: Int, shuffle: Boolean = false,
               partitionCoalescer: Option[PartitionCoalescer] = Option.empty)
              (implicit ord: Ordering[T] = null)
      : RDD[T] = withScope {
    require(numPartitions > 0, s"Number of partitions ($numPartitions) must be positive.")
    if (shuffle) {
      val distributePartition = (index: Int, items: Iterator[T]) => {
        var position = (new Random(index)).nextInt(numPartitions)
        items.map { t =>
          // Note that the hash code of the key will just be the key itself. The HashPartitioner
          // will mod it with the number of total partitions.
          position = position + 1
          (position, t)
        }
      } : Iterator[(Int, T)]

      new CoalescedRDD(
        new ShuffledRDD[Int, T, T](mapPartitionsWithIndex(distributePartition),
        new HashPartitioner(numPartitions)),
        numPartitions,
        partitionCoalescer).values
    } else {
      new CoalescedRDD(this, numPartitions, partitionCoalescer)
    }
  }
 */
object CoalesceDemo {
  def main(args: Array[String]): Unit = {
    val config1: SparkConf = new SparkConf().setMaster("local[*]").setAppName("DistinctDemo")
    val sc = new SparkContext(config1)

    val listRDD: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 4)
    listRDD.sum()
    /*println(listRDD.partitions.length)
    val rdd1: RDD[String] = listRDD.mapPartitionsWithIndex((index, iter) => {
      iter.map(e => s"index:$index, value:$e")
    })
    listRDD.foreach(println)
    //rdd1.sortBy(_.split(",")(0)).foreach(println)

    val rdd2: RDD[Int] = listRDD.coalesce(1, false)
    println(rdd2.partitions.length)
    val rdd3: RDD[String] = rdd2.mapPartitionsWithIndex((index, iter) => {
      iter.map(e => s"index:$index, value:$e")
    })
    rdd2.foreach(println)*/

    listRDD.coalesce(5, true).collect()


    Thread.sleep(1000000)
  }
}
