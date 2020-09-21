package chap06_Collection


/*
 * 바인딩 메서드(Binding method)
 *  - 컬렉션 객체에서 호출 가능한 함수
 *  형식) object.method1().method2()
 *  - 처리 : 원소를 순차적으로 method1에 넘김 -> method1처리 -> method2(처리)
 *  - 자체 제너레이터(반복) 기능이 포함되어 있음.
 */


object Step04_Binding {
  
  def main(args: Array[String]) : Unit = {
    
    // 1. 컬렉션 객체 생성 - 그냥 벡터
    var nums = 1 to 20  //  숫자 컬렉션
    println(nums.size)  // 20
    
    // 2. 바인딩 메서드
    // 1) object.foreach(func) : object 원소를 순차적으로 받고, func 자료 처리
    println("[foreach]")
    nums.foreach( (x:Int) => print(x + " ") )  // 무명함수 : (x) => x+1     // 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 
    println()
    
    // 2) object.map(_매핑연산) : object원소를 순차적으로 받고(_), 연산 수행
    println("[map]")
    var map_re = nums.map(_ * 2)  // 각 원소에 곱하기 2(각 원소를 1:1로 매핑 연산)    
    println(map_re)  // // Vector(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40)
    
    // 3) object.filter(_조건식) : object 원소를 순차적으로 받고(_), 조건에 따라서  필터링
    var filter_re = nums.filter(_ % 2 == 0)  // 짝수인 것만 필터링.
    println(filter_re)  // Vector(2, 4, 6, 8, 10, 12, 14, 16, 18, 20)
    
    // 4) object.filter(_조건식).map(_매핑연산)
    var filter_map_re = nums.filter(_ >= 10).map(_ * 0.5)
    println(filter_map_re)  // Vector(5.0, 5.5, 6.0, 6.5, 7.0, 7.5, 8.0, 8.5, 9.0, 9.5, 10.0)
    
    
    
    
    // 1. 컬렉션 객체 생성 - 그냥 벡터
    val num = List(1,2,3,4,5,1.5,2,3.5,7) 
    println(nums.size)  // 20
    
    // 2. 바인딩 메서드
    // 1) object.foreach(func) : object 원소를 순차적으로 받고, func 자료 처리
    println("[foreach : List]")
    num.foreach( (x:Double) => print(x + " ") )
    println()
    
    // 2) object.map(_매핑연산) : object원소를 순차적으로 받고(_), 연산 수행
    println("[map : List]")
    var map_re2 = num.map(_ * 2)   
    println(map_re) 
    
    // 3) object.filter(_조건식) : object 원소를 순차적으로 받고(_), 조건에 따라서  필터링
    var filter_re2 = num.filter(_ % 2 == 0)
    println(filter_re)
    
    // 4) object.filter(_조건식).map(_매핑연산)
    var filter_map_re2 = num.filter(_ >= 10).map(_ * 0.5)
    println(filter_map_re) 
    
  }
}