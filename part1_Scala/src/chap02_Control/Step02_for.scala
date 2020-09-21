package chap02_Control

/*
 * 형식 1)
 * for(변수 <- 컬렉션) {     //컬렉션은 파이썬의 열거형객체와 비슷.
 * 		반복문1
 * 		반복문2
 * }
 * 
 * - 컬렉션(collection) : 열거형 data를 저장하는 자료구조 통칭(Array, List)
 * - 제너레이터 식 : (변수 <- 컬렉션)
 * 
 * 
 * 
 * 형식 2)
 * for(변수 <- 컬렉션 if 조건식) 반복문
 * 
 * - 가드(guard) : if 조건식 : 조건에 만족하는 항목만 반복문 실행
 */

object Step02_for {
  def main(args : Array[String]) : Unit = {
    
    //for 형식 1)
    var tot = 0 //var tot : Int = 0
    
    // start until stop-1 : 정수 컬렉션 생성
    for(i <- 1 until 11){   //range(n) 유사함
      print(i + " ") // 같은 line에 중복해서 출력
      tot += i  // 누적변수
    }  // for end
    
    println()
    println("tot = " + tot)
    
    // start to stop : 정수 컬렉션 생성
    tot = 0
    for(i <- 1 to 10){
      //format()  함수
      print("i = %d, tot = %d\n".format(i, tot))
      tot += i
    }  // for end
    println("\ntot = " + tot)
    
    // List 컬렉션
    var dogList = List("진돗개-한국", "셰퍼드-독일", "불독-독일", "풍산개-한국")
    // 블록없는 for문
    for(dog <- dogList) println(dog)
    
    
    
    // for 형식 2) 가드를 포함
    println("가드 포함 경우")
    for(dog <- dogList if(dog.contains("한국"))) println(dog)
    // python : [for 변수 in 열거형객체 if 조건식]
    
    // 조건식으로 and 연산자 적용
    println("조건식 : and 연산자 적용")
    for(dog <- dogList if(dog.contains("한국") && dog.startsWith("진돗개"))) println(dog)
    
    // 조건식으로 or 연산자 적용
    println("조건식 : or 연산자 적용")
    for(dog <- dogList if(dog.contains("한국") || dog.startsWith("진돗개"))) println(dog)
    
    /*
     * 문) 가듣 문법을 적용하여 다음 결과를 출력하시오.
     * 진돗개-한국
     * 셰퍼드-독일
     * 불독-독일
     */
    println("문줴")
    for(dog <- dogList if( dog.contains("독일") || dog.startsWith("진돗개"))) println(dog)
    
    
    // yield(양보) : 프린트 되는 것을 양보하고 변수에 저장이 될 수 있게끔.
    // var 변수 = for(변수 <- 컬렉션 if 조건식) yield
    var dogVar = for(dog<-dogList if(dog.contains("한국")))
        yield dog
    println("dogVar = " + dogVar)
    // python : 변수 = [실행문 for 변수 in 열거형 if 조건식]

  }
}