package com.goumi.utils

import org.apache.spark.{SparkConf, SparkContext}

object SparkUtils {

  def createContext(isLocal: Boolean = true):SparkContext ={
    val conf: SparkConf = new SparkConf()
      .setAppName(this.getClass.getSimpleName)
      .set("spark.shuffle.file.buffer", "64")
      .set("spark.reducer.maxSizeInFlight", "96")
      .set("spark.shuffle.io.maxRetries", "6")
    if(isLocal){
      conf.setMaster("local[*]")
    }

    val sc = new SparkContext(conf)
    sc
  }

}
