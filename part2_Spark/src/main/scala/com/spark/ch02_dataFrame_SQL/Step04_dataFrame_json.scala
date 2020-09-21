package com.spark.dataFrame_SQL

import org.apache.spark.sql.SparkSession

object Step04_dataFrame_json {
  def main(args: Array[String]) : Unit = {
    
    //SparkSession 객체 생성
    val spark = SparkSession.builder.master("local").appName("dataFrameAPI").getOrCreate()
    
    //path 변수
    val path = "C:\\ITWILL\\Work\\5_Hadoop_Spark\\workspace\\part2_Spark\\src\\main\\resources\\com\\spark\\test\\usagov_bitly.txt"
    
    //json -> DataFrame
    val df = spark.read.json(path)
    df.show()
    
    df.printSchema()
    
    // 가상 테이블 만들기
    df.createOrReplaceTempView("json_df")
    
    //sql문 작성하기
    spark.sql("select * from json_df").show()
    spark.sql("select count(*) from json_df").show()  // 3560
    
    // subset 생성하기
    val nk_true = spark.sql("select * from json_df where nk = 1")
    nk_true.show()
    println("전체 관측치 : " + nk_true.count())
    
    // 객체 닫기
    println("성공!")
    spark.close()
  }
}