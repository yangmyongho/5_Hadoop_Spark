package chap06_Collection.exams

import scala.io.Source
import scala.collection.mutable // 가변 컬렉션 제공 

/*
 *  문) input.txt 파일를 대상으로 공백 단위로 토큰을 생성한 후 중복된 토큰을 제거하고 다음과 같이 출력하시오.
 *  
 *  <출력결과>  
 *  Set(good, output, funvery, timemouse, programming, is, devicekeyboard, devicecomputer, system, fun, a, have, input)
 *  단어 수 = 13
 */
object Exam02 {
  
  // 시작점 : main 함수 
  def main(args: Array[String]): Unit = {
    
      try {
          var texts = ""
          for(line <- Source.fromFile("C:\\ITWILL\\Work\\5_Hadoop_Spark\\workspace\\part1_Scala\\src\\fileDir\\input.txt").getLines){
              texts += line
          }
          
          // 내용 작성 
          
          // 1) 단어로 쪼개기
          println(texts)  // programming is funvery fun!have a good timemouse is input devicekeyboard is input devicecomputer is input output system
          var words = texts.split("[ !]+")
          var temp = for(word <- words) yield word  // 해도 그만 안해도 그만임 어차피 해시태그로나옴
          
          // 2) Set에 넣어서 중복 제거
          var word_set = mutable.Set.empty[String]
          for(word <- words) {
            word_set += word
          }
          println(word_set)
          
          
    
     
      } catch {
          case ex: Exception => println(ex)
      } 
       
  }
}