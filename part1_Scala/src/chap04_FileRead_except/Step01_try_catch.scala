package chap04_FileRead_except

/*
 * 예외처리 : 실행 시점 오류 처리 과정
 * try{
 * 		예외발생 가능한 코드
 * }catch{                    >>> python에서는 except였음
 * 		예외처리 코드 작성
 * }
 * 
 */

object Step01_try_catch {
  
  def main(args : Array[String]) : Unit ={
    
    var lst = List(10, 20, 30, 40, 50)
    var size = lst.size  // 원소 개수 반환
    println("size = " + size)  // size = 5
    println(lst(0))  // 첫번째 원소
    println(lst(size-1))  // 마지막 원소 : lst(4)
    
    for(i <- lst) print(i + " ")
    println()
    
    try{
      // 예외 발생
      for(i <- 0 until 6)  // 0 - 5
        print(lst(i) + " ")  // 10 20 30 40 50 // java.lang.IndexOutOfBoundsException
    }catch{
      //case 객체 : 예외클래스
      case ex : IndexOutOfBoundsException => println("\n예외정보 : " + ex)
    }
    println("!프로그램 종료!")
    
    
      
      
  }
  
}