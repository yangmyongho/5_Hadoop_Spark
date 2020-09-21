package com.spark.dataFrame_SQL

import org.apache.spark.sql.SparkSession  // DataFrame 생성

/*
 * text file -> DataFrame
 * 
 * - column 사용을 위한 표준내장함수 : 문자열 처리 함수
 */

object Step06_dataFrame_txt {
  
  def main(args: Array[String]) : Unit = {
    
    // SparkSession 객체 생성
    val spark = SparkSession.builder.master("local").appName("dataFrameAPI").getOrCreate()
    
    // 1. text file load -> DF
    val path = "C:\\ITWILL\\Work\\5_Hadoop_Spark\\workspace\\part2_Spark\\src\\main\\resources\\com\\spark\\test\\"
    val df = spark.read.text(path + "input.txt")
    //df.show()
    /*
     * +--------------------+
      |               value|
      +--------------------+
      |  programming is fun|
      |           very fun!|
      |    have a good time|
      |mouse is input de...|
      |keyboard is input...|
      |computer is input...|
      +--------------------+
     */
    df.show(false)  // 생략하지 않겠다.. 뭘? 아마 내용을..
    /*
     * +-------------------------------+
        |value                          |
        +-------------------------------+
        |programming is fun             |

     */
    
    // 2. column 사용을 위한 표준내장함수
    import org.apache.spark.sql.functions._
    // df.select(column 표준내장함수)
    
    df.select(col("value")).show()
    df.select(split(col("value"), " ").as("words")).show(false)
    /*
     * +-------------------------------------+
      |words                                |
      +-------------------------------------+
      |[programming, is, fun]               |
     */
    // col() -> split() -> explode() 토큰 단위로 자른 이 데이터들을 워드 단위로 잘라줌
    df.select(explode(split(col("value"), " ").as("words"))).show(false)
    /*
     * +-----------+
      |col        |
      +-----------+
      |programming|
      |is         |
      |fun        |
      |very       |
      |fun!       |
      |have       |
     */
    
    //sentence -> words
    val words = df.select(explode(split(col("value"), " ")).as("words"))
    println("단어 생성 결과")
    words.show(false)
    /*
     * +-----------+
      |words      |
      +-----------+
      |programming|
      |is         |
     */
    
    // word count
    val grp = words.groupBy("words")  // df.groupBy
    val wc = grp.count()  // 단어의 출현빈도수
    
    println(wc)
    wc.show()
    /*
     * +-----------+-----+
      |      words|count|
      +-----------+-----+
      |      input|    3|
      |   keyboard|    1|
      |     system|    1|
      |programming|    1|
      |     device|    2|
      |        fun|    1|
      |         is|    4|
      |       have|    1|
      |     output|    1|
      |      mouse|    1|
      |       very|    1|
      |   computer|    1|
      |       time|    1|
      |       good|    1|
      |          a|    1|
      |       fun!|    1|
      +-----------+-----+
     */
    
    println("성공~!!")
    spark.close()
  }
}