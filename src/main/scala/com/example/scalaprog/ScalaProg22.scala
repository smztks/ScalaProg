package com.example.scalaprog

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/10/21
 * Time: 13:23
 * To change this template use File | Settings | File Templates.
 */

// 第22章 リストの実装
object ScalaProg22

// Listクラスの原則
object Sp22_01 {
  /* NG 空のListコンストラクタの呼び出しはできない。= new Listはダメ
  new List
  new List()
  new List("")
  new List(Nil)
  new List(1)
  */
  val a = List  // OK

  val xs = List(1, 2, 3)
  var ys: List[Any] = xs

  /* List抽象クラスは、以下の3つの基本抽象メソッドで構成されている。
  def isEmpty: Boolean
  def head: T         // メソッドの処理はheadの抽出
  def tail: List[T]   // メソッドの処理はtailの抽出
  */

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// Nilオブジェクト
object Sp22_01_01 {
  /*
  // Nilはケースオブジェクト & NilはNothingのList ... headの結果値はNothing
  case object Nil extends List[Nothing]{
    // isEmptyの具象メソッドデス
    override def isEmpty = true
    // 値が無いので例外を投げますよ
    def head:Nothing = throw new NoSuchElementException("head of empty list")
    def tail:List[Nothing]= throw new NoSuchElementException("tail of empty list")
  }
   */
  println("end of: " + Thread.currentThread.getStackTrace()(1))
}
// ::クラス
object Sp22_01_02 {
  /*
  final case class ::[T](hd: T, tl: List[T]) extends List[T] {
    def head = hd
    def tail = tl
    override def isEmpty: Boolean = false
    // 実際は、連結処理が記載されている。
  }

  // final = サブクラスがメンバーをオーバーライトできないようにする。
  // case = ケースクラスのパラメータはフィールドとして扱える
  final case class ::[T](head: T, tail: List[T]) extends List[T] {
    override def isEmpty: Boolean = false
  }
   */

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// その他のメソッド
object Sp22_01_03 {
  /*
  def length: Int =
    if (isEmpty) 0 else 1 + tail.length // tailの再帰である地点まで加算する

  def drop(n: Int): List[T] =
    if (isEmpty) Nil
    else if (n <= 0) this
    else tail.drop(n - 1) // dropの再帰である地点(n=0)でのthisを返す。

  def map[U](f: T => U): List[U] =
    if (isEmpty) Nil
    else f(head) :: tail.map(f) // mapの再帰でf(head)の名前渡しパラメータ(_.toInt)を処理していく。
   */

  val abcde = List('a', 'b', 'c', 'd', 'e')
  println("length: " + (abcde length))
  println("drop: " + (abcde drop 2))
  println("map: " + (abcde map (_.toInt)))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// リストの構築
object Sp22_01_04 {
  // ::はリストのメソッド、メソッド名の最後がコロンの場合、右被演算子に束縛される。... P.64, P.107-108
  // 例： x:: List(1,2,3) ... 本当の形態は、List(1,2,3).::(x)

  // Lisクラスの「::」メソッド(cons)の定義
  // def :: [U >: T](x: U): List[U] = new scala.::(x, this)
  // このメソッド自体がUを使って多相的になり、[U >: T]の下限境界によってリスト要素型Tのスーパーに制限される。
  // 追加できる要素は、U型のものに限定され、結果型はList[U]となる。

  // ①Plant-Fruit1クラス
  abstract class Plant
  class Fruit1 extends Plant
  class Apple extends Fruit1
  class Orange extends Fruit1

  val apples = new Apple :: Nil
  // apples: List[Apple] = List(Apple@1a80183)
  val fruits1 = new Orange :: apples
  // fruits: List[Fruit] = List(Orange@13bedc4, Apple@183e895)

  val oranges = new Orange :: Nil
  val fruits1a = apples ::: oranges
  // fruits1a: List[Fruit1] = List(Orange@10a2d0d, Apple@11e3c55)

  // ②Musicクラス
  abstract class Music
  class Pops extends Music
  class Jazz extends Music

  val pops = new Pops :: Nil
  // pops: List[Pops] = List(Pops@18efa2f)
  val music = new Jazz :: pops
  // music: List[Music] = List(Jazz@281902, Pops@1bae5f5)

  // Fruit1とMusicクラスの共通の上位クラスがないので、ScalaObjectクラスで構成される。
  val mix = new Apple :: pops
  // mix: List[ScalaObject] = List(Apple@47122d, Pops@1bae5f5)

  // ③Plant-Fruit2クラス
  class Fruit2 extends Plant
  class Banana extends Fruit2

  val banana = new Banana :: Nil

  // Fruit1とFruit2クラスの共通の上位クラスがあるので、Plantクラスで構成される。
  val fruits2 = new Apple :: banana
  // fruits2: List[Plant] = List(Apple@672bb3, Banana@104ca54)

  val fruits2a = apples ::: oranges ::: banana
  // fruits2a: List[Plant] = List(Apple@1af0af7, Orange@18efd7c, Banana@9de959)

  val furits3 = fruits1a ::: fruits2a
  // furits3: List[Plant] = List(Apple@1af0af7, Orange@18efd7c, Apple@1af0af7, Orange@18efd7c, Banana@9de959)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// ListBufferクラス
object Sp22_02 {
  val xs = List(1, 2, 3)

  // ①末尾再帰でない再帰処理
  def incAll(xs: List[Int]): List[Int] =
    xs match {
      case List() => List()
      case x :: xs1 => x + 1 :: incAll(xs1)
      // incAllに対する再帰呼び出しは::演算の中で発生している為、
      // 末尾再帰（javaのループコードに置き換えられない）ではなく、
      // 再帰呼び出し毎にスタックフレームを必要としてしまう。 ... いままでのmatch式の例ではこのパターンだったが...

    }
  println(incAll(xs))

  // ②「:::」演算を使った場合、は第1被演算子の長さに比例して時間がかかる処理の為、処理効率が悪い。
  var result = List[Int]()
  for (x <- xs) result = result ::: List(x + 1)
  println(result)

  // ③bufというListBufferの末尾にelemという要素を追加する操作
  import scala.collection.mutable.ListBuffer
  val buf = new ListBuffer[Int] // ListBufferクラスを代入しないと「+=」が使えない。
  for (x <- xs) buf += x + 1
  println(buf.toList)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// Listクラスの実際の中身
object Sp22_03 {
  // mapの実態は、ListBuffer + While処理で、非末尾再帰実装ではない。
  /*
  import scala.collection.mutable.ListBuffer
  final override def map[U](f: T => U): List[U] = {
    val b = new ListBuffer[U]
    val these = this
    while (!these.isEmpty) {
      b += f(these.head)
      these = these.tail
    }
    b.toList
  }
   */

  // 以下、toListの計算量についての解説

  // 実際の「::」クラスの定義 ... tlがvarになっている。しかし、private[scala]修飾子が付いている。
  // スコープ範囲がscalaパッケージからのみのアクセスに限定...ListBufferクラスはからアクセス可能
  // ※Listは「見かけ上」は純粋関数型だが、「内実」はListBufferを使った命令型実装になっている。
  /*
  final case ::[U](hd: U, private[scala] var tl: List[U]) extends List[U] {
    def head = hd
    def tail = tl
    override def isEmpty: Boolean = false
  }
   */

  // ListBufferクラス
  // ListBufferの要素はリストとして表現されていて、新しい要素の追加にあたり
  // リストの最後の「::」セルのtlフィールドを書き換えている。
  /*
  package scala.collection.immutable
  final class  ListBuffer[T] extends Buffer[T] {
    private var start: List[T] = Nil // バッファに格納されているすべての要素のリストを示す
    private var list0: ::[T] = _  // リストの最後の「::」セルを示す
    private var exported: Boolean = false // toList操作を使ってバッファをリストに変換したかを示す
  }
   */

  // ListBufferクラス内のtoListメソッド
  // startが参照する要素リストを返す。
  /*
  override def toList: List[T] = {
    exported = !start.isEmpty // リストが空でなければexportedにtrueをセットする。
    start // startが参照する要素リストを返す
  }
   */

  // ListBufferクラス内の+=メソッド
  // ほとんどのケースは、要素の追加完了後にtoListを行なうので、下記のコストがかかるコピー処理は不要となる。
  /*
  override def += (x: T) {
    if (exported) copy()
    // 最新のリストを使う為に、toListメソッドが使用された際の「exported」がtrueならば、
    // startが示す最新のリストをコピーする。
    if (start.isEmpty) {
      last0 = new scala.::(x, Nil)
      start = last0
    } else {
      val last1 = last0
      last0 = new scala.::x, Nil)
      last1.tl = last0
    }
  }
   */

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 関数型の見かけ
object Sp22_04 {
  // Listは「見かけ上」は純粋関数型だが、「内実」はListBufferを使った命令型実装になっている。
  // Scalaのリストでは内部的にはミュータブルでも外からみたらイミュータブルという実装になっている

  val xs = List(3, 4, 5)
  xs
  val ys = 1 :: xs
  ys.tail
  val zs = 2 :: xs
  zs.tail
  // 両方のtailは同じデータ構造を指し示している。処理効率が上がっている。

  println(ys.drop(2)) // OK
  println(ys.drop(2).tail) // OK
  //println(ys.drop(2).tail = Nil) // Error: value tail_= is not a member of List[Int]

  // :: は、リストの先頭に要素を少しずつ追加していく
  // ListBufferは、末尾に要素を追加していく

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}
