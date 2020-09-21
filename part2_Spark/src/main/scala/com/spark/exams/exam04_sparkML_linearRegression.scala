package com.spark.exams

import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.evaluation.RegressionEvaluator

object exam04_sparkML_linearRegression {
  
  /*
   *  score_iq.csv 파일을 대상으로 선형회귀분석을 수행하시오.
   *  - y 변수(label) : score
   *  - x 변수(features) : iq, academy, game, tv
   *  - sid 변수 제거
   *  - train/test split(8:2)
   *  - model 생성 : train set
   *  - model 평가 : test set
   */
  
  def main(args: Array[String]) : Unit = {
    
    val spark = SparkSession.builder.master("local").appName("dataFrameAPI").getOrCreate()
    
    // score_iq.csv import
    val df = spark.read.format("csv").option("header", "true").option("inferSchema", "true")
    .load("C:\\ITWILL\\Work\\5_Hadoop_Spark\\workspace\\part2_Spark\\src\\main\\resources\\com\\spark\\test\\score_iq.csv")
    
    df.show(false)
    /*
     * +-----+-----+---+-------+----+---+
        |sid  |score|iq |academy|game|tv |
        +-----+-----+---+-------+----+---+
        |10001|90   |140|2      |1   |0  |
        |10002|75   |125|1      |3   |3  |
     */
    
    // sid 변수 제거
    val df2 = df.drop("sid")
    df2.printSchema()  // 칼럼 type 확인
    df2.show()
    
    // x, y 변수 선택
    val assemble = new VectorAssembler().setInputCols(Array("iq", "academy", "game", "tv"))
    .setOutputCol("features")
    
    // old DF(score|iq|academy|game|tv) -> new DF(score|iq|academy|game|tv|features)
    val data = assemble.transform(df2)  // 위에서 x변수 설정에 참여하지 않은 score가 자동 label
    data.show()
    
    // train / test split(8:2)
    val Array(train, test) = data.randomSplit(Array(0.8, 0.2), seed=123)
    
    // model 생성 : train
    val lr = new LinearRegression().setMaxIter(10)
    .setFeaturesCol("features").setLabelCol("score")
    
    val model = lr.fit(train)
    
    println(s"기울기 : ${model.coefficients}, 절편 : ${model.intercept}")
    // 기울기 : [0.4271456493396368,1.793673195743125,-0.5785553604646255,-0.6678316482739358], 절편 : 23.961163645327552
    
    //  train 오차
    val model_summary = model.summary
    println(s"train set r2 = ${model_summary.r2}")  // train set r2 = 0.9532999594435441
    println(s"train set MSE = ${model_summary.meanSquaredError}")  // train set MSE = 1.8018270468308004
    
    // model 평가 : test set
    val pred = model.transform(test)
    pred.show()
    
    pred.select("score", "prediction","features").show()
    
    // 평가 객체 생성
    val evaluator = new RegressionEvaluator()
    .setLabelCol("score").setPredictionCol("prediction")
    
    val mse = evaluator.setMetricName("mse").evaluate(pred)
    val r2 = evaluator.setMetricName("r2").evaluate(pred)
    
    println("Model evaluation results using test set")
    println(s"mse : ${mse}, r2 score : ${r2}")
    // mse : 2.0059887838086885, r2 score : 0.9641912995429347
    //  ㄴ  표준화안되어서인지 mse 상태 좋지 않음.
    
    
    // 객체 종료
    println("성공~!!")
    spark.close()
    
  }
}