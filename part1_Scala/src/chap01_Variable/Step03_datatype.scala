package chap01_Variable

/*
 * Scala 기본 자료형
 *   Int : 정수형(4byte) - default
 *   Long : 정수형(8byte)
 *   Float : 실수형(4byte)
 *   Double : 실수형(8byte) - default
 *   String : 문자형(2개 이상의 문자, n개 문자 조합)
 *   Char : 단일 문자형(1개 문자)
 *   Boolean : 논리형(true / false)
 */


object Step03_datatype {
  def main(args : Array[String]) : Unit = {    
    val calc : Int = 100 * 4 / 3  // 실수 계산 결과가 정수형으로 변환되어 저장
    //val calc2 : Int = 100 * 0.25 / 3  // error : type mismatch
    println("calc = " + calc)  // 133
    
    val calc_long : Long = 1000000*2
    print("calc_long=" + calc_long, calc_long.getClass())
    println()
    
    val calc_re : Float = 100 * 4 / 3
    println("calc_re = " + calc_re)
    val calc_re2 = 100 * 2.5 / 3
    println("calc_re2 = " + calc_re2 + ", type = " + calc_re2.getClass())
      // calc_re2 = 83.33333333333333, type = double
    printf("calc_re2 = %.5f", calc_re2)  // format 이용 출력문(줄바꿈 기능 X)
    println()  // line skip
    
    // 실수형 변수 선언
    val x : Double = 2.4
    val a : Float = 0.5f
    val b : Float = 1.0f
    val y_pred = x * a + b
    println("y_pred = " + y_pred)
    
    // 논리형 변수 선언
    var bool_re : Boolean = 2.5f == y_pred  // 논리식
    println("bool_re = "+bool_re)
    
    // 문자형과 단일문자형
    var strVar : String = "우리나라 대한민극"
    var charVar : Char = '우'  // 단일문자형(')
    println("strVar = " + strVar)
    print("charVar = " + charVar, " type = " + charVar.getClass())
  }
}