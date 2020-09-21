package com.spark.ch03_sparkML

import org.apache.spark.sql.SparkSession  // create DF 

//전처리
// ham -> 0, span -> 1 dummy 변수 생성
import org.apache.spark.ml.feature.StringIndexer
import org.apache.spark.ml.feature.VectorAssembler  // features 생성

//test mining
import org.apache.spark.ml.feature.{RegexTokenizer, StopWordsRemover, CountVectorizer, IDF}  // 토큰 생성기(word) -> 불용어제거 -> word:고유번호 -> 희소행렬(idf가중치)
import org.apache.spark.ml.classification.{LogisticRegression}  // create model
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator  // evaluate model : 이항/다항 분류 평가

object Step05_Logistic {
  
  def main(args: Array[String]) : Unit = {
    
    // SparkSession 객체 생성
    val spark = SparkSession.builder.master("local").appName("dataFrameAPI").getOrCreate()
    
    
    // 1. dataset load
    val df = spark.read
              .format("csv")
              .option("header", "false")  // 제목이 없다.
              .option("delimiter", ",")
              .option("inferSchema", "true")
              .load("src/main/resources/sms_spam_data.csv")
    df.show()
    /*
     * | _c0|                 _c1|
        +----+--------------------+
        | ham|Go until jurong p...|
        | ham|Ok lar... Joking ...|
        |spam|Free entry in 2 a...|
      
           전처리 과정 : _c0 -> dummy, _c1 -> sparse matrix(features)
     */
    
    
    // 2. StringIndexer : String -> dummy변수
    val idx = new StringIndexer().setInputCol("_c0").setOutputCol("label")
    
    // fit() : model -> transform() : old DF -> new DF 
    val sms_data_label = idx.fit(df).transform(df)
    println("[타겟 변수를 범주화함]")
    sms_data_label.show()
    /*
     * | _c0|                 _c1|label|
        +----+--------------------+-----+
        | ham|Go until jurong p...|  0.0|
     */
    
    
    // 2. RegexTokenizer : 정규표현식을 이용한 토큰생성기
    val tokenizer = new RegexTokenizer().setInputCol("_c1")  // 문장
    .setOutputCol("words")  // 단어
    .setPattern("\\W+")  // 토큰 구분자 : 정규표현식 이용. 워드를 단위로 쪼개겠다는 뜻
    
    val tokenized = tokenizer.transform(sms_data_label)
    println("[show tokenized feature sentences]")
    tokenized.show()
    /*
     * | _c0|                 _c1|label|               words|
        +----+--------------------+-----+--------------------+
        | ham|Go until jurong p...|  0.0|[ ,  ,  , , , .. ...|
        | ham|Ok lar... Joking ...|  0.0|[ , ... ,  ,  ,  ...|
     */
    
    // 4. StopWordsRemover : 단어에 포함된 불용어 제거
    val stopWords = new StopWordsRemover().setInputCol("words")  // 불용어가 포함된 단어
    .setOutputCol("terms")  // 정제된 단어
    
    // old DF -> new DF
    val newData = stopWords.transform(tokenized)
    println("[show columns 'words' & 'terms', stopwords removed]")
    newData.select("words", "terms").show()  // before & after 임.
    
    // 5. CountVectorizer : word -> 고유번호, 단어 개수(feature 길이) 제한. python의 maxfeature와 비슷
    val countVec = new CountVectorizer().setInputCol("terms")  // 단어
    .setOutputCol("countVec")  // 고유번호(중복되지 않는)
    .setVocabSize(4000)  // 단어 길이
    
    val newDataCount = countVec.fit(newData).transform(newData)
    println("[show counter]")
    newDataCount.show()  // | _c0|   _c1|label|    words|   terms|   countVec|
    
    // 6. IDF : 단어 출현빈도수에 대한 가중치 (TFiDF)
    val tfidf = new IDF().setInputCol("countVec")
    .setOutputCol("tfidfVec")  // features
    
    val tfidf_data = tfidf.fit(newDataCount)  // model 만들고
    .transform(newDataCount)  // ?
    println("[print created features]")
    tfidf_data.show(false)  // | _c0|    _c1|label|  words|   terms| countVec|  tfidfVec| 가중치의 결과는 비율로 나옴
    
    // label -> y변수, tfidfVec -> x 변수(features)
    tfidf_data.select("label", "tfidfVec").show(false)
    
    // 7. features 생성
    val assemble = new VectorAssembler().setInputCols(Array("tfidfVec")) // x 변수 선택
    .setOutputCol("features")  // x 변수 -> features 지정
    
    // old DF -> new DF
    val data = assemble.transform(tfidf_data)
    println("[show data including label and feature]")
    data.show()
    data.select("label", "features").show()  // libsvm file이 됐음
    
    
    
    
    
    // 8. train/test split
    val Array(train, test) = data.randomSplit(Array(0.8, 0.2))
    
    // 9. model 생성
    val lr = new LogisticRegression().setMaxIter(10).setRegParam(0.01).setLabelCol("label")  // y 변수
    .setFeaturesCol("features")  // x 변수
    
    val model = lr.fit(train)
    val pred = model.transform(test)
    println("[show prediction]")
    pred.show()
    pred.select("label","prediction").show(1000)
    
    // 10. model evaluation
    val evaluator = new MulticlassClassificationEvaluator().setLabelCol("label")  // 정답 컬럼
    .setPredictionCol("prediction")  // 예측치 컬럼
    
    val acc = evaluator.setMetricName("accuracy").evaluate(pred)
    val f1 = evaluator.setMetricName("accuracy").evaluate(pred)
    println(s"model accuracy : ${acc}, f1 score : ${f1}")  // model accuracy : 0.9823825503355704, f1 score : 0.9823825503355704
    
    
    // 객체 닫기
    println("success~~~!!")
    spark.close()
  }
  
  
}