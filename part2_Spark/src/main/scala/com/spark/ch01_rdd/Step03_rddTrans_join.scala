package com.spark.ch01_rdd

import org.apache.spark.{SparkConf, SparkContext}

/*
 * RDD Transformation : join 관련 메서드
 */

object Step03_rddTrans_join {
  
  def zip(sc : SparkContext) : Unit = {
    val rdd1 = sc.parallelize(Seq("a", "b", "c")) // main함수와 다른 블록에 있으므로 main 내에서 이미사용된 rdd1이라는 이름으로 객체생성 가능
    val rdd2 = sc.parallelize(List(1,2,3))
    val zip_re = rdd1.zip(rdd2)
    zip_re.foreach(println)
  }
  
  def reduceByKey(sc : SparkContext) : Unit ={
    val lst = List("data", "text", "word", "data", "word", "data")  // 컬렉션 data
    val rdd = sc.parallelize(lst)  //RDD 생성
    
    // Transformation : old rdd -> new rdd
    val new_rdd = rdd.map( (_, 1) )  // ("data", 1)... ("word", 1)
    println(new_rdd.collect.mkString(","))
    // (data,1),(text,1),(word,1),(data,1),(word,1),(data,1)
    
    // Transformation : old rdd -> new rdd
    val reduce_rdd = new_rdd.reduceByKey(_+_).foreach(println)  // _과 _둘다 value값. 키를 기준으로 벨류값들을 더한다는 뜻
    /*
     * (text,1) 
     * (data,3)
     * (word,2)
     */
  }
 
  
  def main(args: Array[String]) : Unit = {  
    
    // SparkContext object 생성
    val conf = new SparkConf()
      .setAppName("SparkTest")
      .setMaster("local")  // 스파크 환경 객체  >>conf.setAppName(~).setMaster(~)와 같은 것.
    
    // rdd data 생성(주로 외부의 파일을 가져와서 rdd 객체로 만드는 역할)
    val sc = new SparkContext(conf)  // 분산 파일읽기
    
    // 1. join : 동일한 키를 기준으로 원소 묶음 : 길이는 서로 다름
    val rdd1 = sc.parallelize(Seq("kim", "lee", "park", "choi")).map((_, 1))  // ("kim", 1)
    println(rdd1.collect().mkString(" "))
    val rdd2 = sc.parallelize(List("lee", "choi")).map((_,2))  // ("lee", 2)
    println(rdd2.collect().mkString(" "))
    
    val join_re = rdd1.join(rdd2)  // 동일키 기준 join
    println(join_re.collect.mkString(","))  // (lee,(1,2)),(choi,(1,2))
    
    // 2. zip : 원소의 순서대로 원소 묶음 : 길이는 동일함
    zip(sc)
    /*
     * (a,1)
     * (b,2)
     * (c,3)
     */
    
    // 3. reduceByKey : 동일한 키를 기준으로 value를 합친다(?)
    reduceByKey(sc)
    
    
  }  // main end  
  
}