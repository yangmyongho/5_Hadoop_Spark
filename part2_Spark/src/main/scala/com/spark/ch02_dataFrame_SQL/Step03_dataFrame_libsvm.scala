package com.spark.dataFrame_SQL

/*
 * libsvm -> DataFrame 생성 
 *  - libsvm : label(y), features(x)
 */

import org.apache.spark.sql.SparkSession // DataFrame 생성 

object Step03_dataFrame_libsvm {
  
    def main(args: Array[String]): Unit = {
    
     // SparkSession 객체 생성 
     val spark = SparkSession.builder
                 .master("local") 
                 .appName("dataFrameAPI")
                 .getOrCreate()
                     
     val df = spark.read.format("libsvm")
              .load("src/main/resources/iris_libsvm.txt")
     
     // 전체 칼럼 모두 보기          
     df.show()         
                 
     
     // linear regression model
     import org.apache.spark.ml.regression.LinearRegression
     
     val trainset = spark.read.format("libsvm")
              .load("src/main/resources/sample_libsvm_data.txt")
     trainset.show(false)
     
     // object 생성 
     val lr = new LinearRegression()
     
     // model 생성 
     val model = lr.fit(trainset)
     
     val trainSummary = model.summary
     
     // model 평가 
     println(s"r2 score = ${trainSummary.r2}")
     // r2 score = 0.9999998176861554
     
     // 객체 닫기 
     spark.close()
  }
}












