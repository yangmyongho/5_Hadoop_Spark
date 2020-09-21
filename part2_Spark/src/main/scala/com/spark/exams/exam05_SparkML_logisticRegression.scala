package com.spark.exams

  /*
   * 문) iris_libsvm.txt 파일을 이용하여 로지스틱 선형회귀모델을 생성하시오. 
   * y변수 : label 
   * x변수 : features
   * train/test split 비율 - 70 : 30 
   * train set : model 생성 
   * test set : model 평가(accuracy, f1 score)   
   */

import org.apache.spark.sql.SparkSession
//import org.apache.spark.ml.feature.VectorAssembler // x,y 변수 선택 
import org.apache.spark.ml.classification.{LogisticRegression, LogisticRegressionModel} // model, save/load 
import org.apache.spark.ml.{Pipeline, PipelineModel} // pipeline model, save/load
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator // model 평가
// pipeline model 생성, save, load
import org.apache.spark.ml.{Pipeline, PipelineModel}

object exam05_SparkML_logisticRegression {
  def main(args: Array[String]) {

    val spark = SparkSession
      .builder()
      .appName("Logistic_PipelineSample")
      .master("local[*]")
      .getOrCreate()

    // 1. data set Load
    val data = spark.read.format("libsvm")
      .load("C:\\ITWILL\\Work\\5_Hadoop_Spark\\workspace\\part2_Spark\\src\\main\\resources\\com\\spark\\test\\iris_libsvm.txt") 
    println("[show data]")
    data.show() // label, features 확인 
          

    // 2. train/test split    
    val Array(train, test) = data.randomSplit(Array(0.7, 0.3), seed=123)
    println("[show train set]")
    train.show(105)
    println("[show test set]") 
    test.show(45)
      
    
    // 3. model 생성 환경 : 로지스틱 회귀 모델   --??.setLabelCol("label") or .setLabelCol("Species") ???
    val lr = new LogisticRegression().setMaxIter(10).setRegParam(0.01)
    .setLabelCol("label").setFeaturesCol("features")
    println("[lr created]")  // 확인됨
    
    val model = lr.fit(train)
    
    println("[model created]")
        
    // 4. model 평가 : 이항/다항 모두 적용 가능 
    val pred = model.transform(test)
    println("[show prediction results]")
    pred.show()
    
    val evaluator = new MulticlassClassificationEvaluator()
    .setLabelCol("label").setPredictionCol("prediction")
    
    val acc = evaluator.setMetricName("accuracy").evaluate(pred)
    val f1 = evaluator.setMetricName("f1").evaluate(pred)
    println(s"accraycy : ${acc}, f1 score : ${f1}")
    
    
    spark.stop()
  }
  
}