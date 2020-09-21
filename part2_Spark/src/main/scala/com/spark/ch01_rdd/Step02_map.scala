package com.spark.ch01_rdd

import org.apache.spark.{SparkConf, SparkContext}

/*
 * RDD Transformation : map 관련 메서드
 *  1. rdd.map()
 *  2. rdd.flatMap()
 *  3. rdd.filter()
 */

object Step02_map {
    
   
  def main(args: Array[String]) : Unit = {  
    
    // SparkContext object 생성
    val conf = new SparkConf()
      .setAppName("SparkTest")
      .setMaster("local")  // 스파크 환경 객체  >>conf.setAppName(~).setMaster(~)와 같은 것.
      

    // rdd data 생성(주로 외부의 파일을 가져와서 rdd 객체로 만드는 역할)
    val sc = new SparkContext(conf)  // 분산 파일읽기
    
    // 1. rdd.map(매핑연산자) : rdd 원소를 순서대로 받아서 연산 수행( 1:1 )
    val rdd= sc.parallelize(List("a", "b", "c"))
    val map_re = rdd.map((_,1))  // (a, 1), (b, 1), (c, 1)
    map_re.foreach(println)
    
    val rdd2 = sc.parallelize(1 to 10)
    val map_re2 = rdd2.map(_+1)
    //rdd 원소 추출 -> 구분자(,) 원소 -> 출력
    println(map_re2.collect())  // [I@7f69d591
    println(map_re2.collect().mkString(","))  // 2,3,4,5,6,7,8,9,10,11
    
    // 2. rdd.flatmap(매핑연산자) : rdd 원소를 순서대로 받아서 연산 수행( 1:N )
    val names = sc.parallelize(List("홍길동, 강호동", "이순신, 강감찬, 유관순", "홍길동, 이순신, 강감찬"))
    val flatmap_re = names.flatMap(_.split(","))
    println("size : " + flatmap_re.count())  // size : 8
    println(flatmap_re.collect().mkString(","))  // 홍길동, 강호동,이순신, 강감찬, 유관순,홍길동, 이순신, 강감찬
    
    // 3. filter(조건식) : rdd 원소를 순서대로 받아서 조건이 참인 원소만 반환
    val filter_re = names.filter(_.size > 9)
    println(filter_re.collect().mkString("\t"))  // 이순신, 강감찬, 유관순	홍길동, 이순신, 강감찬
    
  }  // main end
  
}