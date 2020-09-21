package com.spark.exams

import org.apache.spark.sql.SparkSession

object exam03_dataFrameWordCount {
  def main(args: Array[String]): Unit = {
    
     // SparkSession 객체 생성 
     val spark = SparkSession.builder.master("local") .appName("dataFrameAPI").getOrCreate()
                 
      // 단계1 : HDFS의 /test 디렉터리에서 README.txt 파일 읽기(만약 file 없으면 file 업로드)
     val readme = spark.read.text("hdfs://localhost:9000/test/README.txt")
     readme.show(false)
            
      // 단계2 : 줄단위 읽기 -> 공백 기준 단어 분리 
     import org.apache.spark.sql.functions._
     //readme.select(split(col("value"), " ").as("words")).show(false)
     readme.select(explode(split(col("value"), " ")).as("words")).show(false)
     val words = readme.select(explode(split(col("value"), " ")).as("words"))
      
      // 단계3 : 워드 카운트 구하고, HDFS의 /output_wc 디렉터리에 저장하기
     val grp = words.groupBy("words")
     val wc = grp.count()
     
     //println(wc)  // [words: string, count: bigint]
     wc.show(false)
                 
     // 단계4 : HDFS의 /output_wc 디렉터리에 저장된 결과 파일 보기  
     // 여러 개의 파일로 분산되어 저장
     /*wc.write.format("csv").option("header", "true")
     .mode("overwrite").save("hdfs://localhost:9000/output_wc")   */
     // 하나의 파일로 묶어서 저장
     wc.coalesce(1).write.format("csv").option("header", "true")
     .mode("overwrite").save("hdfs://localhost:9000/output_wc")   
     
     
     
     // 객체 닫기 
     println("성공~!")
     spark.close()
     
  }
}