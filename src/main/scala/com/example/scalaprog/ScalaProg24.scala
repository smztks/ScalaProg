package com.example.scalaprog

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/10/23
 * Time: 13:51
 * To change this template use File | Settings | File Templates.
 */

// 第24章 ScalaコレクションAPI
object ScalaProg24

// コレクションの一貫性
object Sp24_02 {
  /*
  コレクションクラスの階層構造
  Traversable
    iterable
      Seq
        IndexedSeq
          Vector
          ResizableArray
          GenericArray
        LinerSeq
          MutableList
          List
          Stream
        Buffer
          ListBuffer
          ArrayBuffer
      Set
        SortedSet
          TreeSet
        HashSet (mutable)
        LinkedHashset（HashSetの順序付き版）
        Hashset (immutable)
        BitSet
        EmptySet, Set1, Set2, Set3, Set4
      Map
        SortedMap
          TreeMap
        HashMap (mutable)
        LinkedHashMap (mutable)
        HashMap (immutable)
        EmptyMap, Map1, Map2, Map3, Map4
   */

  object Color extends Enumeration {
    val Red, Green, Blue = Value
  }
  val x, y, z, a, b, c = Nil

  import scala.collection.immutable
  import scala.collection.mutable

  Traversable(1, 2, 3)                      // 不要
  Iterable("x", "y", "z")                   // 不要
  Map("x" -> 24,  "y" -> 25, "z" -> 26)     // 不要
  Set(Color.Red, Color.Green, Color.Blue)   // 不要
  immutable.SortedSet("hello", "world")     // immutable
  mutable.Buffer(x, y, z)                     // mutable
  IndexedSeq(1.0, 2.0)                      // 不要
  immutable.LinearSeq(a, b, c)                // immutable / mutable

  List(1, 2, 3)                               // 不要
  immutable.HashMap("x" -> 24, "y" -> 25, "z" -> 26)    // immutable / mutable

  println(List(1, 2, 3) map (_ + 1))
  println(Set(1, 2, 3) map (_ * 2))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// Traversableトレイト ... Traversable=横断できる、通過できる
object Sp24_03 {
  val xs = List(10, 20, 30)
  val ys = List("ab", "cd", "ef")
  val zs = List(1 -> 'a', 2 -> 'b', 3 -> 'c')

  println("### 抽象メソッド ###")
  xs foreach (x => print(x + ","))

  println("### 加算・追加 ###")
  println(xs ++ ys)

  println("### マップ ###")
  ys map (x => print(x + ","))
  println(ys flatMap (x => x.toUpperCase))
  println(xs collect { case 10 => 15 case 20 => 25 case x => x + 5 })

  println("### 変換 ###")
  println(xs.toArray.apply(0))
  println(xs.toList)
  println(xs.toIterable)
  println(xs.toSeq)
  println(xs.toIndexedSeq)
  println(xs.toStream)
  val xs2 = List(10, 20, 30, 10, 20, 30)
  println(xs2.toSet)
  println(zs.toMap)

  println("### コピー ###")
  import scala.collection.mutable.ListBuffer
  val buf = new ListBuffer[Int]
  xs copyToBuffer buf
  println(buf)

  val arr = new Array[Int](5)
  xs copyToArray(arr, 1, 4)
  println(arr.toList)

  println("### サイズ情報 ###")
  println(xs.isEmpty)
  println(xs.nonEmpty)
  println(xs.size)
  println(xs.hasDefiniteSize)

  println("### 要素取得 ###")
  println(xs.head)
  def show(x: Option[Int]) =
    x match {
      case Some(s) => s
      case None => "?"
    }
  println(show(xs.headOption))
  println(xs.last)
  println(show(xs.lastOption))
  println(show(xs find (x => x > 15)))

  println("### サブコレクション取得 ###")
  println(xs.tail)
  println(xs.init)
  println(xs slice (1, 2))
  println(xs take 1)
  println(xs drop 1)
  println()

  println(xs takeWhile (x => x < 15))
  println(xs dropWhile (x => x < 15))
  println(xs filter (x => x > 15))
  println(xs withFilter (x => x > 15) map (x => x + 5))
  println(xs filterNot (x => x > 15))

  println("### 分割 ###")
  println(xs splitAt 2)
  println(xs span (x => x < 15))
  println(xs partition (x => x > 15))
  println(xs groupBy (x => x > 15))

  println("### 要素条件 ###")
  println(xs forall (x => x > 5))
  println(xs exists (x => x > 25))
  println(xs count (x => x > 15))

  println("### 畳み込み ###")
  println((18000 /: xs)(_ / _)) // ((18000/10)/20)/30 ... 18000/10=1800, 1800/20=90, 90/30=3
  val xs3 = List(80, 40, 10)
  println((xs3 :\ 2)(_ / _))  // 80/(40/(10/2)) ... 10/2=5, 40/5=8, 80/8=10

  println(xs.foldLeft(18000)(_ / _))
  println(xs3.foldRight(2)(_ / _))

  // cf. http://d.hatena.ne.jp/tamura70/20100512/scala
  println(xs.reduceLeft (_ - _))  // (10-20)-30 = -40
  println(xs.reduceRight (_ - _)) //  10-(20-30) = 20

  println("### 型限定の畳み込み ###")
  println(xs.sum)
  println(xs.product)
  println(xs.min)
  println(xs.max)

  println("### 文字列 ###")
  val buf2 = new StringBuilder
  ys addString (buf2, "[", "-", "]")
  println(buf2)
  println(ys mkString("[", "-", "]"))
  println(ys.stringPrefix)
  val xs4 =Map("USD" -> 1.0, "EUR" -> 0.7596, "JPY" -> 1.211, "CHF" -> 1.223)
  println(xs4.stringPrefix)

  println("### ビュー ###")
  println(xs.view)
  //println(xs view (1, 2))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// Iterbleトレイト
object Sp24_04 {
  val xs = List(10, 20, 30)
  val ys = List("ab", "cd", "ef")

  println("### 抽象メソッド ###")
  val it = xs.iterator
  println(it.next())
  println(it.next())

  println("### その他のイテレーター ###")
  val git = xs grouped 2
  println(git.next())
  println(git.next())

  val sit = xs sliding 2
  println(sit.next())
  println(sit.next())

  println("### サブコレクション ###")
  println(xs takeRight 1)
  println(xs dropRight 1)

  println("### ジッパー ###")
  println(xs zip ys)

  val xs2 = List(10, 20, 30, 40)
  println(xs zipAll (xs2, 5, 15))
  println(xs2 zipAll (xs, 5, 15))

  println(xs.zipWithIndex)

  println("### 比較 ###")
  val xs3 = List(10, 20, 30)
  println(xs sameElements xs3)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// シーケンストレイトSeq,IndexedSeq,LinearSeq
object Sp24_05 {
  val xs = List(10, 20, 30, 1, 10, 20)
  val ys = List(10, 20)

  println("### 添字参照、長さ ###")
  println(xs(3))
  println(xs isDefinedAt 3)
  println(xs.length)
  println(xs lengthCompare 10)
  println(xs.indices)

  println("### 添字による検索 ###")
  println(xs indexOf 10)
  println(xs lastIndexOf 10)

  println(xs indexOfSlice ys)
  println(xs lastIndexOfSlice ys)

  println(xs indexWhere (x => x > 25))
  println(xs segmentLength ((x => x > 15), 1))

  println(xs prefixLength (x => x > 5))

  println("### 加算・追加 ###")
  val x = 5
  println(x +: xs)
  println(xs :+ x)
  println(xs padTo (10, x))

  println("### 更新 ###")
  println(xs patch (2, ys, 2))
  println(xs updated (0, x))

  import scala.collection.mutable
  val xs2 = mutable.Seq(10, 20, 30)
  xs2(0) = x
  println(xs2.toList)

  println("### ソート ###")
  println(xs.sorted)
  println(xs sortWith (_.toString.head < _.toString.head))
  println(xs sortBy (x => x * -1))

  println("### 逆順 ###")
  println(xs.reverse)

  val it = xs.reverseIterator
  println(it.next())
  println(it.next())

  println(xs reverseMap (x => x > 15))
  xs reverseMap (x => print(x + ","))
  println(xs reverseMap(x => x.toString.head))

  println("### 比較 ###")
  println(xs startsWith ys)
  println(xs endsWith ys)
  println(xs contains x)
  println(xs containsSlice ys)

  val xs3 = List(11, 21, 31, 2, 11, 21)
  println((xs corresponds xs3)(_ < _))

  println("### 集合間演算 ###")
  println(xs intersect ys)
  println(xs diff ys)
  println(xs union ys)
  println(xs distinct)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// バッファー
object Sp24_05_01 {
  import scala.collection.mutable.ListBuffer
  val buf = new ListBuffer[Int]
  val x, y, z = 1
  val xs = List(10, 20)
  //val xs = Array(10, 20)

  println("### 追加 ###")
  buf += x
  println(buf)

  buf += (x, y, z)
  println(buf)

  buf ++= xs
  println(buf)

  x +=: buf
  println(buf)

  xs ++=: buf
  println(buf)

  buf insert (0, x)
  println(buf)

  buf insertAll (5, xs)
  println(buf)

  println("### 削除 ###")
  buf -= x
  println(buf)

  buf remove 0
  println(buf)

  buf remove (1, 2)
  println(buf)

  buf trimStart 3
  println(buf)

  buf trimEnd 2
  println(buf)

  buf.clear()
  println(buf)

  println("### クローン作成 ###")
  buf += (x, y, z)
  val buf2 = buf.clone
  println(buf2)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 集合
object Sp24_06_a {
  val xs = Set(10, 20, 30)
  val xs2 = Set(5, 10, 15, 40, 50)
  val ys = Set(10, 20, 30, 10, 20)
  val ys2 = Set(40, 50)
  val x = 5
  val y = 10
  val z = 15

  println("### テスト ###")
  println(xs contains y)
  println(xs(y))
  println(xs subsetOf ys)

  println("### 追加 ###")
  println(xs + y)
  println(xs + (x, y, z))
  println(xs ++ ys2)

  println("### 削除 ###")
  println(xs - x)
  println(xs2 - (x, y, z))
  println(xs2 -- ys2)
  println(xs.empty)

  println("### 二項演算 ###")
  println(xs & ys)
  println(xs intersect ys)
  println(xs | ys2)
  println(xs union ys2)
  println(xs2 &~ ys2)
  println(xs2 diff ys2)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 集合(Set)
object Sp24_06_b {
  import scala.collection.mutable
  val xs = mutable.Set(10, 20, 30)
  val xs2 = Set(5, 10, 15, 40, 50)
  val ys = Set(10, 20, 30, 10, 20)
  val ys2 = Set(40, 50)
  val x = 5
  val y = 10
  val z = 15

  println("### 追加 ###")
  xs += x
  println(xs)

  xs += (x, y, z)
  println(xs)

  xs ++= ys2
  println(xs)

  println(xs add x)

  println("### 削除 ###")
  xs -= x
  println(xs)

  xs -= (x, y, z)
  println(xs)

  xs --= ys2
  println(xs)

  println(xs remove x)

  xs retain (x => x > 25)
  println(xs)

  xs.clear()
  println(xs)

  println("### 更新 ###")
  xs += x
  xs(y) = true
  xs.update(z, true)
  println(xs)

  println("### クローン作成 ###")
  val xs3 = xs.clone
  println(xs3)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// ソートされた集合 P.328参照
object Sp24_06_01 {
  // 文字列を逆順に追加するように指定
  val myOrdering = Ordering.fromLessThan[String](_ > _)

  // 上記順序に従う空のツリー集合を作る。
  import scala.collection.immutable.TreeSet
  TreeSet.empty(myOrdering)

  // 要素型のデフォルト順序を使う。順序引数を省略して要素型か空集合を指定できる。
  val set = TreeSet.empty[String]

  // ツリー集合から新しい集合を作る。新しい集合は元の集合と同じ順序を維持する。
  val numbers = set + ("1one", "2two", "3three", "4four", "5five")

  // rangeメソッドは、末尾要素を除く、指定範囲のすべての要素を返す。
  println(numbers range ("1one", "4four"))
  // TreeSet(1one, 2two, 3three)

  // fromメソッドは、集合の順序に基づいて開始要素以上の大きさとなる、すべての要素を返す。
  println(numbers from "3three")
  // TreeSet(3three, 4four, 5five)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// マップ immutable
object Sp24_07_a {
  val ms = Map(10 -> "ab", 20 -> "cd", 30 -> "ef")
  val ms2 = Map("AB" -> 1, "CD" -> 2, "EF" -> 3)
  val ms3 = Map(10 -> "ab")
  val k = 20
  val l = 30
  val m = 40

  println("### ルックアップ ###")
  println(ms get k)
  println(ms(k))
  println(ms getOrElse (m, "yz"))
  println(ms contains k)
  println(ms isDefinedAt k)

  println("### 追加と更新 ###")
  println(ms + (40 -> "gh"))
  println(ms + (40 -> "gh", 50 -> "ij"))
  println(ms ++ ms2)
  println(ms updated (40, "gh"))

  println("### 削除 ###")
  println(ms - k)
  println(ms - (k, l, m))
  println(ms -- ms.keys)
  println(ms -- Set(10, 20))

  println("### サブコレクション作成 ###")
  println(ms.keys)

  println(ms.keySet)

  val kit =ms.keysIterator
  println(kit.next())
  println(kit.next())

  println(ms.values)

  val vit = ms.valuesIterator
  println(vit.next())
  println(vit.next())

  println("### 変形 ###")
  println(ms filterKeys (x => x > 15))
  println(ms mapValues (x => x + "!"))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// マップ mutable
object Sp24_07_b {
  import scala.collection.mutable
  val ms = mutable.Map(10 -> "ab", 20 -> "cd", 30 -> "ef")
  val ms2 = Map(80 -> "qr")
  val k = 20
  val l = 30
  val m = 40

  println("### 追加と更新 ###")
  ms(40) = "ij"
  println(ms)

  ms += (50 -> "kl")
  println(ms)

  ms += (60 -> "mn", 70 -> "op")
  println(ms)

  ms ++= ms2
  println(ms)

  println(ms put (10, "AB"))
  println(ms)

  println(ms getOrElseUpdate (10, "uv"))
  println(ms getOrElseUpdate (90, "uv"))
  println(ms)

  println("### 削除 ###")
  ms -= k
  println(ms)

  ms -= (k, l, m)
  println(ms)

  ms --= Set(50, 60)
  println(ms)

  println(ms remove 10)
  println(ms)

  ms retain ((k, v) => k > 75)
  println(ms)

  ms.clear()
  println(ms)

  println("### 変形とクローン作成 ###")
  ms += (10 -> "ab", 20 -> "cd", 30 -> "ef")
  ms transform ((k, v) => v + "!")
  println(ms)

  val ms3 = ms.clone
  println(ms3)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// マップ mutable - getOrElseUpdate
object Sp24_07_c {
  def f(x: String) = {
    println("taking my time.")
    Thread.sleep(100)
    x.reverse
  }
  val cache = collection.mutable.Map[String, String]()
  // 第2引数は、名前渡しパラメータの為、第2引数の値を必要とするときに限って実行される。
  def cacheF(s: String) = cache.getOrElseUpdate(s, f(s))

  println("### 1st cachF call ###")
  println(cacheF("abc"))

  println("### 2nd cachF call ###")
  println(cacheF("abc"))

  println("### 3rd cachF call ###")
  println(cacheF("xyz"))

  println("### 4th cachF call ###")
  println(cacheF("abc"))
  println(cacheF("xyz"))

  println(cache)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 同期集合・マップ - 同期マップ
// cf. http://d.hatena.ne.jp/plasticscafe/20100901/1283313560
object Sp24_08_a {
  import scala.collection.mutable.{Map, SynchronizedMap, HashMap}

  object MapMaker {
    def makeMap: Map[String, String] = {
      // SynchronizedMapトレイトをミックスインしているので、複数のスレッドで同時に扱える。
      // 各スレッドからのマップへのアクセスは同期することになる。
      new HashMap[String, String] with SynchronizedMap[String, String] {
        override  def default(key: String) = "Why do you want to know?"
      }
    }
  }

  val capital = MapMaker.makeMap
  capital ++= List("US" -> "Washington", "France" -> "Paris", "Japan" -> "Tokyo")

  println(capital("Japan"))
  println(capital("New Zealand"))

  capital += ("New Zealand" -> "Wellington")
  println(capital("New Zealand"))

  println(capital)
  // Map(New Zealand -> Wellington, France -> Paris, US -> Washington, Japan -> Tokyo)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 同期集合・マップ - 同期集合
object Sp24_08_b {
  import scala.collection.mutable

  object SetMaker {
     val synchroSet = new mutable.HashSet[Int] with mutable.SynchronizedSet[Int]
  }
  //println("SetMaker.synchroSet [" + SetMaker.synchroSet + "]")

  val syncset = SetMaker.synchroSet
  syncset ++= List(10, 20, 30)
  println(syncset)
  println(syncset(10))
  println(syncset(40))

  //val tuple1 = (1, "hello", Console)
  //println("tuple1 [" + tuple1 + "]")

  syncset += (40, 50, 60)
  println(syncset)
  println(syncset(40))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 具象イミュータブルコレクションクラス
// リスト
object Sp24_09_01 {
  // リストは有限のイミュータブルシーケンス
  // リストについては、第16章と第22章を参照
}

// ストリーム Stream
object Sp24_09_02 {
  // ストリームはリストとよく似ているが、要素が遅延評価されるところが異なる。
  // ストリームは無限に長くすることができる。
  // 要求された要素だけが計算されることを除けば、ストリームのパフォーマンス特性はリストと同じ
  val str = 1 #:: 2 #:: 3 #:: Stream.empty
  println("str: "+ str)
  // str: Stream(1, ?)

  // フィボナッチ数列:個々の要素が前とその前の2個の要素の和となっている数列
  def fibFrom(a: Int, b: Int): Stream[Int] = a #:: fibFrom(b, a + b)
  val fibs = fibFrom(1, 1).take(7)
  println("fibs: " + fibs)
  // fibs: Stream(1, ?)
  println(fibs.toList)
  // List(1, 1, 2, 3, 5, 8, 13)

  // Streamを使わない場合
  def fibFrom2(a: Int, b: Int): List[Int] = a :: fibFrom2(b, a + b)
  // val fibs2 = fibFrom2(1, 1).take(7)
  // println(fibs2.toList)
  // Exception in thread "main" java.lang.StackOverflowError
  // fibForm2の無限再帰が起きるためにエラーとなる。#::を使った場合は、右辺は要求されるまで評価されない。

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// ベクター Vector
object Sp24_09_03 {
  // ベクターは、ランダムアクセス時の非効率性を解決するために Scala 2.8 から導入された新しいコレクション型
  // 先頭要素以外にも効率的にアクセスできる。
  val vec = scala.collection.immutable.Vector.empty
  val vec2 = vec :+ 1 :+ 2
  val vec3 = 100 +: vec2
  println(vec3)
  println(vec3(0))

  // 元のベクターとは要素が一個だけ異なる新しいベクターを作ることができる。
  val vec4 = Vector(1, 2, 3)
  println(vec4 updated (2, 4))
  println(vec4)

  // 高速のランダム選択と高速の関数的更新の間でうまくバランスが取られているため、
  // イミュータブルな添字付きシーケンスのデフォルト実装となっている。
  // 下記をインタプリタ(REPL=Read-Evaluate-Print Loop)で実行
  collection.immutable.IndexedSeq(10, 20, 30)
  // res15: scala.collection.immutable.IndexedSeq[Int] = Vector(10, 20, 30)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// イミュータブルスタック LIFO（後入れ先出し）
object Sp24_09_04 {
  val stack = scala.collection.immutable.Stack.empty
  val hasOne = stack.push(3, 2, 1) // pushを使ってスタックを要素にプッシュする。
  println(stack)
  println(hasOne)
  println(hasOne.top)  // topによりスタックの先頭を削除せずに参照する。
  println(hasOne.pop)  // popを使って要素をポップ（削除）する。
  println(hasOne)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// イミュータブルキュー FIFO（先入れ先出し）
object Sp24_09_05 {
  val empty = scala.collection.immutable.Queue[Int]()
  // enqueue = 待ち行列に入れる; キューに入れる; エンキュー; ENQ
  val has1 = empty.enqueue(1)
  val has123 = has1.enqueue(List(2, 3))
  println(has123)

  // dequeue = 待機解除する
  val (elment, has23) = has123.dequeue
  println(elment + "|" + has23)

  val (has2, has3) = has23.dequeue
  println(has2 + "|" + has3)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// レンジ（Range:範囲）
object Sp24_09_06 {
  println(1 to 3)
  // Range(1, 2, 3)
  println(5 to 14 by 3)
  // Range(5, 8, 11, 14)
  println(1 until  3)
  // Range(1, 2)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// ハッシュトライ(HashTrie)
object Sp24_09_07 {
  // Scalaのイミュータブルな集合とマップのデフォルト実装として使われている。
  // 要素が1個から4までの集合とマップは、要素（マップの場合はキー/値のペア）をフィールドとして
  // 格納する1個のオブジェクトとして格納される。

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 赤黒木 TreeSet,TreeMap P.328を参照
object Sp24_09_08 {
  // 内部に赤黒木を使った集合とマップの実装を提供している。それらには、TreeSetやTreeMapという名前からアクセスする。
  // 赤黒木は、すべての要素をソートされた順序で返す効率的なイテレーターを提供するため、ScalaのSoterdSetの標準実装でもある。

  val set = collection.immutable.TreeSet.empty[Int]
  println(set + 1 + 3 + 3)
  // TreeSet(1, 3)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// イミュータブルビットセット (BitSet)
// cf. http://www.ne.jp/asahi/hishidama/home/tech/java/array.html#h_BitSet
object Sp24_09_09 {
  // ビットセットは、大きな整数のビットを使って小さな整数のコレクションを表現する。
  // 例えば、3,2,0を格納するビットセットは2進数の「1101」、10進数の「13」で表現できる。
  // 集合内の最大の整数が数百程度までなら、ビットセットは非常にコンパクトな表現になる。

  val bits = scala.collection.immutable.BitSet.empty
  val moreBits = bits + 3 + 2 + 0
  println(moreBits)     // BitSet(0, 2, 3)
  println(moreBits(3))  // true
  println(moreBits(1))  // false

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// リストマップ
object Sp24_09_10 {
  // リストマップは、キーと値のペアによる連結リストを使ってマップを表現する。
  val map = collection.immutable.ListMap(1 -> "one", 2 -> "two")
  println(map(2))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 具象ミュータブルコレクションクラス
// 配列バッファー (ArrayBuffer)
object Sp24_10_01 {
  val buf = collection.mutable.ArrayBuffer.empty[Int]
  buf += 1
  buf += 10
  val arr = buf.toArray
  println(arr + "|" + arr(1))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// リストバッファー (ListBuffer)
object Sp24_10_02 {
  val buf = collection.mutable.ListBuffer.empty[Int]
  buf += 1
  buf += 10
  val list = buf.toList
  println(list + "|" + list(1))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 文字列ビルダー (StringBuilder)
object Sp24_10_03 {
  val buf = new StringBuilder
  buf += 'a'
  buf ++= "bcdef"
  List('x', 'y', 'z') addString (buf, "[", "-", "]")
  val str = buf.toString
  println(str)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// （単方向）連結リスト
object Sp24_10_04 {
  // （単方向）連結リストは、nextポインタで連結されたノードから構成されるミュータブルシーケンスである。

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 双方向連結リスト (DoubleLinkedLIsts)
object Sp24_10_05 {
  // 双方向連結リスト(DoubleLinkedLIsts)は、next以外に、現在のノードの前の要素を指すprevという
  // ミュータブルフィールドを持つところが異なる。

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// ミュータブルリスト (MutableList)
object Sp24_10_06 {
  // MutableListは、1本の連結リストと、終端の空ノードを参照するポインタから構成される。
  // MutableListは、Scalaのmutable.LinearSeqの標準実装となっている。

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// キュー (Queue)
object Sp24_10_07 {
  val queue = new scala.collection.mutable.Queue[String]
  queue += "a"
  queue ++= List("b", "c")
  println(queue)

  // dequeue = 待機解除する
  queue.dequeue
  println(queue)

  queue.dequeue
  println(queue)

  queue.dequeue
  println(queue)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 配列シーケンス (ArraySeq)
object Sp24_10_08 {
  // 配列シーケンスは、要素をArray[AnyRef]に格納する固定サイズのミュータブルシーケンスである。
  // ScalaではArraySeqクラスを使って実装されている。

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// スタック (Stack)
object Sp24_10_09 {
  val stack = new scala.collection.mutable.Stack[Int]

  stack.push(1)
  println(stack)

  stack.push(2)
  println(stack)

  println(stack.top)

  stack.pop
  println(stack)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 配列スタック (ArrayStack)
object Sp24_10_10 {
  // ArrayStackは、ミュータブルスタックの代わりに使える実装で、必要に応じてサイズを変えられるArrayを基礎としている。

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// ハッシュテーブル (HashMap, HashSet)
object Sp24_10_11 {
  // ハッシュテーブルは、格納されるオブジェクトのハッシュコードがうもく散らばっていれば非常に高速である。
  // そのため、Scalaのミュータブル集合とマップは、デフォルトでハッシュテーブルを基礎としている。

  val map = collection.mutable.HashMap.empty[Int, String]

  map += (1 -> "make a web site")
  map += (3 -> "profit!")
  println(map(3))
  println(map contains 2)
  println(map(2))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 弱いハッシュマップ
object Sp24_10_12 {
  // 弱いハッシュマップは、ガベージコレクターがマップから格納されているキーへのリンクをたどらない特殊なハッシュマップ
  // Scalaの弱いハッシュマップは、Javaのjava.util.WeakHashMapに対するラッパーとして実装されている。

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 並列マップ (ConcurrentMap)
// Concurrent = 並列
// cf. http://www.javamex.com/tutorials/synchronization_concurrency_8_hashmap2.shtml
object Sp24_10_13 {

  import collection.JavaConversions._
  val l: java.util.concurrent.ConcurrentMap[Int, String] = {
    new java.util.concurrent.ConcurrentHashMap[Int, String](10);
  }
  val m: scala.collection.mutable.ConcurrentMap[Int, String] = l
  val k = 10
  val v = "ab"

  println("### 追加 ###")
  m putIfAbsent(k, v)
  println(m)

  println("### 削除 ###")
  m remove (k, v)
  println(m)

  println("### 更新 ###")
  m putIfAbsent(k, v)
  m replace (k, "ab", "cd")
  println(m)

  m replace (k, v)
  println(m)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// ミュータブルビットセット (BitSet)
object Sp24_10_14 {
  val bits = scala.collection.mutable.BitSet.empty
  bits += 1
  bits += 3
  println(bits)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 配列 (1)WrappedArray
object Sp24_11_a {
  val a1 = Array(1, 2, 3)
  val a2 = a1 map (_ * 3)
  val a3 = a2 filter (_ % 2 != 0)

  println(a1.toList)
  println(a2.toList)
  println(a3.toList)
  println(a3.reverse.toList)

  val seq: Seq[Int]  = a1
  // seq: Seq[Int] = WrappedArray(1, 2, 3)

  val a4: Array[Int] = seq.toArray
  println(a1 eq  a4)


  seq.reverse
  // res53: Seq[Int] = WrappedArray(3, 2, 1)

  val ops: collection.mutable.ArrayOps[Int] = a1
  // ops: scala.collection.mutable.ArrayOps[Int] = [I(1, 2, 3)
  ops.reverse
  // res54: Array[Int] = Array(3, 2, 1)

  intArrayOps(a1).reverse
  // res55: Array[Int] = Array(3, 2, 1)

  // ArrayOpsへの変換は、WrappedArrayへの変換よりも優先順位が高い。

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 配列 (2)ClassManifest
object Sp24_11_b {
  /*
  def evenElems[T](xs :Vector[T]): Array[T] = {
    val arr = new Array[T]((xs.length + 1) / 2)
    for (i <- 0 until xs.length by 2)
        arr(i / 2) = xs(i)
    arr
  }
  */
  // Error: cannot find class manifest for element type T
  // val arr = new Array[T]((xs.length + 1) / 2)
  //           ^

  // eventElemsの実際の型パラメーターが何であるかを実行時にヒントを与える為、
  // Scalaコンパイラーに暗黙のパラメーターとしてクラスマニフェストを要求するよう指示する。

  //def evenElems[T](xs :Vector[T])(implicit m:ClassManifest[T]): Array[T] = {
  def evenElems[T: ClassManifest](xs :Vector[T]): Array[T] = {
    val arr = new Array[T]((xs.length + 1) / 2)
    println(arr.toList)
    for (i <- 0 until xs.length by 2)  // by=～個毎飛ばし(=step)
        arr(i / 2) = xs(i)
    arr
  }
  val arr = evenElems(Vector(1, 2, 3, 4, 5))
  println(arr.toList)

  val arr2 = evenElems(Vector("this", "is", "a", "test", "run"))
  println(arr2.toList)

  val xs = Vector(1, 2, 3, 4, 5)
  // def wrap[U](xs: Vector[U]) = evenElems(xs)
  // Error: could not find implicit value for evidence parameter of type ClassManifest[U]
  // def wrap[U](xs: Vector[U]) = evenElems(xs)
  //                                        ^

  def wrap[U: ClassManifest](xs: Vector[U]) = evenElems(xs) // OK
  // wrap: [U](xs: Vector[U])(implicit evidence$1: ClassManifest[U])Array[U]

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 文字列
object Sp24_12 {
  val str = "hello !!!"
  println(str.reverse)
  println(str.map(_.toUpper))
  println(str drop 3)
  println(str slice (1, 4))
  println(str slice (2, 7))
  val s: Seq[Char] = str
  // s: Seq[Char] = WrappedString(h, e, l, l, o,  , !, !, !)
  println(s)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// パフォーマンス特性
object Sp24_13 {
  // excel scala2.8.1-collection-api.xlsを参照

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 等価性 (1)
object Sp24_14_a {
  import collection.mutable.{HashMap, ArrayBuffer}
  val buf = ArrayBuffer(1, 2, 3)
  // buf: scala.collection.mutable.ArrayBuffer[Int] = ArrayBuffer(1, 2, 3)
  val map = HashMap(buf -> 3)
  // map: scala.collection.mutable.HashMap[scala.collection.mutable.ArrayBuffer[Int],Int] = Map((ArrayBuffer(1, 2, 3),3))
  println(map(buf))
  // 3

  buf(0) += 1
  // println(map(buf))
  // Error: Caused by: java.util.NoSuchElementException: key not found: ArrayBuffer(2, 2, 3)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 等価性 (2)
object Sp24_14_b {
  import collection.mutable.{HashMap, ArrayBuffer}
  val buf1 = ArrayBuffer(1, 2, 3)
  val buf2 = ArrayBuffer(2, 2, 3)
  // buf1: scala.collection.mutable.ArrayBuffer[Int] = ArrayBuffer(1, 2, 3)
  val map = HashMap(buf1 -> 10, buf2 -> 20)
  // map: scala.collection.mutable.HashMap[scala.collection.mutable.ArrayBuffer[Int],Int] = Map((ArrayBuffer(2, 2, 3),20), (ArrayBuffer(1, 2, 3),10))3))
  println(map(buf1))
  // 10

  buf1(0) += 1
  println(map(buf1))
  // 20

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// ビュー (1)
object Sp24_15_a {
  /*
  def lazyMap[T, U](coll: Iterable[T], f: T => U) = new Iterable[U] {
    def iterator = coll.iterator map f
  }
  */

  val v = Vector(1 to 10: _*)
  // v: scala.collection.immutable.Vector[Int] = Vector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

  // v map (_ + 1)という式は新しいベクターを作り出し、そのベクターはmap (_ * 2)という呼び出しによって
  // 第3のベクターに変換（トランスフォーム）される。
  v map (_ + 1) map (_ * 2)
  // res6: scala.collection.immutable.Vector[Int] = Vector(4, 6, 8, 10, 12, 14, 16, 18, 20, 22)

  // 中間的な結果を作るのを避けるためのもっと汎用的な方法は、ベクターをまずビューに変換し、
  // すべての変換をビューに適用してから、最後にビューをベクターに戻す。
  (v.view map (_ + 1) map (_ * 2)).force
  // res7: Seq[Int] = Vector(4, 6, 8, 10, 12, 14, 16, 18, 20, 22)

  // 以下は、「(v.view map (_ + 1) map (_ * 2)).force」の演算を1つ1つに分ける。
  // v.viewを実行する、遅延評価されるSeqすなわちSeqViewが作られる。
  // 第1パラメーターのIntはビューの要素の型を示す。
  // 第2野パラメーターのVector[Int]は、ビューにforceを適用したときに返されるコンストラクターを示す。
  val vv = v.view
  // vv: java.lang.Object with scala.collection.SeqView[Int,scala.collection.immutable.Vector[Int]] = SeqView(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

  // mapの結果は、SeqViewM(...)と表示される値になっている。ベクターvに対して、(_ + 1)関数を引数とする
  // mapを適用する必要があることを記録するラッパーである。そのmapは、ビューにforceを適用するまで実行されない。
  // SeqViewの後ろのMは、ビューがmap演算をカプセル化していることを示す。
  val res1 = vv map (_ + 1)
  // res8: scala.collection.SeqView[Int,Seq[_]] = SeqViewM(...)

  // 2つのmap演算を含むSeqViewが返されているので、Mの数も2個になってSeqViewMM(...)となっている。
  val res2 = res1 map (_ * 2)
  // res10: scala.collection.SeqView[Int,Seq[_]] = SeqViewMM(...)

  // force演算の一部として格納されている2個の関数が実行され、新しいベクターが構築される。
  // こうすれば、中間データ構造は不要となる。
  // 最終結果の静的な型はSeqであり、Vectorではないことである。最初の遅延mapが適用された結果値は、
  // 静的な型のSeqViewM[Int, Seq[_]]になっている。つまり、viewがVectorという特定のシーケンス型に
  // 適用されたという「知識」は失われているのである。
  res2.force
  // res12: Seq[Int] = Vector(4, 6, 8, 10, 12, 14, 16, 18, 20, 22)

  // ビューを使うことを検討するとよいという理由は2つある。
  // 1. パフォーマンス…コレクションをビューに変換すると、中間結果を作らずに済む。
  // 2. シーケンス固有の利点…ミュータブルシーケンスのビューに適用できるトランスフォーマー関数の多くは、
  // 元のシーケンスの一部を覗くウィンドウを提供する。


  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// ビュー (2)パフォーマンス
object Sp24_15_b{
  def isPalindrome(x: String) = x == x.reverse  // x == x.reverse ... 回文になっているかの真偽値を返す。
  def findPalindrome(s: Seq[String]) = s find isPalindrome

  val words = Seq("ab", "cd", "ef", "gh", "ij", "kl", "ll")

  // 下記方法だと、中間シーケンスが作成されてしまう。
  println(findPalindrome(words take 7))
  // take = xsの最初のn個の要素（順序が定義されていない場合には、任意のn個の要素）から構成されるコレクションを返す。

  // 中間シーケンスは作らず、軽量のビューオブジェクトを1つ作るだけである。
  println(findPalindrome(words.view take 7))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// ビュー (3)ミュータブシーケンス固有の利点
object Sp24_15_c{
  val arr = (0 to 9).toArray
  // subarrビューは、それらの要素をコピーせず、ただそれらの要素に対する参照を提供する。
  val subarr = arr.view.slice(3, 6)
  // subarr: scala.collection.mutable.IndexedSeqView[Int,Array[Int]] = SeqViewS(...)

  println(arr.toList)
  println(subarr.toList)

  def negate(xs: collection.mutable.Seq[Int]) =
    for (i <- 0 until xs.length) xs(i) = -xs(i)

  // arrの要素の断片であるsubarrのすべての要素を書き換えている。
  negate(subarr)
  println(arr.toList)

  // メソッドを適用した添字範囲はどれかという問いと、どのメソッドを適用すべきかという問いを切り分けてくれる。

  // Scala 2.8コレクションライブラリーでは、より規則正しいルールを作った。
  // ストリームビューを除くすべてのコレクションは正格である。
  // 正格なコレクションを遅延的なコレクションに変換する方法は、viewメソッド呼び出しだけに限定する。
  // 遅延的なコレクションから正格なコレクションに戻す方法は、forceだけに限定する。
  // 正格 = 規則の正しいこと。また規則にあてはまっていること。

  // 遅延評価による奇妙な動作を避けるためには、ビューを使うシナリオを2つに絞るべきである。
  // 1. コレクションの変換が副作用を持たない純粋関数型コード
  // 2. 変換が明示的に行われるミュータブルコレクション
  // 避けるべきは、新しいコレクションを作成すると同時に副作用を持っているような操作でビューを併用すること。

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// イテレーター (1)
object Sp24_16_a{
  val xs = List(1, 2, 3)
  // iteratorは、一度nextで移動すると、位置情報が維持されるので、whilte,foreach,for用に2個用意する。
  val it1, it2, it3 = xs.iterator

  println("Iterator - while(1)")
  while (it1.hasNext)
    println(it1.next())
  println("Iterator - while(2)")
  while (it1.hasNext)
    println(it1.next())

  println("Iterator - foreach(1)")
  it2 foreach println
  println("Iterator - foreach(2)")
  it2 foreach println

  println("Iterator - for(1)")
  for (elem <- it3) println(elem)
  println("Iterator - for(2)")
  for (elem <- it3) println(elem)

  println("use map & next")
  val it4 = Iterator("a", "number", "of", "words")
  val res = it4.map(_.length)
  res foreach println
  // it4.next()
  // Error: java.util.NoSuchElementException: next on empty iterator

  println("use dropWhile & next")
  val it5 = Iterator("a", "number", "of", "words")
  it5 dropWhile (_.length < 2)
  it5.next()
  // res24: java.lang.String = number

  val ys = List(10, 20, 30)
  val itA = ys.iterator
  val (itB, itC) = itA.duplicate

  println("Iterator - while(B)")
  while (itB.hasNext)
    println(itB.next())

  println("Iterator - while(C)")
  while (itC.hasNext)
    println(itC.next())

  println("Iterator - while(A)")
  while (itA.hasNext)
    println(itA.next())

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// イテレーター (2)
object Sp24_16_b{
  val xs = List(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120)
  val it, it2, it3, it4, it5, it6, it7, it8, it9, it10,
    it11, it12, it13, it14, it15, it16, it17, it18, it19, it20,
    it21, it22, it23, it24, it25 = xs.iterator

  println("### 抽象メソッド ###")
  println(it.next())
  println(it.hasNext)

  println("### 他のイテレーター ###")
  val bit = it.buffered
  println(bit.head + "|" + bit.next())

  val git = it grouped 2
  println(git.next())
  println(git.next())

  val sit = it sliding 2
  println(sit.next())
  println(sit.next())

  println("### コピー ###")
  import scala.collection.mutable.ListBuffer
  val buf = new ListBuffer[Int]
  it copyToBuffer buf
  println(buf)

  val arr = new Array[Int](5)
  it2 copyToArray(arr, 1, 3)
  println(arr.toList)

  println("### クローン作成 ###")
  val (dit1, dit2) = it2.duplicate  // 2個のクローンのみ有効
  println(dit1.next() + "|" + dit2.next() + "|" + it2.next())

  println("### 追加・加算 ###")
  val ait = dit1 ++ dit2
  for (elem <- ait) print(elem + ",")
  println()

  val pit = it3 padTo (15, 1)
  for (elem <- pit) print(elem + ",")
  println()

  println("### マップ ###")

  val mit = it4 map (x => x * 10)
  println(mit.next())

  val fit = it4 flatMap (x => List(x * 10).iterator)
  println(fit.next())

  val cit = it4 collect { case 30 => 35 case 40 => 45 case x => x + 5 }
  println(cit.next())

  println("### 変換 ###")
  val ys = List(10, 20, 30, 10, 20, 30)
  val zs = List(1 -> 'a', 2 -> 'b', 3 -> 'c')
  val cit1, cit2, cit3, cit4, cit5, cit6, cit7 = ys.iterator
  val cit8 = zs.iterator

  val to1it = cit1.toArray
  // to1it: Array[Int] = Array(10, 20, 30, 10, 20, 30)
  println(to1it.toList)

  val to2it = cit2.toList
  // to2it: List[Int] = List(10, 20, 30, 10, 20, 30)
  println(to2it)

  val to3it = cit3.toIterable
  // to3it: Iterable[Int] = Stream(10, ?)
  println(to3it)

  val to4it = cit4.toSeq
  // to4it: Seq[Int] = Stream(10, ?)
  println(to4it)

  val to5it = cit5.toIndexedSeq
  // to5it: scala.collection.immutable.IndexedSeq[Int] = Vector(10, 20, 30, 10, 20, 30)
  println(to5it)

  val to6it = cit6.toStream
  // to6it: Stream[Int] = Stream(10, ?)
  println(to6it)

  val to7it = cit7.toSet
  // to7it: scala.collection.immutable.Set[Int] = Set(10, 20, 30)
  println(to7it)

  val to8it = cit8.toMap
  // to8it: scala.collection.immutable.Map[Int,Char] = Map((1,a), (2,b), (3,c))
  println(to8it)

  println("### サイズ情報 ###")
  println(it4.isEmpty)
  println(it4.nonEmpty)
  println(it4.size)
  println(it4.length)
  println(it4.hasDefiniteSize)

  println("### 要素取得・添字検索 ###")
  it5 find (x => x > 15)
  println(it5.next())

  it5 indexOf 50
  println(it5.next())

  it5 indexWhere (x => x > 70)
  println(it5.next())

  println("### サブイテレーター ###")

  val sbit1 = it6 take 3
  for (elem <- sbit1) print(elem + ",")
  println()

  val sbit2 = it6 drop 3
  for (elem <- sbit2) print(elem + ",")
  println()

  val sbit3 = it7 slice (2, 4)
  for (elem <- sbit3) print(elem + ",")
  println()

  val sbit4 = it7 takeWhile (x => x < 70)
  for (elem <- sbit4) print(elem + ",")
  println()

  val sbit5 = it7 dropWhile (x => x < 110)
  for (elem <- sbit5) print(elem + ",")
  println()

  val sbit6 = it8 filter (x => x < 40)
  for (elem <- sbit6) print(elem + ",")
  println()

  val sbit7 = it9 withFilter (x => x < 40)
  for (elem <- sbit7) print(elem + ",")
  println()

  val sbit8 = it10 filterNot (x => x < 40)
  for (elem <- sbit8) print(elem + ",")
  println()

  println("### 分割 ###")

  val (pit1, pit2) = it11 partition (x => x < 40)
  for (elem <- pit1) print(elem + ",")
  println()
  for (elem <- pit2) print(elem + ",")
  println()

  println("### 要素の条件 ###")
  println(it12 forall (x => x < 50))
  println(it12 exists (x => x > 70))
  println(it12 count (x => x < 110))

  println("### 畳み込み ###")
  val zs1 = List(10, 20, 30)
  val zs2 = List(80, 40, 10)
  val zit1, zit3, zit5, zit6 = zs1.iterator
  val zit2, zit4 = zs2.iterator

  println((18000 /: zit1)(_ / _)) // ((18000/10)/20)/30
  println((zit2 :\ 2)(_ / _)) // 80/(40/(10/2))

  println(zit3.foldLeft(18000)(_ / _))
  println(zit4.foldRight(2)(_ / _))

  println(zit5.reduceLeft (_ - _))    // (10-20)-30 = -40
  println(zit6.reduceRight (_ - _)) //  10-(20-30) = 20

  println("### 型限定の畳み込み ###")
  println(it13.sum)
  println(it14.product)
  println(it15.min)
  println(it16.max)

  println("### ジッパー ###")
  val zpit1 = it17 zip it18
  println(zpit1.next())

  val zs3 = List(10, 20, 30)
  val zit7, zit8 = zs3.iterator
  val zpit2 = it19 zipAll (zit7, 3, 7)
  for (elem <- zpit2) print(elem + ",")
  println()
  val zpit3 = zit8 zipAll (it20, 3, 7)
  for (elem <- zpit3) print(elem + ",")
  println()

  val zpit4 = it17.zipWithIndex
  for (elem <- zpit4) print(elem + ",")
  println()

  println("### 更新 ###")

  val zs4 = List(333, 555, 777)
  val zit9 = zs4.iterator
  val rit = it21 patch (2, zit9, 5)
  for (elem <- rit) print(elem + ",")
  println()

  println("### 比較 ###")
  val zs5 = List(10, 20, 30, 45, 50, 60)
  val zit10 = zs5.iterator
  println(it22 sameElements zit10)
  for (elem <- it22) print(elem + ",")
  println()
  for (elem <- zit10) print(elem + ",")
  println()

  println("### 文字列 ###")
  val buf2 = new StringBuilder
  it23 addString (buf2, "[", "-", "]")
  println(buf2)

  println(it24 mkString ("[", "-", "]"))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// バッファー付きイテレーター
object Sp24_16_01{
  val xs = List("", "", "", "ab", "cd", "ef", "gh", "ij")
  val it1, it2 = xs.iterator

  // 空文字列は読み飛ばせるが、空でない最初の文字列も読み飛ばしてしまう。
  println("skipEmptyWordsNOT")
  def skipEmptyWordsNOT(it: Iterator[String]) {
    while (it.next().isEmpty) {
      println("skip")
    }
    println(it.next())
  }
  skipEmptyWordsNOT(it1)

  // バッファー付きイテレーターを使った場合、空文字列は読み飛ばせ、空でない最初の文字も読める。
  println("skipEmptyWords")
  def skipEmptyWords(it: BufferedIterator[String]) = {
    while (it.head.isEmpty) {
      println("skip :[" + it.next() + "]")
    }
    println(it.next())
  }
  val bit = it2.buffered
  skipEmptyWords(bit)

  println("bufferedメソッド")
  val it = Iterator(1, 2, 3, 4)
  val bit2 = it.buffered
  println(bit2.head)
  println(bit2.next())
  println(bit2.next())

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 0からコレクションを作る
object Sp24_17_a{
  val dog, cat, bird = 1

  Traversable()
  List()
  List(1.0, 2.0)
  Vector(1.0, 2.0)
  Iterator(1, 2, 3)
  Set(dog, cat, bird)
  scala.collection.mutable.HashSet(dog, cat, bird)
  Map('a' -> 7, 'b' -> 0)

  List(1, 2, 3)
  // res0: List[Int] = List(1, 2, 3)

  Traversable(1, 2, 3)
  // res1: Traversable[Int] = List(1, 2, 3)

  import scala.collection.mutable
  mutable.Traversable(1, 2, 3)
  // res2: scala.collection.mutable.Traversable[Int] = ArrayBuffer(1, 2, 3)

  List()
  // List[Nothing] = List()
  List.empty
  // List[Nothing] = List()

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// シーケンスのファクトリーメソッド
object Sp24_17_b{
  val x, y, z = 1
  val xs, ys, zs = List(10, 20, 30)

  println(List.empty)
  println(List(x, y, z))
  println(List.concat(xs, ys, zs))
  println(List.fill(3)(10))
  println(List.fill(2, 5)(3))
  println(List.tabulate(5)(x => x * x))
  println(List.tabulate(5)(x => x + 1))
  println(List.tabulate(2, 5)((x, y) => (x + 1) * y))
  println(List.range(1, 5))
  println(List.range(1, 10, 2))
  println(List.iterate(2, 5)(x => x + 1))
  println(List.iterate(2, 5)(x => x * x))

  /*
  seq.empty

  seq(x, y, z)

  seq.concat(xs, ys, zs)

  seq.fill(n)(e)

  seq.fill(m, n)(e)

  seq.tabulate(n)(f)

  seq.tabulate(m, n)(f)

  seq.range(start, end)

  seq.range(start, end, step)

  seq.iterate(x, n)(f)
  */

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// JavaコレクションとScalaコレクションの相互互換
object Sp24_18{
  // 並列マップ (ConcurrentMap) sp24_10_13 も参照

  import collection.JavaConversions._
  import collection.mutable._

  val jul: java.util.List[Int] = ArrayBuffer(1, 2, 3)
  val buf: Seq[Int] = jul
  println(buf)

  val m : java.util.Map[String, Int] = HashMap("abc" -> 1, "hello" -> 2)
  println(m)

  val jul2: java.util.List[Int] = List(1, 2, 3)
  // collection.immutable.ListをJavaのjava.util.List型に変化し、java.util.LIstに対して
  // ミューテーション（書き換え）の行われる操作を実行すると、エラーとなる。

  //jul2.add(7)
  // Error: Exception in thread "main" java.lang.ExceptionInInitializerError

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// Scala 2.7かの移植
object Sp24_19{
  val xs = List((1, 2), (3, 4))
  List.unzip(xs)  // deprecated = 非推奨警告
  xs.unzip
  // <console>:7: warning: method unzip in object List is deprecated: use `xs.unzip' instead of `List.unzip(xs)'

  val m = xs.toMap
  m.keys  // マイグレーション警告
  // <console>:8: warning: method keys in trait MapLike has changed semantics:
  // As of 2.8, keys returns Iterable[A] rather than Iterator[A].

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}
