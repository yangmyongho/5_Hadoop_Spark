package com.spark.ch03_sparkML

import org.apache.spark.sql.SparkSession  // DataFrame object
import org.apache.spark.ml.regression.LinearRegression  // create model
import org.apache.spark.ml.feature.VectorAssembler  // x, y 변수 선택
import org.apache.spark.ml.evaluation.RegressionEvaluator  // model evaluator

/*
 * csv file + Linear Regression model
 */

object Step02_LinearRegression_csv {
  
  def main(args: Array[String]): Unit = {
    
     // 1. SparkSession 객체 생성 
     val spark = SparkSession.builder.master("local").appName("dataFrameAPI").getOrCreate()
     
     val df = spark.read.format("csv").option("header", "true").option("delimiter", ",")
     .option("inferSchema", "true").load("C:\\ITWILL\\Work\\5_Hadoop_Spark\\workspace\\part2_Spark\\src\\main\\resources\\com\\spark\\test\\iris.csv")
     
     df.show()
     
     // 칼럼명 변경하기
     val colNames = Seq("Sepal_Length", "Sepal_Width", "Petal_Length", "Petal_Width", "Species")
     //old -> new DF
     val iris_df = df.toDF(colNames : _*)
     iris_df.show()

     //x,y 변수 생성  : y(Sepal_Length), x(나버지 3개)
     val iris_xy = iris_df.drop("Species")
     iris_xy.show()
     
     // 2. label, features 생성 : VectorAssembler
     val assemble = new VectorAssembler().setInputCols(Array("Sepal_Width", "Petal_Length", "Petal_Width"))  // x 변수 선택
     .setOutputCol("features")  // x 변수를 입력해서 얘네들 3개를 가지고 feature로 지정하겠다.
     
     val data = assemble.transform(iris_xy)  // 위에서 x 변수에 참여하지 않은 나머지 하나가 자동으로 label 로 됨.
     data.show()  // |Sepal_Length|Sepal_Width|Petal_Length|Petal_Width|     features|

     data.select("Sepal_Length", "features").show(false)
     
     // 3. train/test split(70% vs 30%)
     val Array(train, test) = data.randomSplit(Array(0.7, 0.3), seed=123)
     
     // 4. model 생성  : train set 이용
     val lr = new LinearRegression().setMaxIter(10)  // hyper parameter 10번 반복하겠다.
     .setFeaturesCol("features")  // x 변수
     .setLabelCol("Sepal_Length")  // y 변수
     
     val model = lr.fit(train)  // 훈련셋 이용
     
     println(s"기울기 : ${model.coefficients}, 절편 : ${model.intercept}")
     //기울기 : [0.5849917533462233,0.7270919758484158,-0.6413420724037501], 절편 : 2.09057060911327
     
     val model_summary = model.summary
     
     println(s"train set r2 : ${model_summary.r2}")
     println(s"train set MSE : ${model_summary.meanSquaredError}")
     // train set r2 : 0.871161168667772
     // train set MSE : 0.08355507425090175
     
     // 5. model 평가
     val pred = model.transform(test)  
     pred.show()  // |Sepal_Length|Sepal_Width|Petal_Length|Petal_Width|     features|       prediction|
     
     // 평가 객체 생성
     val evaluator = new RegressionEvaluator()
     evaluator.setLabelCol("Sepal_Length")  // 정답 칼럼
     .setPredictionCol("prediction")  // 예측치 칼럼
     
     // 평가 방법과 예측치 변수 지정
     val mse = evaluator.setMetricName("mse").evaluate(pred)  // 평균 제곱 오차
     val r2_score = evaluator.setMetricName("r2").evaluate(pred)  // 
     val mae = evaluator.setMetricName("mae").evaluate(pred)  // 평균 절대값 오차
     val rmse = evaluator.setMetricName("rmse").evaluate(pred)  // root 평균 제곱 오차
     
     println("Test set 적용 model 평가 결과")
     println(s"rmse : ${rmse}, mse : ${mse}, mae : ${mae}, r2 : ${r2_score}")
     // rmse : 0.35527888737371266, mse : 0.1262230878135032, mae : 0.29288148507062084, r2 : 0.8295839370878721

     
     // 객체 닫기      
     println("성공~!")
     spark.close()
     
  }
  
}