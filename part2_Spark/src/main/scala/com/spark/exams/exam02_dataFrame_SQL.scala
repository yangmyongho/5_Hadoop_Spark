package com.spark.exams

import org.apache.spark.sql.SparkSession

object exam02_dataFrame_SQL {
  
  /*
   * 문) score_iq.csv 파일을 읽어서 다음과 같이 DataFrame을  생성하시오.
   * 
   *    단계1 : spark DataFrame 객체 생성(file 위치 : "src/main/resources")
   *    
   *    단계2 : academy 변수를 대상으로 그룹화 하고, 집단별 score 평균 구하기 
   *    
   *    단계 3 : DataFrame을 대상으로 가상 테이블 만들기(테이블명 : score_df)
   *    
   *    단계 4 : tv 칼럼이 2이상인 관측치를 대상으로 subset 만들기 
   *    
   *    단계 5 : subset을 csv 형식으로 저장하기 (file 위치 : "src/main/resources/output_df") 
   */
  
    def main(args: Array[String]): Unit = {
    
     // SparkSession 객체 생성 
     val spark = SparkSession.builder.master("local") .appName("dataFrameAPI").getOrCreate()
     
     // 단계1 : spark DataFrame 객체 생성(file 위치 : "src/main/resources")
     val path = "C:\\ITWILL\\Work\\5_Hadoop_Spark\\workspace\\part2_Spark\\src\\main\\resources\\com\\spark\\test\\"
     val score = spark.read.format("csv").option("header", "true")
     .option("inferSchema", "true").load(path + "score_iq.csv")
     
     score.show(false)
     
     // 단계2 : academy 변수를 대상으로 그룹화 하고, 집단별 score 평균 구하기 
     val grp = score.groupBy("academy")
     grp.mean("score").show()
     /*
      * +-------+-----------------+
        |academy|       avg(score)|
        +-------+-----------------+
        |      1|73.75555555555556|
        |      3|81.35714285714286|
        |      4|             87.0|
        |      2|             81.1|
        |      0|             65.0|
        +-------+-----------------+
      */
     
     // 단계 3 : DataFrame을 대상으로 가상 테이블 만들기(테이블명 : score_df)
     score.createOrReplaceTempView("score_df")
     
     // 단계 4 : tv 칼럼이 2이상인 관측치를 대상으로 subset 만들기 
     val score_tv2 = spark.sql("select * from score_df where tv >= 2")
     score_tv2.show()
     
     // 단계 5 : subset을 csv 형식으로 저장하기 (file 위치 : "src/main/resources/output_df")
     score_tv2.write.format("csv").option("header", "true").mode("overwrite")
     .save(path + "output_df")
     
     // 객체 닫기 
     println("성공~!!")
     spark.close()
     
  }
  
}