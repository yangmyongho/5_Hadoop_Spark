package com.spark.ch01_rdd

import org.apache.spark.{ SparkConf, SparkContext }

/*
 * RDD Action : load & save
 *  1. rdd.collect() : rdd 원소추출 -> vector 형식 생성
 *  2. rdd= sc.textFile(local file or HDFS) : 텍스트 파일 읽어서 rdd 생성
 *  3. rdd.saveAsTextFile(local file of HDFS) : rdd 객체를 텍스트 파일로 저장
 */


object Step05_rddAction {
  
  
  def main(args: Array[String]) : Unit = {
    
    val conf = new SparkConf()
          .setAppName("RDD_SET")
          .setMaster("local")
  
     val sc = new SparkContext(conf)
    
    // 1. load 관련 : rdd.collect(), first(), take()
    val rdd = sc.parallelize(1 to 100, 5)  // data : 정수 컬렉션, 파티션 수 :5
    
    println(rdd.collect.mkString(" "))  // 1 2 3 4 5 ...... 전체 원소
    println(rdd.first)  // 1
    println(rdd.take(10))  // [I@6e46d9f4
    println(rdd.take(10).mkString(" "))  // 1 2 3 4 5 6 7 8 9 10
    
    // 2. rdd= sc.textFile(local file or HDFS) : 텍스트 파일 읽어서 rdd 생성 
    val rdd2 = sc.textFile("file:/C:/hadoop-2.6.0/README.txt")
    println(rdd2.count())  // 줄 수 = RDD 원소 수  : 31
    println(rdd2.take(31).mkString("\n"))
        
    // 3. save 관련 : rdd.saveAsTextFile(local file of HDFS) : rdd 객체를 텍스트 파일로 저장
    //val rdd3 = rdd.saveAsTextFile("file:/C:/hadoop-2.6.0/output")  // 파티션 5개
    //val rdd4 = rdd2.saveAsTextFile("file:/C:/hadoop-2.6.0/output2")  // 파티션 1개
    
    // 4. HDFS file load & HDFS file save
    // rdd load
    val hdfs_rdd = sc.textFile("hdfs://localhost:9000/test/README.txt", 5)
    
    // rdd save
    hdfs_rdd.saveAsTextFile("hdfs://localhost:9000/output")  // output 디렉터리는 자동 생성됨
    
    println("success")
    sc.stop
    
  }  // main end
  
  }