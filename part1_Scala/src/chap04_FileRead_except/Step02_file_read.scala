package chap04_FileRead_except

// io 패키지 import
import scala.io.Source  // file을 source로 read
import java.io.FileNotFoundException  // 예외처리

object Step02_file_read {
  
  // iris.csv -> read 함수 정의
  def csv_load(filename : String) : Unit ={
    try{
      val fileRead = Source.fromFile("C:\\ITWILL\\Work\\5_Hadoop_Spark\\workspace\\part1_Scala\\src\\fileDir\\" + filename)
      println("~iris.csv load 성공~")
      
      
      var lines = fileRead.getLines().drop(1)  // 전체 줄 단위 읽기, 제목 행 제거(이때, 인덱스가 아닌 그냥 행 번호를 입력한다.)
      for(line <- lines){
        // 한 줄 읽기 -> 콤마 기준 토큰 생성 -> 토큰 앞/뒤 공백 제거(map()) : _는 콤마가 제거되고 맵핑된 아규먼트 -> 변수 저장
        var cols = line.split(",").map(_.trim)  // line -> column
        
        // 칼럼 단위로 출력 : s"${칼럼}"
        println(s"${cols(0)}, ${cols(1)}, ${cols(2)}, ${cols(3)}, ${cols(4)}")
      }
      fileRead.close  //file 객체 닫기
      
    }catch{
      case ex : FileNotFoundException => println("예외정보 : " + ex)
    }
  }
  
  def main(args: Array[String]) : Unit = {
    
    try{
      val fileRead = Source.fromFile("C:\\ITWILL\\Work\\5_Hadoop_Spark\\workspace\\part1_Scala\\src\\fileDir\\scala_object.txt")
      println("file 읽기 성공")
      
      // 줄 단위 전체 읽기
      var lines = fileRead.getLines()
      for(line <- lines) println(line)
      
      fileRead.close()  // file 객체 닫기. 읽으면 닫아줄 것.
    }catch{
      case ex : FileNotFoundException => println("예외정보 : " + ex)
    }
    println("!프로그램 종료!")
    
    // 함수 호출
    csv_load("iris.csv")

  }
  
}