package com.goumi.utils

import org.apache.spark.Partitioner

import scala.collection.mutable

class UidPartitioner(uids:Array[String]) extends Partitioner{
  private val uidToIndex = new mutable.HashMap[String, Int]()

  var index:Int = 0

  for (elem <- uids) {
    uidToIndex(elem) = index
    index+=1
  }
  override def numPartitions: Int = uids.length

  override def getPartition(key: Any): Int = {
    uidToIndex(key.asInstanceOf[String])
  }
}

class UidPartitioner02(uids:Array[String]) extends Partitioner{
  private val uidToIndex = new mutable.HashMap[String, Int]()

  var index:Int = 0

  for (elem <- uids) {
    uidToIndex(elem) = index
    index+=1
  }
  override def numPartitions: Int = uids.length

  override def getPartition(key: Any): Int = {
    uidToIndex(key.asInstanceOf[(Long, String)]._2)
  }
}

class UidPartitioner03(uids:Array[String]) extends Partitioner{
  private val uidToIndex = new mutable.HashMap[String, Int]()

  var index:Int = 0

  for (elem <- uids) {
    uidToIndex(elem) = index
    index+=1
  }
  override def numPartitions: Int = uids.length

  override def getPartition(key: Any): Int = {
    uidToIndex(key.asInstanceOf[(String, String, String)]._1)
  }
}