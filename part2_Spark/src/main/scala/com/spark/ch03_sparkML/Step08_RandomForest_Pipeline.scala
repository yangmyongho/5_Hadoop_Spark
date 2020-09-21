package com.spark.ch03_sparkML

import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.feature.{StringIndexer, VectorAssembler}
import org.apache.spark.ml.classification.RandomForestClassifier  // model
import org.apache.spark.ml.classification.RandomForestClassificationModel  // model save/load
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.mllib.evaluation.MulticlassMetrics


object Step08_RandomForest_Pipeline {
  
  def main(args: Array[String]): Unit = {
    
     // 1. SparkSession 객체 생성 
     val spark = SparkSession.builder.master("local").appName("dataFrameAPI").getOrCreate()
     
     val df = spark.read.format("csv").option("header", "true").option("delimiter", ",")
     .option("inferSchema", "true").load("src/main/resources/weather.csv")
     
     df.show()
     /*
      * |Sunshine|WindGustSpeed|Humidity|Pressure|RainTomorrow|
        +--------+-------------+--------+--------+------------+
        |     6.3|           30|      29|  1015.0|         Yes|
        |     9.7|           39|      36|  1008.4|         Yes|
      */
     df.printSchema()  // 컬럼 타입
     
     
     // 4. train/test split 7:3
     val Array(train, test) = df.randomSplit(Array(0.7, 0.3), seed=123)
     
     
     
     // 2. label 생성 : StringIndexer
     val sIndexer = new StringIndexer().setInputCol("RainTomorrow").setOutputCol("label")
     
     val sIndexerDF = sIndexer.fit(df).transform(df)
     println("[show classified label]")
     sIndexerDF.show()
     /*
      * |Sunshine|WindGustSpeed|Humidity|Pressure|RainTomorrow|label|
        +--------+-------------+--------+--------+------------+-----+
        |     6.3|           30|      29|  1015.0|         Yes|  1.0|
      */
     
     // 3. features 생성 : |Sunshine|WindGustSpeed|Humidity|Pressure|  >> features
     val assembler = new VectorAssembler()
     .setInputCols(Array("Sunshine", "WindGustSpeed", "Humidity","Pressure"))
     .setOutputCol("features")
     
     val weather_df = assembler.transform(sIndexerDF)
     println("[show DataFrame with label, features(libsvm)]")
     weather_df.show(false)
     
     
     // 5. model 생성
     val rf_obj = new RandomForestClassifier(). setLabelCol("label").setFeaturesCol("features")
     .setNumTrees(10)
     
     
     
     // 위 내용은 step07과 동일함.
     
     
     
     // 6. pipeline : step1 label 생성 -> step2 features 생성 -> step3 model 생성
     val pipeline = new Pipeline().setStages(Array(sIndexer, assembler, rf_obj))
     
     val pipelineModel = pipeline.fit(train)
     
     val pred = pipelineModel.transform(test)
     
     println("[show pipeline prediction]")
     pred.show()
     pred.select("label", "prediction")
     
     
     // 7. model 평가
     
     // (1) accuracy 분류정확도
     val evaluator = new MulticlassClassificationEvaluator().setLabelCol("label").setPredictionCol("prediction")
     
     
     val acc = evaluator.setMetricName("accuracy").evaluate(pred)  
     val f1 = evaluator.setMetricName("f1").evaluate(pred) 
     println(s"model accuracy = ${acc}, f1 score = ${f1}")  // model accuracy = 0.847457627118644, f1 score = 0.8405084745762711
     
     // (2) confusion matrix
     import spark.implicits._  // DF -> RDD
     val predRdd = pred.select("label", "prediction").as[(Double, Double)].rdd
     
     val conmat = new MulticlassMetrics(predRdd)
     println("[show confusion matrix]")
     println(conmat.confusionMatrix)
     /*
      * 91.0  11.0  
				7.0   9.0  
      */
     
     
     
     
     
     // 객체 닫기
     spark.close()
  }
  
}