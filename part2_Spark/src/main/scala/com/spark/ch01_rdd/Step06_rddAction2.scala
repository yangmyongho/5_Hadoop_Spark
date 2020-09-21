package com.spark.ch01_rdd

import org.apache.spark.{ HashPartitioner, SparkConf, SparkContext }
import org.apache.spark.storage.StorageLevel
import scala.collection.mutable.ListBuffer

/*
 * RDD Action 관련 method  
 *
 *  - rdd.takeSample() : RDD 원소를 대상으로 샘플 수를 지정하여 샘플링 
 *  - rdd.countByValue() : RDD 대상으로 단어 -> 출현빈도수 형태로 반환  
 *  - rdd.reduce() : RDD 전체 원소를 대상으로 하나의 값으로 병합하여 반환 
 *  - rdd.sum() : RDD 전체 원소의 합 반환 
 */

object Step06_rddAction {
  
  def takeSample(sc: SparkContext) {
    val rdd = sc.parallelize(1 to 100)
    val result = rdd.takeSample(false, 20) // false : 비복원이라는 뜻.
    println(result) // object info  : [I@72f46e16
    println(result.length)  // 20
    result.foreach( (x:Int) => print(x+",") )  // 73,7,33,47,95,36,72,62,28,55,24,34,66,40,80,48,91,27,46,83,
  }

  def countByValue(sc: SparkContext) {
    val rdd = sc.parallelize(List(1, 1, 2, 3, 3))
    val result = rdd.countByValue()
    println(result)  // Map(1 -> 2, 3 -> 2, 2 -> 1)
  }

  def reduce(sc: SparkContext) {
    val rdd = sc.parallelize(1 to 10, 3)
    val result = rdd.reduce(_+_)  // i = i + cnt
    println(result) // 55
  }
  
  def sum(sc: SparkContext) {
    val rdd = sc.parallelize(1 to 10)
    val result = rdd.sum()
    println(result) // 55.0
  }
  
  def main(args: Array[String]) {      
      val conf = new SparkConf()
          .setAppName("WordCountMapReduce")
          .setMaster("local")
  
      val sc = new SparkContext(conf)
     
      takeSample(sc)
      countByValue(sc)
      reduce(sc)      
      sum(sc)
    }

}