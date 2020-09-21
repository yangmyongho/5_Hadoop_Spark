package chap01_Variable

/*
 * Scala 값 수정이 불가능한(상수) 변수 선언 명령어
 *  형식) val 변수명 : 자료형 = 값
 *    - 초기화 이후 수정 불가능
 *    - 참조는 가능하다.
 *    - 자료형 생략 시 추론 기능
 */


object Step02_val {
  def main(args: Array[String]) : Unit = {
    val numVal : Int = 100
    val numVal2 = 105.41
    println("numVal = " + numVal)
    println("type numVal = " + numVal.getClass())  // int
    println("numVal2 = " + numVal2)
    println("type numVal2 = " + numVal2.getClass())  // double
    
    // 변수 수정(안 되겠지만..)
    //numVal = 200  // error : reassignment to val
    
    // 참조 가능 : 연산
    var numVal_calc = numVal * 2
    println("numVal_calc = " + numVal_calc)
  }
}