package chap01_Variable

/*
 * Scala 변수/상수 선언
 *  - var : 값을 수정하는 변수 선언식 명령어
 *    형식) var 변수명 : 자료형  = 값
 *     ex) var num : Int = 100
 *  - 자료형 생략 시 자료형을 스칼라가 추론함.
 */


object Step01_var {
  def main(args: Array[String]) : Unit = {
    var num1 : Int = 1000 //type 명시한 예
    println("num1 = " + num1) //콘솔 출력
    var num2 = 2000 //type 생략한 예
    println("num2 = " + num2)
    
    var str1 : String = "대한민국, 우리나라"
    println("str1 = "+str1)
    var str2 = "korean"
    println("str2 = " + str2)
    
    // 변수 값 수정
    num1 = 100
    println("num1 = " + num1)
  }
}