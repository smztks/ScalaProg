package com.example.scalaprog

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/10/22
 * Time: 12:59
 * To change this template use File | Settings | File Templates.
 */

// 第23章 for式の再説
object ScalaProg23

object Sp23_00 {
  // 「children: Person*」連続パラメータ ... P162
  case class Person(name: String, isMale: Boolean, children: Person*)
  val lara = Person("Lara", false)
  val bob = Person("Bob", true)
  val julie = Person("Julie", false, lara, bob)
  val persons = List(lara, bob, julie)

  val a = persons filter (p => !p.isMale) flatMap (p => (p.children map (c => (p.name, c.name))))
  println(a)
  // List((Julie,Lara), (Julie,Bob))

  // withFilter=フィルタをかけた上で、mapとかforeachを実行できる。
  // 男性データを集めた中間データ構造の生成を回避できる。
  val b = persons withFilter (p => !p.isMale) flatMap (p => (p.children map (c => (p.name, c.name))))
  println(b)
  // List((Julie,Lara), (Julie,Bob))

  val c = for (p <- persons; if !p.isMale; c <- p.children)
    yield (p.name, c.name)  // yield=産出する、生成する
  println(c)
  // List((Julie,Lara), (Julie,Bob))

  // 1.結果値を生成(yield)するすべてのfor式は、map,flatMap,withFilterの組み合わせに変換される。
  // 2.結果値を生成しないfor式は、withFilterとforeachの組み合わせに変換される。

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// for式
object Sp23_01 {
  // for ( seq ) yield expr
  // seq = generator; definition; filter

  //for (p <- persons; n = p.name; if (n startsWith "To"))
  //  yield n

  /*
  for {
    p <- persons            // generator  ... pat <- expr: pat内の変数が、要素内の対応する部分に束縛される
    n = p.name              // definition ... pat = expr: exprの値にpatパターンを束縛する
    if (n startWith "To")   // filter     ... if expr: exprがfalseを返す要素を反復処理から脱落させる
  } yield n
   */

  case class Person(name: String, isMale: Boolean, children: Person*)
  val lara = Person("Lara", false)
  val lalara1 = Person("Lalara1", false)
  val lalara2 = Person("Lalara2", false)
  val bob = Person("Bob", true)
  val julie = Person("Julie", false, lara, bob, lalara2)
  val persons = List(lara, bob, julie, lalara1, lalara2)

  val a = for(p <- persons; n = p.name; if(n startsWith "La"))
    yield n
  println(a)

  // 後発のジェネレータのほうが先発のジェネレータよりも速いペースで変化する。
  val b = for (x <- List(1, 2, 3); y <- List("one", "two", "three"))
    yield (x, y)
  println(b)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// N女王問題
// cf. http://ja.wikipedia.org/wiki/%E3%82%A8%E3%82%A4%E3%83%88%E3%83%BB%E3%82%AF%E3%82%A4%E3%83%BC%E3%83%B3
// cf. http://www.kawa.net/works/js/8queens/nqueens.html
object Sp23_02 {
  var cnt = 0
  def queens(n: Int): List[List[(Int, Int)]] = {
    def placeQueens(k: Int): List[List[(Int, Int)]] = {
      println("k: " + k + " - placeQueens ")
      if (k == 0) {// 0段目の場合は初期化
        List(List())  //空リストという一個の要素だけを持つリスト
      } else {
        for {
          // 再帰処理 ... placeQueensの戻り値は、k段にk個の女王を置いたすべての解を生成する。
          queens <- placeQueens(k - 1)  // チェス盤にk-1個の女王を置いたすべての部分解を得るまで反復処理
          //queensには、List(List((x,y),(x,y),...),List((x,y),(x,y),...))内のList((x,y),(x,y),...)を渡す。
          column <- 1 to n  // k番目の女王を置けるすべての行を得るまで反復処理
          queen = (k, column) // タプル(P.334)にて、k段目,column行の女王の位置候補を生成します tuple:n組
          if isSafe(queen, queens) // filter: isSafeがfalseの場合、解は生成されない。
        } yield queen :: queens
      }
    }
    placeQueens(n)
  }
  def isSafe(queen: (Int, Int), queens: List[(Int, Int)]) =
    // forall(P.308):リストのすべての要素に対して条件が当てはまればtrue、1個でも条件外があればfalse
    if (queens forall (q => !inCheck(queen, q))){
      println("O: "+ queen + "|" + queens)
      true
    } else {
      println("X: " + queen + "|" + queens)
      false
    }
  def inCheck(q1: (Int, Int), q2: (Int, Int)) =
    q1._1 == q2._1 || // 同じ段にあるかどうか
    q1._2 == q2._2 || // 同じ行にあるかどうか
    (q1._1 - q2._1).abs == (q1._2 - q2._2).abs  // 同じ対角上（段・行座標の差の絶対値） abs:絶対値

  println("O: " + queens(4))
  /*
  k: 4 - placeQueens
  k: 3 - placeQueens
  k: 2 - placeQueens
  k: 1 - placeQueens
  k: 0 - placeQueens
  O: (1,1)|List()
  O: (1,2)|List()
  O: (1,3)|List()
  O: (1,4)|List()
  X: (2,1)|List((1,1))
  X: (2,2)|List((1,1))
  O: (2,3)|List((1,1))
  O: (2,4)|List((1,1))
  X: (2,1)|List((1,2))
  X: (2,2)|List((1,2))
  X: (2,3)|List((1,2))
  O: (2,4)|List((1,2))
  O: (2,1)|List((1,3))
  X: (2,2)|List((1,3))
  X: (2,3)|List((1,3))
  X: (2,4)|List((1,3))
  O: (2,1)|List((1,4))
  O: (2,2)|List((1,4))
  X: (2,3)|List((1,4))
  X: (2,4)|List((1,4))
  X: (3,1)|List((2,3), (1,1))
  X: (3,2)|List((2,3), (1,1))
  X: (3,3)|List((2,3), (1,1))
  X: (3,4)|List((2,3), (1,1))
  X: (3,1)|List((2,4), (1,1))
  O: (3,2)|List((2,4), (1,1))
  X: (3,3)|List((2,4), (1,1))
  X: (3,4)|List((2,4), (1,1))
  O: (3,1)|List((2,4), (1,2))
  X: (3,2)|List((2,4), (1,2))
  X: (3,3)|List((2,4), (1,2))
  X: (3,4)|List((2,4), (1,2))
  X: (3,1)|List((2,1), (1,3))
  X: (3,2)|List((2,1), (1,3))
  X: (3,3)|List((2,1), (1,3))
  O: (3,4)|List((2,1), (1,3))
  X: (3,1)|List((2,1), (1,4))
  X: (3,2)|List((2,1), (1,4))
  O: (3,3)|List((2,1), (1,4))
  X: (3,4)|List((2,1), (1,4))
  X: (3,1)|List((2,2), (1,4))
  X: (3,2)|List((2,2), (1,4))
  X: (3,3)|List((2,2), (1,4))
  X: (3,4)|List((2,2), (1,4))
  X: (4,1)|List((3,2), (2,4), (1,1))
  X: (4,2)|List((3,2), (2,4), (1,1))
  X: (4,3)|List((3,2), (2,4), (1,1))
  X: (4,4)|List((3,2), (2,4), (1,1))
  X: (4,1)|List((3,1), (2,4), (1,2))
  X: (4,2)|List((3,1), (2,4), (1,2))
  O: (4,3)|List((3,1), (2,4), (1,2))
  X: (4,4)|List((3,1), (2,4), (1,2))
  X: (4,1)|List((3,4), (2,1), (1,3))
  O: (4,2)|List((3,4), (2,1), (1,3))
  X: (4,3)|List((3,4), (2,1), (1,3))
  X: (4,4)|List((3,4), (2,1), (1,3))
  X: (4,1)|List((3,3), (2,1), (1,4))
  X: (4,2)|List((3,3), (2,1), (1,4))
  X: (4,3)|List((3,3), (2,1), (1,4))
  X: (4,4)|List((3,3), (2,1), (1,4))
  O: List(List((4,3), (3,1), (2,4), (1,2)), List((4,2), (3,4), (2,1), (1,3)))
   */
  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// for式によるクエリー
object Sp23_03 {
  case class Book(title: String, authors: String*)

  val books:List[Book] = {
    List(
      Book("吾輩は猫である", "A夏目漱石", "B夏目漱石"),
      Book("山月記", "中島敦"),
      Book("臨終までa", "梶井久"),
      Book("臨終までb", "梶井久"),
      Book("海a", "梶井基次郎a"),
      Book("海b", "梶井基次郎b"),
      Book("ある女の生涯", "A島崎藤村", "B島崎藤村"),
      Book("夜明け前", "A島崎藤村", "B島崎藤村", "C島崎藤村")
    )
  }

  // startsWith = ～で始まる
  val x = for (b <- books; a <- b.authors
    if a startsWith "A")
  yield (b.title, a)
  println(x)

  // indexOf = 文字列の中で文字 ch が最初に出現する位置を、0を基点とする文字数
  val y = for (b <- books if (b.title indexOf "a") >= 0)
    yield b.title
  println(y)

  // 異なる書籍で、同じ著者を検索
  val z = for (b1 <- books; b2 <- books if b1 != b2;
    a1 <- b1.authors; a2 <- b2.authors if a1 == a2)
  yield a1
  println(z)

  // zの重複削除
  def removeDuplicates[A](xs: List[A]): List[A] = {
  //def removeDuplicates(xs: List[String]): List[String] = {
    if (xs.isEmpty) xs
    else
      xs.head :: removeDuplicates(
        //xs.tail filter (x => x != xs.head)
        //xs.tail filter (_ != xs.head)
        for (x <- xs.tail if x != xs.head) yield x
      )
  }
  println(removeDuplicates(z))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// for式の変換
object Sp23_04 {
  // すべてのfor式は、map,flatMap,withFilterの3つの高階関数によって表現できる。

  // ①ジェネレータが1個のときのfor式の変換
  // for (x <- expr1) yield expr2 ... expr1.map(x => expr2)

  // ②1個のジェネレーターと1個のフィルターで始まるfor式の変換
  // for (x <- expr1 if expr2) yield expr3 ...
  // for (x <- expr1 withFilter (x => expr2)) yield expr3 ...
  // expr1 withFiter (x => expr2) map (x => expr3)

  // ②'フィルターの後ろにさらに要素が含まれている場合
  // filterのあとに新たにfor式の要素が追加された場合は、まずfilterの変換を行ってから次の要素の変換にとりかかる
  // for (x <- expr1, if expr2; seq) yield expr3 ...
  // for (x <- expr1 withFilter expr2; seq) yield expr3
  // 例：
  // for(x <- expr1 if expr2; if expr3) yield expr4 ...
  // for(x <- expr1 filter expr2; if expr3) yield expr4 ...
  // for((x <- expr1 filter expr2) filter expr3) yield expr4 ...

  // ③2個のジェネレーターで始まるfor式の変換
  //for (x <- expr1; y <- expr2; seq) yield expr3
  //expr1.flatMap(x => for (y <- expr2; seq) yield expr3)

  case class Book(title: String, authors: String*)

  val books:List[Book] = {
    List(
      Book("吾輩は猫である", "A夏目漱石", "B夏目漱石"),
      Book("山月記", "中島敦"),
      Book("臨終までa", "梶井久"),
      Book("臨終までb", "梶井久"),
      Book("海a", "梶井基次郎a"),
      Book("海b", "梶井基次郎b"),
      Book("ある女の生涯", "A島崎藤村", "B島崎藤村"),
      Book("夜明け前", "A島崎藤村", "B島崎藤村", "C島崎藤村")
    )
  }

  // 異なる書籍で、同じ著者を検索
  /*
  val z = for (b1 <- books; b2 <- books if b1 != b2;
    a1 <- b1.authors; a2 <- b2.authors if a1 == a2)
  yield a1
  */
  val z = books flatMap (b1 =>
    books withFilter (b2 => b1 != b2) flatMap (b2 =>
      b1.authors flatMap (a1 =>
        b2.authors withFilter (a2 => a1 == a2) map (a2 =>
          a1))))
  println(z)

  // ④ジェネレーターに含まれるパターンの変換 ... ジェネレータで表現されるのがタプルの場合
  // for ((x1, ..., xn) <- expr1) yield expr2 ...
  // expr1.map { case z(x1, ..., xn) => expr2}

  // for (pat <- expr1) yield expr2
  // expr1 withFilter {
  //  case pat => true
  //  case _ => false
  // } map {
  //  case pat => expr2
  // }

  // ⑤定義の変換 ... 組み込みの定義が含まれている場合
  // for (x <- expr1; y = expr2; seq) yield expr3 ...
  // for ((x, y) <- for (x <- expr1) yield (x, expr2); seq)
  // yield expr3

  // × 毎回、y = zzzの式を再評価してしまう
  //for (x <- 1 to 1000; y = zzz)
  //yield x * y

  // ○ y = zzzの式を評価した値を使用
  //val y = zzz
  //for (x <- 1 to 1000) yield x * y

  // ⑥forループの変換
  // for (x <- expr1) body ...
  // expr1 foreach (x => body)

  // for (x <- expr1; if expr2; y <- expr3) body ...
  // expr1 withFilter (x => expr2) foreach (x =>
  //  expr3 foreach (y => body))

  val xss = List(List(1,2,3),List(4,5,6))

  var sum1 = 0
  for(xs <- xss; x <- xs) sum1 += x
  println(sum1)

  var sum2 = 0
  xss foreach (xs =>
    xs foreach (x =>
      sum2 += x))
  println(sum2)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 逆方向への変換
object Sp23_05 {
  object Demo {
    def map[A, B](xs: List[A], f: A => B): List[B] =
      for (x <- xs) yield f(x)
    def flatMap[A, B](xs: List[A], f: A => List[B]): List[B] =
      for (x <- xs; y <- f(x)) yield y
    def filter[A](xs: List[A], p: A => Boolean): List[A] =
      for (x <- xs if p(x)) yield x
  }

  val list1 = List(1,2,3,4,5)
  val d1 = Demo.map(list1, (x: Int) => { x + 1 })
  val d2 = Demo.flatMap(list1, (x: Int) => { List(x + 1) })
  val d3 = Demo.filter(list1, (x: Int) => { x < 3})
  println(d1 + "|" + d2 + "|" + d3)

  val list2 = List("abc", "def", "ghi")
  val d4 = Demo.map(list2, (x: String) => { x.toList })
  val d5 = Demo.flatMap(list2, (x: String) => { x.toList })
  val d6 = Demo.filter(list2, (x: String) => { x < "efg" })
  println(d4 + "|" + d5 + "|" + d6)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// forの一般化
object Sp23_06 {
  /*
  ①mapだけを定義している型
    1個のジェネレーターだけで構成されるfor式を使える。
  ②mapのほかにflatMapも定義している型
    複数のジェネレーターから構成されるfor式を使える
  ③foreachを定義している型
    forループを使える（ジェネレーターは1個でも複数でもよい）
  ④withFilterを定義している型
    for式の中でifで始まるフィルター式を使える。
   */
  abstract class C[A] {
    def map[B](f:A=>B):C[B]
    def flatMap[B](f:A=>C[B]):C[B]
    def filter(p:A => Boolean):C[A]
    def foreach(b:A => Unit):Unit
  }

  // map,flatMap, filter, 「ユニット」コンストラクター（インスタンスコンストラクターorファクトリーメソッド）
  // ...モナド（関数型言語で，参照透明性を保持しながら手続き型的な記述をするための枠組み）は
  // この4個によって特徴づけられる。
  // cf. http://itpro.nikkeibp.co.jp/article/COLUMN/20091023/339329/
  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// Maybeモナド = 失敗するかもしれない演算をつなぎ合わせる。モナドには種類がある。
// http://itpro.nikkeibp.co.jp/article/COLUMN/20091023/339329/?ST=develop&P=5
// http://itpro.nikkeibp.co.jp/article/COLUMN/20091023/339329/?SS=imgview&FD=-558131221&ST=develop

// 手続き的な発想だと「この場合はAとBの結果になり，さらにAは…」という発想でプログラムだったが、
// モナドの場合は，「演算をつなぎ合わせる」という発想となる。
// モナドらしいプログラムを書く最大のポイントは，flatMapに渡せる関数を書くこと
object Sp23_98 {
  val config = Map(
    "database"  -> List(("path","/var/app/db"),("encoding1" , "euc-jp" ),("encoding2" , "utf-8" )),
    "urlmapper" -> List(("cgiurl", "/app"),("rewrite" , "True")),
    "template"  -> List(("path" , "/var/app/template"))
  )

  // 通常のコーディング
  def findNormal(x:Map[String,List[(String,String)]]):String =
    x.get("database") match {
      case Some(y) => y.find(z => z._1 == "encoding1") match {
        case Some(a) => a._2
        case None    => "Nothing!"
      }
      case None => "Nothing!"
    }
  println(findNormal(config))
  // euc-jp

  def show(x: Option[(String, String)]) =
    x match {
      case Some(y) => y._2
      case None    => "Nothing!"
    }
  // モナド式
  println(show(config.get("database").flatMap(_.find(_._1 == "encoding2"))))
  println(show(config.get("urlmapper").flatMap(_.find(_._1 == "rewrite"))))
  println(show(config.get("template").flatMap(_.find(_._1 == "path"))))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// Listモナド = 適用するたびに値が増えたり減ったする関数をつなぎ合わせる
// http://itpro.nikkeibp.co.jp/article/COLUMN/20091023/339329/?ST=develop&P=7
object Sp23_99 {
  def expandCharClass(x: String): List[String] = {
    val pattern = """(.*)\[(.*)\](.*)""".r

    def expandChar(x: String): List[String] = {
      val pattern(start, ext, end) = x
      ext.toList.map(y => start + y + end)
    }

    // unapplySeq=可変個の引数に対応した上記のようなドメイン名パターンマッチを実現するための抽出子
    pattern.unapplySeq(x) match {
      case Some(y) =>  expandChar(x)
      case None    =>  List()
    }
  }
  println(expandCharClass("abc[012].{jpg,mpg}"))

  def expandAltClass(x: String): List[String] = {
    val pattern = """(.*)\{(.*)\}(.*)""".r

    def expandAlt(x: String): List[String] = {
      val pattern(start, ext, end) = x
      ext.split(',').toList.map(y => start + y + end)
    }

    pattern.unapplySeq(x) match {
      case Some(y) =>  expandAlt(x)
      case None    =>  List()
    }
  }
  println(expandAltClass("abc[012].{jpg,mpg}"))

  println(expandAltClass("abc[012].{jpg,mpg}").flatMap(expandCharClass))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}
