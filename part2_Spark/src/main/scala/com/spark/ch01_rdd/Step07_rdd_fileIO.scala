package com.spark.ch01_rdd

import org.apache.spark.{SparkConf, SparkContext}

object Step07_rdd_fileIO {
  
  def main(args: Array[String]) : Unit = {
    
    // SparkConf 객체 생성
    val conf = new SparkConf()
      .setAppName("SparkTest")
      .setMaster("local")
    
    // SpartContext 객체 생성
    val sc = new SparkContext(conf)
    
    val filePath = "C:/ITWILL/Work/5_Hadoop_Spark/workspace/part2_Spark/src/main/resources/com/spark/test/"
    //val filePath = "src/main/resources/"
    val rdd = sc.textFile(filePath + "input.txt")
    
    println(rdd.collect().mkString("\n"))
    
    val rdd2 = rdd.flatMap(_.split(" "))  // words 생성
    
    val rdd3 = rdd2.filter(_.size >= 3)
    
    val wc = rdd3.map( (_,1) ).reduceByKey(_+_)
    
    wc.saveAsTextFile(filePath + "output")
    
    println("success")
    sc.stop()
    
  }  // main end
}