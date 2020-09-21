package com.spark.dataFrame_SQL

import org.apache.spark.sql.SparkSession

object Step1_dataFrame_create {
  
  
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
    val colNames = Seq("col1", "col2", "col3", "col4", "Species")
    // old DF -> new DF
    val iris_df = df.toDF(colNames : _*)
    iris_df.show()  // |col1|col2|col3|col4|Species|
    
    // 컬럼 단위 통계 구하기
    iris_df.describe("col1").show()
    /*
     * +-------+------------------+
|summary|              col1|
+-------+------------------+
|  count|               150|
|   mean| 5.843333333333335|
| stddev|0.8280661279778637|
|    min|               4.3|
|    max|               7.9|
+-------+------------------+
     */
    
    // 집단 변수로 그룹화
    val df_grp = iris_df.groupBy("Species")
    df_grp.count().show()
    /*
     * +----------+-----+
|   Species|count|
+----------+-----+
| virginica|   50|
|versicolor|   50|
|    setosa|   50|
+----------+-----+
     */
    // 그룹 단위 통계
    df_grp.max("col1").show()
    df_grp.min("col1").show()
    df_grp.mean("col1").show()
    df_grp.sum("col1").show()
    /*
     * +----------+------------------+
|   Species|         sum(col1)|
+----------+------------------+
| virginica| 329.3999999999999|
|versicolor|             296.8|
|    setosa|250.29999999999998|
+----------+------------------+
     */
    
    // DataFrame save : project 디렉터리에 저장
    iris_df.write.format("csv")
             .option("header", "true")
             .mode("overwrite")
             .save("C:\\ITWILL\\Work\\5_Hadoop_Spark\\workspace\\part2_Spark\\src\\main\\resources\\com\\spark\\test/output_df")
    
    // DataFrame save : Hdfs 디렉터리에 저장 >>>>>>>>>>>>>>>>>>>왜안되니?????????????????????????
    iris_df.write.format("csv")
             .option("header", "true")
             .mode("overwrite")
             .save("hdfs://localhost:9000/output_df")
             
    println("성공")
    spark.close()
  }
}