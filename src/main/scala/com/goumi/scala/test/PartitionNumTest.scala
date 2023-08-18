package com.goumi.scala.test

import scala.collection.mutable

object PartitionNumTest {
  def main(args: Array[String]): Unit = {
    val uidToIndex = new mutable.HashMap[Int, Int]()
    val uids: Array[Int] = new Array[Int](58)
    for (i <- 0 to 57) {
      uids(i) = i + 1
    }

    var index: Int = 1
    var partitionNum: Int = 0

    for (elem <- uids) {
      if (index <= uids.size / 10 * 10) {
        if (index % (uids.size / 10 + 1) == 0) {
          index = 1
          partitionNum += 1
        }

        uidToIndex(elem) = partitionNum
        index += 1
      }
      else {
        uidToIndex(elem) = partitionNum + 1
      }
    }

    for (elem <- uidToIndex.toList.sorted) {
      println(elem._1 + "," + elem._2)
    }
  }
}
