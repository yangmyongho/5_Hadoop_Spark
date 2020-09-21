package chap04_FileRead_except.exams

import scala.io.Source
import java.io.FileNotFoundException

/*
 * 문) iris.csv 데이터셋에서 "setosa"가 포함된 행만 출력하기 
 */

object Exam {
  
  def csv_load(file_name : String) : Unit = {
    
    try{
      val fileSource = Source.fromFile("C:\\ITWILL\\Work\\5_Hadoop_Spark\\workspace\\part1_Scala\\src\\fileDir\\"+file_name)
      println("file 호출 됨....")    
      
      var lines = fileSource.getLines()
      for(line<-lines if(line.contains("setosa"))) {
        var cols = line.split(",").map(_.trim())
        
      println(s"${cols(0)}, ${cols(1)}, ${cols(2)}, ${cols(3)}, ${cols(4)}")
      }        
      
      fileSource.close()
      
    }catch{
      case ex : FileNotFoundException => println("예외 정보 : " + ex)
    }
    
  } 
  
  def main(args: Array[String]): Unit = {
      csv_load("iris.csv")
  }
  
}

