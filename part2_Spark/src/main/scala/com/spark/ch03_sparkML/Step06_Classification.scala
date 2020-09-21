package com.spark.ch03_sparkML

/*
 * Tree model + confusion matrix
 */

import org.apache.spark.sql.SparkSession  // create DF 

import org.apache.spark.ml.feature.StringIndexer
import org.apache.spark.ml.classification.{DecisionTreeClassifier}  // create tree model
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator  // evaluate model
import org.apache.spark.mllib.evaluation.MulticlassMetrics // confusion matrix
/*
 * ml vs mllib
 * ml : DataFrame model
 * mllib : RDD model
 */


object Step06_Classification {
  
  def main(args: Array[String]) : Unit = {
    
    // SparkSession 객체 생성
    val spark = SparkSession.builder.master("local").appName("dataFrameAPI").getOrCreate()
    
    // 1. dataset load
    val df = spark.read.format("libsvm")
    .load("C:\\ITWILL\\Work\\5_Hadoop_Spark\\workspace\\part2_Spark\\src\\main\\resources\\iris_libsvm.txt")
    df.show()
    
    // 2. train/test split
    val Array(train, test) = df.randomSplit(Array(0.7, 0.3), seed=123)
    // dataset memory loading
    train.cache()
    test.cache()
    
    // 3. tree model
    //val dt = new DecisionTreeClassifier().setLabelCol("label").setFeaturesCol("features")
    val dt = new DecisionTreeClassifier().setLabelCol("label").setFeaturesCol("features").setPredictionCol("prediction")
    val model = dt.fit(train)
    
    println(s"변수의 중요도 : ${model.featureImportances}")
    // 변수의 중요도 : (4,[0,1,2,3],[0.042055032013320275,0.021027516006660148,0.9069854266440085,0.029932025336011126])
    // 변수의 인덱스 2>0>3>1 순서로 중요도가 높다.
    
    // old DF(df:2) -> newDF(pred:2+1) : feature 라는 컬럼이 하나 더 생김
    val pred = model.transform(test)
    println("[show prediction]")
    pred.show()

    
    // 4. model 평가
    val evaluator = new MulticlassClassificationEvaluator().setLabelCol("label")
    .setPredictionCol("prediction")
    
    val acc = evaluator.setMetricName("accuracy").evaluate(pred)
    val f1 = evaluator.setMetricName("f1").evaluate(pred)
    
    println(s"model accuracy : ${acc}, f1 score : ${f1}")
    // model accuracy : 0.94, f1 score : 0.9402848814827037  >> 아이리스라그런지 정확도가 좋음.
    
    // 교차분할표(confusion matrix) :  MulticlassMetrics는 mllib소속이므로, 이 아이가 사용할 수 있는 자료구조는 rdd이다.
    import spark.implicits._  // DF -> RDD
    /*
     * 1. scala <-> DataFrame
     * 2. rdd <-> DataFrame
     */
    
    // DataFrame -> rdd
    val predRdd = pred.select("label", "prediction").as[(Double, Double)].rdd
    println(predRdd)  // MapPartitionsRDD[78] at rdd at Step06_Classification.scala:71
    
    val conmat = new MulticlassMetrics(predRdd)  // RDD를 인수로.
    println("[print confusion matrix]")
    println(conmat.confusionMatrix)
    /*
      18.0  0.0   0.0   
      2.0   13.0  0.0   
      0.0   1.0   16.0  
     */
    
    
    // 객체 닫기
    spark.close()
  }
  
}