package com.spark.exams

import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.{Pipeline, PipelineModel}
// label, features 전처리 
import org.apache.spark.ml.feature.{StringIndexer, VectorAssembler} 
// model 생성 & save
import org.apache.spark.ml.classification.RandomForestClassifier
import org.apache.spark.ml.classification.RandomForestClassificationModel
// model 평가 
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator // model 평가  
import org.apache.spark.mllib.evaluation.MulticlassMetrics // 교차분할표 

/*
 * 문) 다음과 같은 단계별로 Pipeline 모델을 생성하시오.  
 */
object exam06_SparkML_randomForest {
    
  def main(args: Array[String]): Unit = {
    
    val spark = SparkSession
      .builder()
      .appName("exam06_SparkML_randomForest")
      .master("local[*]")
      .getOrCreate()
      
    // 단계1. dataset load   
    val filePath = "src/main/resources"  
    val df = spark.read
      .option("header", "true")
      .option("inferSchema", "true") // raw data type 적용 
      .csv(filePath + "/iris.csv")    
    
    // column 이름 변경 
    val newName = Seq("Sepal_Length", "Sepal_Width", "Petal_Length", "Petal_Width", "Species")
    val newDF = df.toDF(newName: _*)
    
    println("[show new DataFrame]")
    newDF.printSchema()    
    newDF.show()
    
    
    
    // 단계2. label 칼럼 생성 : Species 컬럼 이용 
    val sIndexer = new StringIndexer().setInputCol("Species").setOutputCol("label")
    
    val sIndexerDF = sIndexer.fit(newDF).transform(newDF)
    println("[show created label]")
    sIndexerDF.show(150)
        
    
   
    // 단계3. features 칼럼  생성 : "Sepal_Length","Sepal_Width","Petal_Length","Petal_Width" 컬럼 이용  
    val assembler = new VectorAssembler()
    .setInputCols(Array("Sepal_Length","Sepal_Width","Petal_Length","Petal_Width"))
    .setOutputCol("features")
    
    val iris_df = assembler.transform(sIndexerDF)
    println("[show DataFrame with label, features]")
   
        
        
    // 단계4. Split 70% vs 30% : weather_data -> data 수정 
    val Array(train, test) = newDF.randomSplit(Array(0.7, 0.3), seed=123)
    
    
    
    // 단계5. model 생성  : RandomForestClassifier 클래스 이용 
    val rfc = new RandomForestClassifier().setLabelCol("label")
    .setFeaturesCol("features").setNumTrees(10)    
    
    
    
    // 단계6. pipeline model : step(label -> feature -> model) 
    val pipe = new Pipeline().setStages(Array(sIndexer, assembler, rfc))
    val pipeModel = pipe.fit(train)
     
    val pred = pipeModel.transform(test)
    println("[show prediction]")
    pred.show()
    pred.select("label", "prediction").show()
    
    
        
    // 단계7. pipeline model 평가 : accuracy, confusion matrix
    
    // accuracy & f1 score
    val evaluator = new MulticlassClassificationEvaluator().setLabelCol("label")
    .setPredictionCol("prediction")
    
    val acc = evaluator.setMetricName("accuracy").evaluate(pred)
    val f1 = evaluator.setMetricName("f1").evaluate(pred)
    println(s"pipeline model accuracy : ${acc}, f1 score : ${f1}")
    //pipeline model accuracy : 0.96, f1 score : 0.9600000000000001
    
    
    // confusion matrix
    import spark.implicits._
    
    val predRdd = pred.select("label", "prediction").as[(Double, Double)].rdd
    val conmat = new MulticlassMetrics(predRdd)
    
    println("[show confusion matrix]")
    println(conmat)  // org.apache.spark.mllib.evaluation.MulticlassMetrics@6033f36c
    println(conmat.confusionMatrix)
    /*
     * 8.0  0.0   1.0   
        0.0  20.0  0.0   
        1.0  0.0   20.0  
     */

    
    
    
    
    // model save & load
    pipeModel.write.overwrite().save("C:/hadoop-2.6.0/pipeModel2")
    println("Pipeline Model saved")
    
    val pipeModelLoaded = PipelineModel.load("C:/hadoop-2.6.0/pipeModel2")
    println("[show loaded pipeline model results with test set applied]")
    pipeModelLoaded.transform(test).show()
    
    // accuracy test
    val pred_loaded = pipeModelLoaded.transform(test)
    
    val evaluator_loaded = new MulticlassClassificationEvaluator()  // 우선 이벨류에이터 새로만들긴 했지만 만드나 안만드나 값 똑같음.
    .setLabelCol("label").setPredictionCol("prediction")
    val acc_loaded = evaluator_loaded.setMetricName("accuracy").evaluate(pred_loaded)
    val f1_loaded = evaluator_loaded.setMetricName("f1").evaluate(pred_loaded)
    print(s"test set applied, loaded pipelineModel accuracy = ${acc}, f1 score = ${f1}")
    //test set applied, loaded pipelineModel accuracy = 0.96, f1 score = 0.9600000000000001
    
    spark.close()
    
  }
}