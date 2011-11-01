package com.example.scalaprog

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/10/17
 * Time: 16:19
 * To change this template use File | Settings | File Templates.
 */

// 第19章 型のパラメータ化
object ScalaProg19

// 関数型待ち行列
object Sp19_01 {
  //val q = Queue(1, 2, 3)

  class SlowAppendQueue[T](elems: List[T]) { // Not efficient
    def head = elems.head // headは速い
    def tail = new SlowAppendQueue(elems.tail)  // tailは速い
    def enqueue(x: T) = new SlowAppendQueue(elems ::: List(x))  // 待ち行列が比例して、最後尾への連結は遅い
    override def toString = elems mkString ("SlowAppendQueue(", ", ", ")")
  }
  //val q1 = new SlowAppendQueue(Nil) enqueue 1 enqueue 2
  val q1 = new SlowAppendQueue(List(1, 2, 3)) enqueue 4 enqueue 5
  println(q1)

  class SlowHeadQueue[T](smele: List[T]) {
    def head = smele.last // lastは遅い
    def tail = new SlowHeadQueue(smele.init)  // initは遅い
    def enqueue(x: T) = new SlowHeadQueue(x :: smele) // 先頭への連結は速い
    override def toString = smele.reverse mkString ("SlowHeadQueue(", ", ", ")")
  }
  //val q2 = new SlowHeadQueue(Nil) enqueue 1 enqueue 2
  val q2 = new SlowHeadQueue(List(1, 2, 3).reverse) enqueue 4 enqueue 5
  println(q2)

  class Queue[T](
    private val leading: List[T], // 前から順に要素を並べる ... mirrorがコールされた時にtrailing.reverseを渡す
    private val trailing: List[T] // 待ち行列の末尾から逆順に要素を並べる
  ) {
    private def mirror =  // Queueクラスを返す
      if (leading.isEmpty)
        new Queue(trailing.reverse, Nil)
        // trailingリスト全体の要素を反転し、leadingにコピー,leadingはisEmpty:Trueなので、Nil
        // 理由：head,tail処理を行なう際、leadingに寄せておく方が、処理が早くなる。
      else
        this  // leadingに初期値リストが代入されているので、なにもせず、Queueクラスを返すのみ
    def head = mirror.leading.head
    def tail = {
      val q = mirror
      // 同期をとった後、正順リストのtailと、
      // そのままの逆順リストを返す
      new Queue(q.leading.tail, q.trailing)
    }
    def enqueue(x: T) =
      new Queue(leading, x :: trailing)
    override def toString =
      leading ::: trailing.reverse mkString ("Queue(", ", ", ")")
  }
  object Queue {
    // constructs a queue with initial elements `xs'
    // 「xs: T*」連続パラメータ ... P162
    def apply[T](xs: T*) = new Queue[T](xs.toList, Nil) // leadingに(1, 2, 3)toListで追加する。
  }
  //val q3 = Queue[Int]() enqueue 1 enqueue 2
  val q3 = Queue[Int](1, 2, 3) enqueue 4 enqueue 5  // enqueueは、trailingの先頭に追加する。
  // leading=List(1,2,3), trailing=List(5,4)
  println(q3)
  println(q3.head)
  println(q3.tail)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 情報隠蔽
// 非公開コンストラクターとファクトリーメソッド
object Sp19_02_01 {
  class QueueP[T] private (
      private val leading: List[T],
      private val trailing: List[T]
  )
  //new QueueP(List(1, 2), List(3)) // コンストラクタへアクセスできない。

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 非公開クラスというもう一つの方法
object Sp19_02_02 {
  trait Queue[T] {  // trait ... P.214
    def head :T
    def tail: Queue[T]
    def enqueue(x: T): Queue[T]
  }
  object QueueObj {    // traitのQueueと同名の必要性はない。
    def apply[T](xs: T*): Queue[T] =
      new QueueImpl[T](xs.toList, Nil)
    private class QueueImpl[T](
      private val leading: List[T],
      private val trailing: List[T]
    ) extends Queue[T] {  // Queueトレイトのミックスイン
      def mirror =
        if (leading.isEmpty)
          new QueueImpl(trailing.reverse, Nil)
        else
          this
      def head: T = mirror.leading.head
      def tail: QueueImpl[T] = {
        val q = mirror
        new QueueImpl(q.leading.tail, q.trailing)
      }
      def enqueue(x: T) =
        new QueueImpl(leading, x :: trailing)
      override def toString =
        (leading ::: trailing.reverse) mkString ("Queue(", ", ", ")")
    }
  }
  val q = QueueObj(1,2,3) enqueue 4 enqueue 5

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 変位指定アノテーション
object Sp19_03 {
  trait Queue[T] {  // trait ... P.214
    def head :T
    def tail: Queue[T]
    def enqueue(x: T): Queue[T]
  }
  //def doesNotCompile(q: Queue) {} // Queueはトレイトであって型ではない。
  //<console>:6: error: trait Queue takes type parameters
  //           def doesNotCompile(q: Queue) {}
  //                                 ^
  def doesCompile(q: Queue[AnyRef]) {}
  // Queueトレイトは、パラメータされた型を指定できる。

  /*
  class Cell[+T](init: T) {  // [T}:非変の型パラメータ、[+T]:共変の型パラメータ、[-T]:反変の型パラメータ
    private[this] var current = init  // private[this] = 内部変数 private[]=限定子付きアクセス修飾子 P.242
    def get = current
    def set(x: T) { current = x } // Cell[+T]共変の場合 ... error: covariant type T occurs in contravariant position in type T of value x
    // setメソッドが場合によってはサブ型の関係性を保持できない為
  }

  val c1 = new Cell[String]("abc")
  val c2: Cell[Any] = c1  // Cell[T]非変の場合 ... error: type mismatch
  c2.set(1)
  val s: String = c1.get
   */

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 変位指定と配列
object Sp19_03_01 {

  val a1 = Array("abc")
  //val a2: Array[Any] = a1 // error: type mismatch
  val a2: Array[Object] = a1.asInstanceOf[Array[Object]]

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 変位指定アノテーションのチェック
object Sp19_04 {
  /*
  trait Queue[+T] {  // trait ... P.214
    def enqueue(x: T) = {}  // Cell[+T]共変の場合 ... error: covariant type T occurs in contravariant position in type T of value x
  }

  class StrangeIntQueue extends Queue[Int] {
    override def enqueue(x: Int) = {
      println(math.sqrt(x))
      super.enqueue(x)    // super スーパークラスのメンバ変数、メソッド、コンストラクタを参照する際に使用 ... 参考 P.223
    }
  }

  val x:Queue[Any] = new StrangeIntQueue
  // 共変を定義したQueueクラスでもInt型にString型を入れてしまうという型の問題が発生
  x.enqueue("abc")
   */

  // 再代入可能なフィールドにはメソッドパラメータ型として＋アノテーションの付与された型パラメータを使えない
  // セッターメソッドのフィールド型Tが存在するため

  // 型パラメータに変位指定アノテーション[型パラメータの横に付ける+や-]を記載するとCompilerのチェック対象となる。

  abstract class Cat[-T, +U] {
    def meow[W](volume: T,  listerner: Cat[U, T])
      : Cat[Cat[U, T], U]
  }

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 下限限界(lower bounds)
object Sp19_05 {
  class Queue[+T] (private val leading: List[T], private val trailing: List[T]) {
    def enqueue[U >: T](x: U) = new Queue[U](leading, x :: trailing)
  }

  // http://d.hatena.ne.jp/Naotsugu/20100503/1272999352
  //通常版
  class Cons1[A](val head: A, val trailing: List[A]) {
    def append(x: A) = new Cons1[A](x, head :: trailing)
    override def toString = (head :: trailing).toString
  }
  val c1 = new Cons1(1, 2 :: Nil)
  println(c1) // List(1, 2)
  println(c1.append(0)) // List(0, 1, 2)
  //c1はIntで型パラメータ化されている為、Double 値や String 値を append しようとするとコンパイルエラーとなる。
  //println(c1.append(0.5))    // エラー
  //println(c1.append("foo"))  // もちろんエラー

  // 下限限界を設定
  class Cons2[A](val head: A, val trailing: List[A]) {
    def append[U >: A](x: U) = new Cons2[U](x, head :: trailing)
    override def toString = (head :: trailing).toString
  }
  val c2 = new Cons2(1, 2 :: Nil)
  println(c2) // List(1, 2)
  println(c2.append(0)) // List(0, 1, 2)
  // スーパークラスで再定義されたオブジェクトが得られる。
  println(c2.append(0.5))    // OK
  println(c2.append("foo"))  // OK

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 反変(contravariance)
object Sp19_06 {
  // 反変の出力チャネル ... AnyRef（要求少ない）の出力チャネルは、String（要求多い）の出力チャネルのサブ型になる。
  trait OutputChannel[-T]{
    def write(x:T)
  }

  // Functionalに含まれる共変と反変
  trait Functional1[-S, +T] {
    def apply(x: S): T
  }

  // 関数型パラメータの変位指定のサンプル
  class Publication(val title: String)  // Publication=出版物
  class Book(title: String) extends Publication(title)
  class Dic(title: String) extends Publication(title)

  object Library {
    val books: Set[Book] =
      Set(
        new Book("Programming in Scala"),
        new Book("Walden")
      )
      val dics: Set[Dic] =
        Set(
          new Dic("JP-EN"),
          new Dic("EN-JP")
        )
    //                      子:反変[-]  親:共変[+]
    def printBookList(info: Book => AnyRef) { // Customer.getTitleのPublication => Stringは、Book => AnyRefのサブ型
      for (book <- books) println(info(book)) // info関数=Customer.getTitle、book=Book型
    }
    def printDicList(info: Dic => AnyRef) {
      for (dic <- dics) println(info(dic))
    }
  }
  object Customer extends Application {
    //              親            子（サブ型）
    def getTitle(p: Publication): String = p.title
    Library.printBookList(getTitle)
    Library.printDicList(getTitle)
  }
  // 実行
  Customer

  // 無印 [T}                   :非変の型パラメータ
  // [+T] or functional 戻り値] :共変の型パラメータ
  // [-T] or functional[引数    :反変の型パラメータ

  // 不変・共変・反変の例 ... http://d.hatena.ne.jp/Naotsugu/20100502/1272827754
  // 不変
  class Nonvariant[A]
  def func1(arg:Nonvariant[Number]) {println(arg)}
  //func1(new Nonvariant[AnyRef])  // エラー
  func1(new Nonvariant[Number]) // OK
  //func1(new Nonvariant[BigInt])  // エラー

  // 共変 ... 子に渡せる
  class Covariant[+A]
  def func2(arg:Covariant[Number]) {println(arg)}
  //func2(new Covariant[AnyRef])  // エラー
  func2(new Covariant[Number])  // OK
  func2(new Covariant[BigInt]) // OK

  // 反変 .. 親に渡せる
  class Contravariant[-A]
  def func3(arg:Contravariant[Number]) {println(arg)}
  func3(new Contravariant[AnyRef])  // OK
  func3(new Contravariant[Number])  // OK
  //func2(new Contravariant[BigInt]) // エラー

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// オブジェクト非公開データ
object Sp19_07 {
  class Queue[+T] private (
    private[this] var leading: List[T], // private[this] = 内部変数 private[]=限定子付きアクセス修飾子 P.242
    private[this] var trailing: List[T]
  ) {
    private def mirror() =
      if (leading.isEmpty) {
        while (!trailing.isEmpty) {
          leading = trailing.head :: leading
          trailing = trailing.tail
        }
      }
    def head: T = {
      mirror()
      leading.head
    }
    def tail: Queue[T] = {
      mirror()
      new Queue(leading.tail, trailing)
    }
    def enqueue[U >: T](x: U) =
      new Queue[U](leading, x :: trailing)

    def this(xs: T*) = this(xs.toList, Nil) // leadingに(1, 2, 3)toListで追加する。
    override def toString =
      leading ::: trailing.reverse mkString ("Queue(", ", ", ")")
  }

  // enqueueは、下限限界設定なので、スーパークラスで再定義されたオブジェクト（Queue[T]）により、
  // DoubleやStringも追加できる。
  val q = new Queue[Int](1, 2, 3) enqueue 0.5 enqueue "foo"  // enqueueは、trailingの先頭に追加する。
  println(q)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 上限境界(upper bounds)
object Sp19_08 {
  class Person(val firstName: String,  val lastName: String)
    extends Ordered[Person] { // Orderdトレイト ... P.220
    def compare(that: Person) = {
      // lastNameの大小を一文字単位で比較し、同一だった場合はfirstNameで更に比較する。
      // compareToIgnoreCase = 大文字と小文字の区別なしで、2 つの文字列を辞書的に比較します。
      // 大=-1 小=1 同=0
      val lastNameComparison = lastName.compareToIgnoreCase(that.lastName)
      if (lastNameComparison != 0)
        lastNameComparison
      else
        firstName.compareToIgnoreCase(that.firstName)
    }
    override def toString = firstName + " " + lastName
  }
  var robert = new Person("Robert", "Jones")
  val sally = new Person("Sally", "Smith")
  println(robert + "|" + sally)
  println(robert < sally)

  val people = List(
    new Person("Larry", "Wall"),
    new Person("Ala2n", "Kay"),
    new Person("Anders", "Hejlsberg"),
    new Person("Alan", "Ka2y"),
    new Person("Guido", "van Rossum"),
    new Person("Ala1n", "Kay"),
    new Person("Yukihiro", "Matsumoto"),
    new Person("Alan", "Ka1y")
  )
  println(people)

  // 上限限界を指定しているマージソート関数 ... P.303参考
  // PersonクラスがOrderedトレイトをミックスインしていないと下記エラーが出る。
  // inferred type arguments [Person] do not conform to method orderdMergeSort's type parameter bounds [T <: Ordered[T]]
  def orderdMergeSort[T <: Ordered[T]](xs: List[T]): List[T] = {
    def merge(xs: List[T], ys: List[T]): List[T] =
      (xs, ys) match {
      case (Nil, _) => ys
      case (_, Nil) => xs
      case (x :: xs1, y :: ys1) =>
        // (x < y)の処理は、先頭要素が大きい（Orderedトレイト機能）方をとって、更に再帰でマージ処理する
        if (x < y) x :: merge(xs1, ys)
        else y :: merge(xs, ys1)
      }
    val n = xs.length / 2
    if (n == 0) xs
    else {
      val (ys, zs) = xs splitAt n
      merge(orderdMergeSort(ys), orderdMergeSort(zs))
    }
  }

  val sortedPeople = orderdMergeSort(people)
  println(sortedPeople)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}
