package chap03_Method.exams

/*
 * 문) 두 실수를 인수로 받아서 나눗셈 연산 후 실수값으로 반환하는  div 메서드 정의하기
 *    함수명 : div()
 */

object Exam01 {
  
  // div 메서드 정의
  def div(x : Float, y : Float) : Float = {
    var div : Float = x / y
    return div
    }
  
  def div3(x:Float, y:Float) : Double = {   // 리턴 타입은 상위타입으로 바꿀 수 있음(Double>Float)
    var div_re = x / y
    println(div_re.getClass())  // type 출력 : float
    return div_re  // 리턴값
  }
  
  def div2(x : Float, y : Float) : Unit = {
    var div : Float = x / y
    print("division result 2 = " + div)
    }
  

  
  def main(args: Array[String]): Unit = {
      // div 메서드 호출 
      var div_re = div(20.5f, 0.5f)
      println("division result = " + div_re)
      
      // 2번째 방법, return값이 없는 함수 호출
      div2(20.5f, 0.5f)
      
      // div3
      println()
      println("div = %.5f".format(div3(10.5f, 2.5f))) // div = 4.20000
 
  }
  
}


