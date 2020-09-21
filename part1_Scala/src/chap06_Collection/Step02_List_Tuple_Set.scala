package chap06_Collection

/*
 * 1. List 컬렉션 특징
 *  - 수정 불가(값 초기화는 가능)
 *  - 순서 존재(index)
 *  - 중복 허용
 *  - 동일 자료형만 저장 가능
 *  형식) val 변수명 = List(값1, 값2, ...)
 *  
 * 2. Tuple 컬렉션 특징
 *  - Tuple 클래스 없음(대신  기호 이용)
 *  - 수정 불가(값 초기화 가능)
 *  - 순서 존재(indexing 방식 : object._index(행번호))
 *  - 서로 다른 자료형도 저장 가능
 *  형식) val 변수 = (값1, 값2, ...)
 *  
 * 3. Set 컬렉션 특징
 *  - 수정 불가한 컬렉션 or 수정 가능 컬렉션 모두 가능. 이때, 수정 가능한 컬렉션을 만들려면 import 필요함
 *  - 순서 없음, 중복 불가능
 *  형식) val 변수 = Set(값1, 값2, ...)
 *  
 */

import scala.collection.mutable  // 수정 가능한 컬렉션 객체 생성


object Step02_List_Tuple_Set {
  
  def main(args: Array[String]) : Unit = {
    
    // 1. List 컬렉션
    val num = List(1,2,3,4,5,1.5,2,3.5,7)  // 이렇게 중간에 실수를 넣으면 모든 원소가 실수형으로 저장된다.
    println("num size = " + num.size)  // num size = 9
    
    println(num)  // List(1.0, 2.0, 3.0, 4.0, 5.0, 1.5, 2.0, 3.5, 7.0)
    println(num.mkString(" "))  //1.0 2.0 3.0 4.0 5.0 1.5 2.0 3.5 7.0
    println(num(0))  //1.0  :: index
    
    // 원소 수정
    //num(num.size-1) = 70  // error : value update is not a member of List
    
    val num2 = List.range(1,11)
    
    for(n <- num2) print(n + " ")  // 1 2 3 4 5 6 7 8 9 10 
    println()
    
    // 2. Tuple 컬렉션 특징
    val names = ("홍길동", 35, "이순신", 45, "유관순", 25)
    println(names)  // (홍길동,35,이순신,45,유관순,25)
    println(names._1)  // 홍길동
    println(names._2)  // 35
    
    // 제너레이터 식
    //for(name <- names) print(name)  // error : value foreach is not a member of (String, Int, String, Int)
    
    // 3. Set 컬렉션
    val num3 = Set(1,2,3,4,5,1,2,3)
    println("size of num3 = " + num3.size)  // size of num3 = 5  >> 중복된 것은 제외하고 셈(중복 불가)
    println(num3)  // Set(5, 1, 2, 3, 4)  >> 순서 없음
    
    // 문장 -> 단어 추출
    val texts = "kim hong! kim, park. hong"
    val wordArr = texts.split("[!,.]+")  // 공백, 특수문자 기준 -> word(토큰) 생성  >> 뒤에 + 기호는 해당 기준이 여러 개일 때
    val wordArr2 = texts.split("[ !,.]+")
    println("word : " + wordArr)  // [Ljava.lang.String;@506e1b77
    
    for(word <- wordArr) print(word+" / ")  // kim hong /  kim /  park /  hong / 
    println()
    for(word <- wordArr2) print(word+" / ")  // kim / hong / kim / park / hong / 
    println()
    
    // 수정 가능한 Set 컬렉션 생성
    val words = mutable.Set.empty[String]  // String 원소를 갖는 빈 Set 객체 생성
    for(word <- wordArr2) {
      words += word  // Set에 값 저장
    }
    
    // Set 컬렉션 보기
    print("수정된 컬렉션 내용 : " + words + " ")  // 수정된 컬렉션 내용 : Set(hong, park, kim) 
    println("\n단어수 : " + words.size)  // 단어수 : 3
  }
}