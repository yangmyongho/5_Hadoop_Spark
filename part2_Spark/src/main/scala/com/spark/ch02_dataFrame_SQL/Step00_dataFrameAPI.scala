package com.spark.dataFrame_SQL

import org.apache.spark.sql.SparkSession  // DataFrame 생성

object Step00_dataFrameAPI {
  
  def main(args: Array[String]) : Unit = {
    
    // SparkSession 객체 생성
    val spark = SparkSession.builder.master("local").appName("dataFrameAPI").getOrCreate()
    
    spark.close()
  }
  
}