package com.spark.ch03_sparkML

import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.feature.VectorAssembler  // features 생성
// 모델 생성, model save/load
import org.apache.spark.ml.classification.{LogisticRegression, LogisticRegressionModel}
// Pipeline model, save, load
import org.apache.spark.ml.{Pipeline, PipelineModel}
// 이항/다항 분류 평가
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator

/*
 * Logistic Regression + Pipeline(model workflow) 기법 적용
 * 
 */

object Step04_logistic_Pipeline {
  
  def main(args: Array[String]) : Unit = {
    
    // SparkSession 객체 생성
    val spark = SparkSession.builder.master("local").appName("dataFrameAPI").getOrCreate()
    
    
    // 1. dataset 생성
    import spark.implicits._  // scala -> DF
    
    // 1) train set : 키, 몸무게, 나이, 성별(y)
    val train = List((171, 68.65, 29, 1), 
                      (175, 74.5, 35, 1),
                      (159, 58.6, 29, 0)).toDF("height", "weight", "age", "gender")
    train.show()
    
    // 2) test set : 키, 몸무게, 나이, 성별(y)
    val test = List((169, 65.0, 35, 1), 
                      (161, 52.0, 29, 0),
                      (171, 70.5, 25, 1)).toDF("height", "weight", "age", "gender")
    test.show()
    
    // 2. Pipeline Step1 : assembler 생성 : features
    val assemble = new VectorAssembler().setInputCols(Array("height", "weight", "age"))
    .setOutputCol("features")
    
    val trainset = assemble.transform(train)  // old DF -> new DF
    println("[show trainset]")
    trainset.show()  // 어떤 컬럼을 가지고 있는지 확인해야 모델 생성 시 setLabelCol과 setFeaturesCol에 올바른 컬럼을 기재할 수 있음
    
    // 3. Pipeline Step2 : create model : trainset
    val lr_obj = new LogisticRegression()
    .setMaxIter(10)  // 반복학습 횟수
    .setRegParam(0.01)  // learning rate. 학습률
    .setLabelCol("gender")  // y 변수
    .setFeaturesCol("features")  // x 변수
    //val model = lr.fit(trainset)  // model 생성
     
    
    // 위 내용은 Step03과 동일함
    
    
    // 4. Pipeline model : step1 : features => step2 : lr_model
    val pipeline = new Pipeline().setStages(Array(assemble, lr_obj))
    
    // pipeline model 생성
    val pipelineModel = pipeline.fit(train) 
    
    // pipeline model 평가
    val pred = pipelineModel.transform(test)
    println("[show pipeline model's prediction]")
    pred.show()
    
    // 5. Pipeline model save/load
    val path = "C:/hadoop-2.6.0/pipeModel"
    pipelineModel.write.overwrite().save(path)  // model save
    println("model saved")
    
    val new_pipeModel = PipelineModel.load(path)
    val newpred = new_pipeModel.transform(train)
    println("[show new prediction]")
    newpred.show()
    
    spark.close()
  }
}