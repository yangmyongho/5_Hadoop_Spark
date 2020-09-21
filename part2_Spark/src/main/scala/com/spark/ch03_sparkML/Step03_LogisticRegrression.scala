package com.spark.ch03_sparkML

import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.feature.VectorAssembler  // features 생성
// 모델 생성, model save/load
import org.apache.spark.ml.classification.{LogisticRegression, LogisticRegressionModel}
// 이항/다항 분류 평가
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator

object Step03_LogisticRegrression {
  
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
    
    // 2. assembler 생성 : features
    val assemble_train = new VectorAssembler().setInputCols(Array("height", "weight", "age"))
    .setOutputCol("features")
    
    val trainset = assemble_train.transform(train)  // old DF -> new DF
    println("[show trainset]")
    trainset.show()  // 어떤 컬럼을 가지고 있는지 확인해야 모델 생성 시 setLabelCol과 setFeaturesCol에 올바른 컬럼을 기재할 수 있음
    
    // 3. create model : trainset
    val lr = new LogisticRegression()
    .setMaxIter(10)  // 반복학습 횟수
    .setRegParam(0.01)  // learning rate. 학습률
    .setLabelCol("gender")  // y 변수
    .setFeaturesCol("features")  // x 변수
    val model = lr.fit(trainset)  // model 생성
    
    // 4. model evaluation : test set
    // 1) create test set assembler 
    val assemble_test = new VectorAssembler().setInputCols(Array("height", "weight", "age"))
    .setOutputCol("features")
    
    val testset = assemble_train.transform(test)  // old DF -> new DF
    println("[show testset]")
    testset.show()
    
    // 2) create model prediction : model transform
    val pred = model.transform(testset)
    println("[show prediction results]")
    pred.show()  // |height|weight|age|gender|         features|       rawPrediction|         probability|prediction|
    
    pred.select("gender", "prediction").show()  // 정답 vs 예측치
    /*
     * +------+----------+
      |gender|prediction|
      +------+----------+
      |     1|       0.0|
      |     0|       0.0|
      |     1|       1.0|
      +------+----------+
     */
    
    // 이항 or 다항 분류를 평가하기
    val evaluator = new MulticlassClassificationEvaluator()
    .setLabelCol("gender")  // answer column
    .setPredictionCol("prediction")  // predicted column
    
    // 분류분석 평가도구는 accuracy와 f1 score 등이 있다.
    val acc = evaluator.setMetricName("accuracy").evaluate(pred)  // evaluator의 label column "gender"와 prediction column "prediction"을 모두 갖고 있는.
    val f1 = evaluator.setMetricName("f1").evaluate(pred)
    println(s"accuracy : ${acc}, f1 score : ${f1}")
    // accuracy : 0.6666666666666666, f1 score : 0.6666666666666666
    
    // 5. model save & load
    // model save
    val path = "C:/hadoop-2.6.0/lrModel"
    model.write.overwrite().save(path)
    
    println("model successfully saved")
    
    
    
    // model load
    val new_lrmodel = LogisticRegressionModel.load(path)
    
    // new dataset 적용
    val result = new_lrmodel.transform(trainset)
    println("[result for new model loaded from directory, trainset applied]")
    result.show()
    result.select("gender", "prediction").show()
    
    spark.close()
  }
  
}