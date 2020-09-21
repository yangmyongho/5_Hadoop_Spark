package chap06_Collection


/*
 * 컬렉션(collection)
 *  - 데이터의 집합을 의미함
 *  - 수정 여부, 순서보장(index), 중복 허용 등으로 분류
 *  
 * Array 컬렉션 특징
 *  - 수정 가능
 *  - 순서 존재 : index 사용
 *  - 동일 type(자료형)만 저장 가능
 *    형식) var 변수 : Array[type(ex : Int, Float, String ...)] = new Array[type](size)
 */

object Step01_Array {
  
  def main(args: Array[String]) : Unit = {
    
    // 1. new 명령어 객체 생성 1) object 생성 -> 2) arr(index) = 값
    var arr : Array[Int] = new Array[Int](5)
    arr(0) = 10 // python : arr[0]
    arr(1) = 20
    arr(2) = 30
    arr(3) = 40
    arr(4) = 50    
    
    // 원소 수정
    arr(4) = 500
    
    // 다른 자료형을 집어넣었을 때
    //arr(4) = 500.12  // error
    
    for(i <- arr) print(i+" ")  // 10 20 30 40 500 
    println()
    
    // 2. 객체 생성 + 초기화
    var arr2 = Array(10, 20, 33, 40, 50)
    for(i <- arr2 if(i % 2 == 0)) print(i+" ")  // 10 20 40 50 
    println()
    
    // 3. Array 생성 축약형
    var arr3 = new Array[Double](50)  // 1) object 생성
    
    // index : 0 ~ 49 -> start until stop-1
    var idx = 0 until 50
    for(i <- idx) {
      var r = Math.random()  // 0~1
      arr3(i) = r  // 2. data 삽입
      }
    
    // 컬렉션 원소 출력
    println("arr3 size = " + arr3.size)
    
    // 가드 추가
    var cnt = 0
    for(a <- arr3 if(a >= 0.5 && a <= 0.8)) {
      print(a+"\n")
      cnt += 1
    }
    println("선택된 원소 개수 = %d개".format(cnt))
    
  }
  
}