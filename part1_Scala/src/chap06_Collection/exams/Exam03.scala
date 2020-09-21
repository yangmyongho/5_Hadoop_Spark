package chap06_Collection.exams

import scala.io.Source // file read package import
import scala.collection.mutable // 수정 가능 컬렉션 
import scala.collection.mutable

/*
 * 문) text_data.txt 텍스트 파일을 대상으로 단어 빈도수를 구하는 함수 정의하기 
 */

object Exam03 {     
  
  
  
    // 워드카운터 함수 
    def countWords(text: String) = {
      
      // 수정가능한 빈 Map 생성
      var word_count = mutable.Map.empty[String, Int]
                    
      // 단어 단위로 split
      var words = text.split("[ .]+")
      println(words)  // hash 
      println(words.mkString(" "))  // splitted words
      println(words.size)
      
      // 맵 객체 생성
      for(w <- words) {
        
        // 맵 객체 생성 준비 단계
        var word = w.toLowerCase()
        
        // 1씩 더하기 전 워드 카운트(향후 단어가 나타나면 여기에 1씩 더함)
        var value_will_add
        = if(word_count.contains(word)) {
          word_count(word)
        }else{
          0
        }
        
        word_count += (word -> (value_will_add+1))
        
      }
      
      // word count 생성 완료
      println(word_count)
      println(word_count.size)
    }
    
    
    
    
    
    
    // main 함수 
    def main(args: Array[String]): Unit = {
      
      
         try {
           
              // text file read
              var texts = ""
              for(line <- Source.fromFile("C:\\ITWILL\\Work\\5_Hadoop_Spark\\workspace\\part1_Scala\\src\\fileDir\\text_data.txt").getLines){
                  //println(line)
                  texts += line
              }
              println(texts)  // 모든 문장을 한줄에 다 합치기
                         
              // 워드 카운트 함수 호출 
              countWords(texts)  
          } catch {
              case ex: Exception => println(ex)
          }
          
          print("~프로그램 종료~")
          
    }
  
}