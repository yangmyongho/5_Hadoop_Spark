package com.spark.ch03_sparkML

import org.apache.spark.sql.SparkSession
// label, features 전처리
import org.apache.spark.ml.feature.{StringIndexer, VectorAssembler}  // label, features 전처리
import org.apache.spark.ml.classification.RandomForestClassifier  // model
import org.apache.spark.ml.classification.RandomForestClassificationModel  // model save/load
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator  // model evaluatin
import org.apache.spark.mllib.evaluation.MulticlassMetrics  // confusion matrix

object Step07_RandomForest {
  
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
     
     // 4. train/test split 7:3
     val Array(train, test) = weather_df.randomSplit(Array(0.7, 0.3), seed=123)
     
     // 5. model 생성
     val rf = new RandomForestClassifier(). setLabelCol("label").setFeaturesCol("features")
     .setNumTrees(10)
     
     val model = rf.fit(train)
     val pred = model.transform(test)
     println("[show predictions]")
     pred.show()
     pred.select("label", "prediction").show()
     
     // 6. model evaluation
     
     // (1) accuracy 분류정확도
     val evaluator = new MulticlassClassificationEvaluator().setLabelCol("label").setPredictionCol("prediction")
     
     
     val acc = evaluator.setMetricName("accuracy").evaluate(pred)  
     val f1 = evaluator.setMetricName("f1").evaluate(pred) 
     println(s"model accuracy = ${acc}, f1 score = ${f1}")  //model accuracy = 0.847457627118644, f1 score = 0.8405084745762711
     
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
     
     
     // 7. model save/load
     val path = "C:/hadoop-2.6.0/rfModel"
     model.write.overwrite().save(path)
     println("model saved~!!")
     
     val rfModel_load = RandomForestClassificationModel.load(path)
     println("모델 재로드 후 테스트 셋 반영")
     rfModel_load.transform(test).show()
     
     // 객체 닫기
     spark.close()
  }
  
}
  