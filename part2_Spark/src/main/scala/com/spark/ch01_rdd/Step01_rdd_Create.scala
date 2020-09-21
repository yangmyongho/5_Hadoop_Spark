package com.spark.ch01_rdd

import org.apache.spark.{SparkConf, SparkContext}

object Step01_rdd_Create {
  
   
  def main(args: Array[String]) : Unit = {  
    
    // SparkContext object 생성
    val conf = new SparkConf()
      .setAppName("SparkTest")
      .setMaster("local")  // 스파크 환경 객체  >>conf.setAppName(~).setMaster(~)와 같은 것.
      

    // rdd data 생성(주로 외부의 파일을 가져와서 rdd 객체로 만드는 역할)
    val sc = new SparkContext(conf)  // 분산 파일읽기
    
    
    // 1. 파티션 지정 RDD 생성 : parallelize(data, 파티션수) 이용
    val rdd1 = sc.parallelize(1 to 100, 5)  // data=정수 컬렉션
    print(rdd1)  // ParallelCollectionRDD[0] at parallelize at Step01_rdd_Create.scala:21
    
    // object.bind()
    rdd1.foreach((x:Int) => print(x+" "))  // 무명함수
    
    // val rdd2 = sc.parallelize(seq, numSlices)
    val rdd2 = sc.parallelize(List("a", "b", "c","d","e"))  // data = List 컬렉션
    print(rdd2)
    
    rdd2.foreach((x:String) => print(x+" "))  // a b c d e 
    
    // 2. 외부 저장 매체(local file, HDFS) 데이터 이용 RDD 생성 : spark_home/README.md 
    val rdd3 = sc.textFile("file:/C:\\hadoop-2.6.0/NOTICE.txt")  // local file
    println(rdd3)  // file:/C:\hadoop-2.6.0/NOTICE.txt MapPartitionsRDD[3] at textFile at Step01_rdd_Create.scala:34
    
    // 텍스트 파일에서 1줄은 -> RDD 1개 원소.
    rdd3.foreach( (x:String) => println(x) )
    
    // 객체 닫기
    sc.stop
  }  // main end
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
}