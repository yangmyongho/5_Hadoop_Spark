package com.spark.dataFrame_SQL

import org.apache.spark.sql.SparkSession

object Step02_dataFrame_SQL {
  
  def main(args: Array[String]) : Unit = {
    
    
    // SparkSession 객체 생성
    val spark = SparkSession.builder
                    .master("local")
                    .appName("dataFrameAPI")
                    .getOrCreate()
    
    val df = spark.read
              .format("csv")
              .option("header", "true")
              .option("delimiter", ",")
              .option("inferSchema", "true")
              .load("C:\\ITWILL\\Work\\5_Hadoop_Spark\\workspace\\part2_Spark\\src\\main\\resources\\com\\spark\\test\\iris.csv")
    df.show()//df 내용보기
    println("관측치 수 = " + df.count())  //rows   : 관측치 수 = 150
    df.show(150)  // 전체 행 보기
    
    // 칼럼 type
    df.printSchema()
    /*
     * root
 |-- Sepal.Length: double (nullable = true)
 |-- Sepal.Width: double (nullable = true)
 |-- Petal.Length: double (nullable = true)
 |-- Petal.Width: double (nullable = true)
 |-- Species: string (nullable = true)
     */
    
    // 컬럼 단위 통계구하기
    //df.descrebe("Sepal.Length")  // error - 이름에 점 있음
    
    // 칼럼명 변경하기 : col1 ~ col4, Sepal.Length
    val colNames = Seq("Sepal_Length", "Sepal_Width", "Petal_Length", "Petal_Width", "Species")
    // old DF -> new DF
    val iris_df = df.toDF(colNames : _*)
    iris_df.show()  // |Sepal_Length|Sepal_Width|Petal_Length|Petal_Width|Species|
    
    // 가상 테이블(view) 만들기
    iris_df.createOrReplaceTempView("iris")  // 가상 테이블 : iris
    
    // SQL문  사용하기
    spark.sql("select Sepal_Length, Petal_Length from iris").show()
    spark.sql("select * from iris where Sepal_Length > 6.0 order by Sepal_Length desc").show()
    spark.sql("select Species, mean(Sepal_Length),mean(Petal_Length) from iris group by Species").show()
    /*
     * +----------+-----------------+------------------+
|   Species|avg(Sepal_Length)| avg(Petal_Length)|
+----------+-----------------+------------------+
| virginica|6.587999999999998|             5.552|
|versicolor|            5.936|              4.26|
|    setosa|5.005999999999999|1.4620000000000002|
+----------+-----------------+------------------+
     */
    
    // 특정 칼럼 제거하기
    val iris_x = iris_df.drop("Species")
    iris_x.show()
    
    // 전체 컬럼 대상 기술 통계량
    iris_x.describe().show()
                
    println("성공")
    spark.close()
  }
}