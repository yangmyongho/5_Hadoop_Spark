package chap03_Method

/*
 *  method vs function
 *   method : class or object에서 선언한 함수(객체 지향언어)
 *   function : 단독으로 선언한 함수(함수 지향언어)
 *   
 *   형식)
 *   def 함수명(매개변수 : type) : 리턴타입 = {
 *   		실행문1
 *   		실행문2
 *   }
 *   
 *   반환값이 없는 경우 : 리턴타입 = Unit
 */


object Step01_method_basic {
  
  // max metod
  def max(x : Int, y : Int) : Int = {
    //if(x>y) x else y
    var max_re = if(x>y) x else y
    return max_re
  }
  
  // adder method
  def adder(x:Float, y:Float) : Float = {
    var add : Float = (x + y)*0.5f
    return add // return 값, 앞에 return을 명시해도 되고 안해도 됨.
  }
  
  // adder 2 : 반환값이 없는 method.
  def adder2(x : Float, y : Float) : Unit = {
    val add : Float = (x+y)*0.5f
    println("adder = " +add)
  }
  
  
  // PI = 3.14159
  def getPI() : Double = {
    //val PI = 3.14159  // 상수 선언 -> default : double
    return 3.14159
  }
  
  // return, {} 생략
  def  getPI2() : Double = 3.14159
  
  // 인수가 없는 함수는 사실 (), return, {} 모두 생략 가능
  def getPI3 : Double = 3.14159

  // 시작점. 이때, 이 main도 method.
  // 시작점 : main method
  def main(args: Array[String]) : Unit = {
    println("Max Method")
    val x = 20
    val y = 15
    var max_re = max(x,y)
    //max method 호출
    println("max = "+max_re)  // max = 20
    
    //adder method 호출
    var adder_re = adder(15f, 20f)
    println("adder = " + adder_re)
    
    //adder2 method 호출
    adder2(1.5f, 25.5f)
    
    // getPI method 호출
    println("PI = " + getPI())
    
    // getPI2 method호출
    println("PI = " + getPI2())
    
    // getPI3 method 호출
    println("PI = " + getPI3)
    
  }
}