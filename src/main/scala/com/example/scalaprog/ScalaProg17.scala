package com.example.scalaprog

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/10/14
 * Time: 11:06
 * To change this template use File | Settings | File Templates.
 */

// 第17章 コレクション
object ScalaProg17

// シーケンス
// リスト
object Sp17_01_01 {
  val colors = List("red", "blue", "green")
  println(colors.head + "|" + colors.tail)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 配列
object Sp17_01_02 {
  val fiveInts = new Array[Int](5)
  println(fiveInts.toList)

  val fiveToOne = Array(5, 4, 3, 2, 1)
  println(fiveToOne.toList)

  fiveInts(0) = fiveToOne(4)
  println(fiveInts.toList)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// リストバッファー
object Sp17_01_03 {
  import scala.collection.mutable.ListBuffer
  val buf = new ListBuffer[Int]
  buf += 1 // 末尾に追加
  buf += 2
  buf += 1
  println(buf)

  3 +=: buf // 先頭に追加
  // buf = 3 + buf ... X
  println(buf.toList)

  buf -= 1  // 最初に見つかった1を取り除く
  println(buf + "|" + buf.length + "|" + buf(2))

  val buf2 = new ListBuffer[String]
  buf2 += "abc"
  buf2 += "def"
  buf2 += "ghi"
  println(buf2)

  buf2 -= "def"  // 最初に見つかったdefを取り除く
  println(buf2)

  println(buf2 ++ "zzz")  // ListBuffer(abc, ghi, z, z, z) ???

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 配列バッファー
object Sp17_01_04 {
  import scala.collection.mutable.ArrayBuffer
  val buf = new ArrayBuffer[Int]()
  buf += 12
  buf += 14
  buf += 15
  3 +=: buf
  buf -= 14
  println(buf + "|" + buf.length + "|" + buf(0))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 文字列(StringOps)
object Sp17_01_05 {
  def hasUpperCase(s: String) = s.exists(_.isUpper) // 全ての文字列はSeq[Char]として使えるとのこと
  println(hasUpperCase("Abc"))
  println(hasUpperCase("aBc"))
  println(hasUpperCase("abC"))
  println(hasUpperCase("abc"))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 集合(Set)とマップ(Map)
// 集合は重複する要素を持たない。マップは意の種類のキーを使える配列≒連想配列
object Sp17_02 {
/*
object Predef {
  type Map[A, + B] = collection.immutable.Map[A, b]
  type Set[A] = collection.immutable.Set[A]
  val Map = collection.immutable.Map
  val Set = collection.immutable.Set
}
*/
  import scala.collection.mutable.Set
  Set(1,2,3,4,5)  // ミュータブル
  import scala.collection.mutable.Map
  Map(1 -> 'a', 2->'b', 3->'c') // ミュータブル

  import scala.collection.mutable
  mutable.Set(1,2,3)
  Set(1,2,3)  // イミュータブル
  mutable.Map(1->'a',2->'b',3->'c')
  Map(1->'a',2->'b',3->'c') // イミュータブル

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 集合の使い方
object Sp17_02_01 {
  val text = "See Spot run.Run,Spot.Run!AAz !,.BBzCC"
  val wordsArray = text.split("[z !,.]+") // []内の文字が１個以上(+:1回以上にマッチ)の文字を分割
  println(wordsArray.toList)

  import scala.collection.mutable
  val words = mutable.Set.empty[String]
  //val words = mutable.Set("sEE", "spot", "ABC") // 初期値として値を代入することも可能
  //var words = Set.empty[String]  // var + mutable Setでも実行可能だが...
  for (word <- wordsArray)
    words += word.toLowerCase
  println(words)

  val nums = Set(1, 2, 3)   //下記の毎処理毎、イミュータブルなnumsが使われる=定数
  println(nums + 5) // 追加
  println(nums - 3) // 指定の要素を取り除く
  println(nums ++ List(5, 6)) // 複数の要素を追加
  println(nums -- List(1, 2)) // 複数の要素を取り除く
  println(nums & Set(1, 3, 5, 7)) // 2つの集合の積集合（同じもののみ）
  println(nums.size)  // 集合のサイズ
  println(nums.contains(3)) // 指定の要素が含まれているかチェック

  import scala.collection.mutable
  val words2 = mutable.Set.empty[String]  //下記の毎処理毎、ミュータブルなwords2が使われる=変数
  println(words2 += "the")  // 要素を追加
  println(words2 -= "the")  // 要素を取り除く
  println(words2 ++= List("do", "re", "mi"))  // 複数の要素を追加
  println(words2 --= List("do", "re"))  // 複数の要素を取り除く
  println(words2.clear) // すべての要素を取り除く

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// マップの使い方
object Sp17_02_02 {
  import scala.collection.mutable
  val map = mutable.Map.empty[String, Int]
  map("hello") = 1
  map("there") = 2
  println(map)
  println(map("hello"))

  def countWords(text: String) = {
    val counts = mutable.Map.empty[String, Int]
    for (rawWord <- text.split("[ ,!.]+")) {
      val word = rawWord.toLowerCase
      val oldCount =
        if (counts.contains(word)) counts(word) // 既存の要素のカウント数
        else 0
      counts += (word -> (oldCount + 1))  // 新規でも既存でも+1をする。
    }
    counts  // 最後にcountsを戻り値として返す
  }
  println(countWords("See Spot run! Run, Spot. Run!"))

  val nums = Map("i" -> 1, "ii" -> 2)  //下記の毎処理毎、イミュータブルなnumsが使われる=定数
  println(nums + ("vi" -> 6)) // Map(i -> 1, ii -> 2, vi -> 6)
  println(nums - "ii") // Map(i -> 1)
  println(nums ++ List("iii" -> 3, "v" -> 5)) // Map(i -> 1, ii -> 2, iii -> 3, v -> 5)
  // ※イミュータブルの場合は、++
  println(nums -- List("i", "ii")) // Map()
  println(nums.size) // 2
  println(nums.contains("ii")) // true
  println(nums.keys)  // Set(i, ii) キーを返す（文字列"i","ii"を順に返すIterableを返す）
  println(nums.keySet)  // Set(i, ii) キーを集合として返す
  println(nums.values)  // MapLike(1, 2) 値を返す（整数の1,2を順に返すIterableを返す）
  println(nums.isEmpty) // false マップが空かどうかを示す

  import scala.collection.mutable //下記の毎処理毎、ミュータブルなwordsが使われる=変数
  val words = mutable.Map.empty[String, Int]
  println(words += ("one" -> 1))  // Map(one -> 1)
  println(words -= ("one")) // Map()
  println(words ++= List("one" -> 1, "two" -> 2, "three" -> 3)) // Map(one -> 1, two -> 2, three -> 3)
  // ※ミュータブルの場合は、++=
  println(words --= List("one", "two")) // Map(three -> 3)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// デフォルトの集合とマップ
object Sp17_02_03 {
  // Set.scalaファイルのSet.classに記載
  // file:///C:/scala/scala-2.8.1.final/doc/scala-devel-docs/api/scala/collection/immutable/Set$.html

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// ソートされた集合とマップ （イミュータブル版しかない、赤黒木アルゴリズムを使用）
object Sp17_02_04 {
  import scala.collection.immutable.TreeSet
  val ts = TreeSet(9, 3, 1, 8, 0, 2, 7, 4, 6, 5)
  val cs = TreeSet('z', 'a', 'b')
  println(ts + "|" + cs)  // TreeSet(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)|TreeSet(a, b, z)

  import scala.collection.immutable.TreeMap
  var tm = TreeMap(3 -> 'x', 1 -> 'x', 4 -> 'x')
  tm += (2 -> 'x')
  println(tm) // Map(1 -> x, 2 -> x, 3 -> x, 4 -> x)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// ミュータブルとイミュータブルのどちらを使うべきか
object Sp17_03 {
  val people1 = Set("Nancy", "Jane")
  //people1 += "Bob" // このままではイミュータブルSetなので追加できずにエラー

  var people2 = Set("Nancy", "Jane")
  people2 += "Bob"
  people2 -= "Jane"
  people2 ++= List("Tom", "Harry")
  println(people2)

  import scala.collection.mutable.Map
  var capital = Map("US" -> "Washington", "France" -> "Paris")
  capital += ("Japan" -> "Tokyo")
  println(capital("France"))

  var roughlyPi = 3.0
  roughlyPi += 0.1
  roughlyPi += 0.04
  println(roughlyPi)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// コレクションの初期化
object Sp17_04 {
  println(List(1, 2, 3))
  println(Set('a', 'b', 'c'))

  import scala.collection.mutable
  println(mutable.Map("hi" -> 2, "there" -> 5))
  println(Array(1.0, 2.0, 3.0).toList)

  val stuff1 = mutable.Set(42)
  //stuff1 += "abracadabra"   // stuff1の要素型はIntであり、String型で追加しようとしてエラーとなる。

  val stuff2 = mutable.Set[Any](42) // Any型であれば、IntもStringも追加できる
  stuff2 += "abracadabra"
  println(stuff2)

  val colors = List("4blue", "1yellow", "3red", "2green")
  import scala.collection.immutable.TreeSet
  // val treeSet = TreeSet(colors) // コレクションを初期化できない
  val treeSet = TreeSet[String]() ++ colors
  println(treeSet)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 配列やリストへの変換
object Sp17_04_01 {
  import scala.collection.immutable.TreeSet
  val colors = List("4blue", "1yellow", "3red", "2green")
  val treeSet = TreeSet[String]() ++ colors
  println(treeSet)

  val list = treeSet.toList
  println(list) // ソートされている
  val array = treeSet.toArray
  println(array.toList) // ソートされている

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 集合・マップのミュータブル版とイミュータブル版の相互変換
object Sp17_04_02 {
  import scala.collection.immutable.TreeSet
  val colors = List("4blue", "1yellow", "3red", "2green")
  val treeSet = TreeSet[String]() ++ colors

  import scala.collection.mutable
  val mutaSet = mutable.Set.empty ++= treeSet // イミュータブルSetをミュータブルSetに代入
  println(mutaSet)

  val immutaSet = Set.empty ++ mutaSet  // ミュータブルSetをイミュータブルSetに代入
  println(immutaSet)

  val mutaMap = mutable.Map("i" -> 1, "ii" -> 2)
  println(mutaMap)

  val immutaMap  = Map.empty ++ mutaMap
  println(immutaMap)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// タプル
object Sp17_05 {
  val tuple = (1, "hello", Console)
  println(tuple)

  def longestWord(words: Array[String]) = {
  //def longestWord(words: Array[String]): (String, Int) = {  //戻り値を明示的に書いても動く
    var word = words(0)
    var idx = 0
    for (i <- 1 until words.length)
      if (words(i).length > word.length){
        word = words(i)
        idx =i
      }
    (word, idx) // タプルを使った場合、戻り値を複数返せる
  }
  //val longest1 = longestWord(Array("a", "bb", "cccccc", "ddd"))
  val longest1 = longestWord(("The quick brown fox").split(" "))
  println(longest1)
  println(longest1._1)  // タプルの場合、_1メソッドで最初の返り値を取得できる。
  println(longest1._2)
  val (word1, idx1) = longest1  // 各変数に代入する場合は、()を付ける
  println(word1 + "|" + idx1)

  val word2, idx2 = longest1    // ()を付けないと、longest1を各変数に代入する処理になってしまう。
  println(word2 + "|" + idx2)

  val (word3, idx3),(word4, idx4) = longest1
  println(word3 + "|" + idx3 + "|" + word4 + "|" + idx4)

  //　タプルは便利だが、関連性のある複数のデータを扱う場合は、タプルよりクラスを使った方が良い
}
