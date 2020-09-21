package chap03_Method

/*
 * 1. 익명함수 : 함수 이름이 없는 함수
 *   - python 람다 함수와 유사
 *   - 한 줄 함수
 *   - 형식) (인수 : type, 인수 : type) => 리턴값
 *   
 *   ex) (x, y) => x + y
 * 
 * 2. 컬렉션 인수 사용
 *   - 함수에서 컬렉션을 인수로 받는 함수
 *   - 컬렉선 : 여러 개의 원소를 저장하는 배열 자료구조
 *   - Array[type], List 클래스에 의해서 객체가 만들어짐
 */


object Step02_method_special {
  
  // 1. 익명 함수 정의
  var any_func = (x : Int) => x*x  // 정의
  println("any func = " + any_func(10))  // 호출
  
  // 정의 & 호출
  ((x : Int, y : Int) => println(x*y))(10, 20)
  
  // 정의 & 호출 2
  ((x : Int, y : Int) => println("any func = " + x*y))(10, 20)  // any func = 200
  
  // 익명함수 -> 일반함수
  def any_func2(x:Int, y:Int) : Int = {
    return x * y
  }
  
  println("any func = " + any_func2(10,20))  // any func = 200
  
  // 일반함수 -> 익명함수 + 함수 축소기법
  val any_func3 : (Int, Int) => Int = _*_
  
  println("any func = " + any_func3(10,20))  // any func = 200
  
  /*
   * 매핑된 아규먼트 : _
   *  - 인수에 의해서 넘겨받은 실제값(아규먼트)을 받는 역할
   *  
   *  - 매개변수(인수) vs 아규먼트(인수의 값)
   *  
   */
  
  // 2. 컬렉션 인수 함수 Array[type]
  def textPro(texts : Array[String]) : Unit = {
    println("texts size = " + texts.size)
    for(txt<-texts if(txt.contains("한국"))){
      println(txt)
    }
    /*
     * 홍길동-한국
     * 강감찬-한국
     */
  }
  
  def main(args: Array[String]) : Unit = {
    
    // 2. 컬렉션 인수 사용
    
    var txtAry = Array("홍길동-한국", "존-미국", "강감찬-한국", "마이클-호주")
    textPro(txtAry)
  }

  
}