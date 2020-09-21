package chap06_Collection

/*
 * Map 컬렉션 특징
 *  - python의 dict와 유사함
 *  - (key -> value)
 *  - 수정 불갸/수정가능(import 필요)
 *  - key를 통해서 value 접근
 *  형식) Map(key -> value, "key" -> value, ...)
 */


import scala.collection.mutable


object Step03_Map {
  
  def wc(texts : String) : Unit = {
    
    // 수정 가능한 Map 생성
    var word_count = mutable.Map.empty[String, Int]  // [key type, value type]
    
    // "Kim, Hong! Kim, Hong, You" -> split("[ ,!]+")
    var words = texts.split("[ ,!]+") 
    println(words)  // [Ljava.lang.String;@7cc355be  : 해쉬 값
    println(words.mkString(" "))  // Kim Hong Kim Hong You
    
    for(wd <- words){
      var word = wd.toLowerCase()  // 소문자 변경
      
      // 향후 더할 word 출현 빈도수
      var word_value
      = if(word_count.contains(word)) {   // 기존 저장된 word이면
        word_count(word) // key -> value
      }else{
        0
      }
      
      // 수정 가능한 map에 (key -> value) 저장
      word_count += (word -> (word_value+1)) 
      
    }  // for문 end  
    
    // word count 결과
    println(word_count)
    
  }  //func end
  
  
  def main(args: Array[String]) : Unit ={
    
    // 1. 수정 불가
    var map_list = Map("one" -> 1, "two" ->2)
    println(map_list)  // Map(one -> 1, two -> 2)
    println(map_list.size)  // 2
    println(map_list("two"))  // 2
    
    // Map("key" -> (값1, 값2, 값3))
    val emp = Map(1001 -> ("홍길동", 250, 50), 1002 -> ("이순신", 350, 100), 1003 -> ("유관순", 200, 40))
    println(emp)
    println(emp(1002))  // (이순신,350,100)
    
    for(e <- emp.keys) {
      println(e)  // key
      println(emp(e))  // key -> value
    }
    
    // 2. 수정 가능
    val texts = "Kim, Hong! Kim, Hong, You"
    
    // 함수 호출
    wc(texts)
    

    
  }
}