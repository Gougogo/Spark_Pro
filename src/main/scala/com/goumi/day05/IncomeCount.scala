package com.goumi.day05
import com.alibaba.fastjson.{JSON, JSONException}
import com.goumi.utils.SparkUtils

import java.sql.{Connection, Date, DriverManager, PreparedStatement, ResultSet, SQLException, Statement}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.slf4j.LoggerFactory

import scala.collection.mutable.ListBuffer

object IncomeCount {
  private val logger = LoggerFactory.getLogger(this.getClass)

  val getMysqlConn = ()=>{
    //创建MySQL连接
    var conn: Connection = null
    conn = DriverManager.getConnection(
      "jdbc:mysql://localhost:3306/database1?characterEncoding=UTF-8",
      "root",
      "studymysql")
    conn
  }

  val getMysqlData = (sc: SparkContext)=>{
    //创建MySQL连接
    var conn: Connection = null
    var statement: Statement = null
    var strs: ListBuffer[(String, String)] = new ListBuffer[(String, String)]()

    conn = DriverManager.getConnection(
      "jdbc:mysql://localhost:3306/database1?characterEncoding=UTF-8",
      "root",
      "studymysql")
    statement = conn.createStatement()
    val resultSet: ResultSet = statement.executeQuery("select * from t_category")
    while(resultSet.next()){
      val t1: String = resultSet.getString(1)
      val t2: String = resultSet.getString(2)
      strs:+(t1, t2)
      println(t1+t2)
    }
    sc.parallelize(strs, 2)
  }

  val dataToMySQL = (it: Iterator[(String, String, Double)]) => {
    //创建MySQL连接
    var conn: Connection = null
    var statement: PreparedStatement = null
    try {
      conn = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/database1?characterEncoding=UTF-8",
        "root", "studymysql")
      statement = conn.prepareStatement("INSERT INTO t_result values (null, ?, ?, ?)")
      //将迭代器中的数据写入到MySQL，将数据攒起来之后再写

      var i = 1
      it.foreach(t => {
        statement.setString(1, t._1)
        statement.setDouble(2, t._3)
        statement.setDate(3, new Date(System.currentTimeMillis()))
        //执行
        //statement.executeUpdate()  //每次来一条就写入，比较耗费资源，所以应该批量写入
        //批量写入
        statement.addBatch()
        //但是攒多少条数据再写入合适呢，所以这里应该处理一下
        if(i % 3 == 0)
          statement.executeBatch()
        i=i+1
      })
      //这是将最后余数进行执行
      statement.executeBatch()
      ()
    } catch {
      case e: SQLException => {
        //单独处理有问题的数据
        //e.printStackTrace()
        throw new RuntimeException("数据库异常", e)
      }
    } finally {
      //释放MySQL的资源
      if (statement != null) {
        statement.close()
      }
      if (conn != null) {
        conn.close()
      }
    }
  }

  def main(args: Array[String]): Unit = {
    val sc: SparkContext = SparkUtils.createContext()
    val lines: RDD[String] = sc.textFile(args(0))
    //val lines: RDD[String] = sc.textFile("in/order.json")
    val beanRDD: RDD[OrderBean] = lines.map(e => {
      //json数据如果有异常，还需要过滤掉异常数据，那么就需要定义一个null bean，最后通过判断是否为null来过滤
      var bean: OrderBean = null

      try {
        //这是用bean对象接受
        bean = JSON.parseObject(e, classOf[OrderBean])
        //简单数据还可以直接用元组接受
      } catch {
        //这里通过模式匹配来处理异常
        //比如在运行JSON.parseObject(e, classOf[OrderBean])的时候遇到一个JSON异常就会走到第一个异常处
        case x: JSONException => {
          //错误数据还可以调用HBase的api然后单独写到HBase里，或者记一些log日志
          //还可以配置log4j来处理日志问题
          logger.error("parse json error解析错误:" + x)

          println("parse json error解析错误:" + x)
          /*
          *
          * {"cid": 1, "money": 600.0, "longitude":116.397128,"latitude":39.916527,"oid":"o123", }
            "oid":"o112", "cid": 3, "money": 200.0, "longitude":118.396128,"latitude":35.916527}
            * 第一句是算的因为有完整的括号，第二行的数据不会被解析
          * */
        }
      }

      //println(bean)
      bean
      //这里可以直接过滤吗，不可以，因为map操作是对每一条数据都进行操作，并且要求返回一个值。
    })

    val filtered: RDD[OrderBean] = beanRDD.filter(e=>(e != null && e.cid != null))

    //将bean进一步处理，取出需要用到的数据
    val dataRDD: RDD[(String, Double)] = filtered.map(e => {

      println(e.cid+" "+ e.money)
      (e.cid, e.money)
    })

    //需要的数据取出来之后，再进行聚合
    val reduced: RDD[(String, Double)] = dataRDD.reduceByKey(_ + _)

    //现在的数据是这样的(cid, money)---("cid": 3, "money": 300.0)，但可能还需要关联维度名称
    //可以通过读数据库，或者某个文件
    //每来一条查一次数据库的话，最好用MapPartition算子
    //查某个文件(1,图书)
    /*val cNameRDD: RDD[String] = sc.textFile(args(1))
    //val cNameRDD: RDD[String] = sc.textFile("in/category.log")
    //小表还可以用广播变量
    val cNameAndCid: RDD[(String, String)] = cNameRDD.map(e => {
      val strings: Array[String] = e.split(",")
      (strings(0), strings(1))
    })*/

    //从mysql中读取数据
    val cNameAndCid: RDD[(String, String)] = getMysqlData(sc)

    //对已有数据进行join
    val joined: RDD[(String, (Double, Option[String]))] = reduced.leftOuterJoin(cNameAndCid)

    //先将数据处理一下
    val res: RDD[(String, String, Double)] = joined.map(e => {
      (e._1, e._2._2.getOrElse("未知"), e._2._1)
    })

    //写数据的时候，处理好的数据可以写到数据库中，可以收集到driver端写，但应该并发在Executor端写数据
    res.foreachPartition(dataToMySQL)
  }

}
