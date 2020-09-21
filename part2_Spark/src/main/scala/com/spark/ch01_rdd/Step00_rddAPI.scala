package com.spark.ch01_rdd

import org.apache.spark.{SparkConf, SparkContext}

object Step00_rddAPI {
 
  def main(args: Array[String]) : Unit = {  
    
    // 1. SparkContext object 생성
    val conf = new SparkConf()
      .setAppName("SparkTest")
      .setMaster("local")  // 스파크 환경 객체  >>conf.setAppName(~).setMaster(~)와 같은 것.
      
      /*
       * 방법1) setMaster("local")
       *  -> 현재 Master의 단일머신에서 작업(클러스터 사용x)
       * 
       * 방법2) setMaster("yarn-cluster")
       *  -> 클러스터 환경에서 작업하겠다.
       */
    
    // rdd data 생성(주로 외부의 파일을 가져와서 rdd 객체로 만드는 역할)
    val sc = new SparkContext(conf)  // 분산 파일읽기
    
  }  // main end
}  // class end