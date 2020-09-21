package com.spark.ch03_sparkML

import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.clustering.{KMeans, KMeansModel}  // create model
import org.apache.spark.mllib.evaluation.MulticlassMetrics  // model evaluation

object Step09_Clustering {
  
  def main(args: Array[String]) : Unit = {
    
    // Spark object
    val spark = SparkSession.builder().appName("Clustering")
    .master("local").getOrCreate()
    
    // dataset load
    val data = spark.read.format("libsvm").load("src/main/resources/iris_libsvm.txt")
    println("show iris libsvm file")
    data.show(false)
    /*
     * |label|features                       |
        +-----+-------------------------------+
        |0.0  |(4,[0,1,2,3],[5.1,3.5,1.4,0.2])|
     */
    
    // 2. k-mean : 확인적 군집분석(k=군집수)
    val kmeans = new KMeans().setK(3).setSeed(123l)  // 시드의 l 은 long이라는 뜻.
    
    val model = kmeans.fit(data)
    
    val pred = model.transform(data)
    
    println("[show clustering prediction]")
    pred.show(150, false)
    
    
    // 3. model evaluation
    import spark.implicits._
    
    val predRdd = pred.select("label","prediction").as[(Double, Double)].rdd
    
    val conmax = new MulticlassMetrics(predRdd)
    
    println("confusion matrix")
    println(conmax.confusionMatrix)
    /*
     * 0.0   48.0  14.0  
        50.0  0.0   0.0   
        0.0   2.0   36.0  
     */
    
    
    
    // 객체 닫기
    spark.close()
  }
  
}