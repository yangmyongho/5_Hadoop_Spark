package com.spark.exams

import org.apache.spark.{SparkConf, SparkContext}
import breeze.linalg.split

object exam01_rddWordCount_result {
  
  

  def main(args: Array[String]) = {
      val conf = new SparkConf()
        .setAppName("WordCount2")
        .setMaster("local")
      val sc = new SparkContext(conf)
      
      // 단계1 : HDFS의 /test 디렉터리에서 NOTICE.txt 파일 읽기(만약 file 없으면 file 업로드)
      val rdd = sc.textFile("hdfs://localhost:9000/test/README.txt")
      println(rdd)
                
            
      // 단계2 : 줄단위 읽기 -> 공백 기준 단어 분리 
      val rdd_split = rdd.flatMap(_.split(" "))  // 1:N 매핑
      println(rdd_split)
       
      
      // 단계3 : 단어 길이 3자 이상 단어 필터링  
      val rdd_filter = rdd_split.filter(_.size >= 3)
      println("필터시작")
      println(rdd_filter)
      println("필터끝")
      rdd_filter.foreach( (x : String) => println(x) )
      
      // 단계4 : 워드 카운트 구하고, HDFS의 /output 디렉터리에 저장하기
      val word_count = rdd_filter.map( (_,1) ).reduceByKey(_+_)
      
      
      // 단계5 : HDFS의 /output 디렉터리에 저장된 워드 카운트 결과 파일 보기
      word_count.saveAsTextFile("hdfs://localhost:9000/output")
      
      
      /*
       * HDFS에 파일 업로드 명령어 
       * > hdfs dfs -put  업로드파일명  /업로드디렉터리명  
       * 
       * HDFS의 파일 보기 명령어 
       * > hdfs dfs -cat /디렉터리명/파일명
       * 
       * HDFS 디렉터리 삭제 명령어 
       * > hdfs dfs -rm -R /디렉터리명 
       */
             
      print("execute success!!")  
      //Stop the Spark context      
      sc.stop
      
   }  
}