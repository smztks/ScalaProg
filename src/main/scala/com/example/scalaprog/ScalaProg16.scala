package com.example.scalaprog

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/10/10
 * Time: 18:17
 * To change this template use File | Settings | File Templates.
 */

// 第16章 リストの操作
object ScalaProg16

// リストリテラル
object Sp16_01 {
  val fruit = List("apples", "oranges", "pears")
  val nums = List(1, 2, 3, 4)
  val diag3 =
    List(
      List(1, 0, 0),
      List(0, 1, 0),
      List(0, 0, 1)
    )
  val empty = List()

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// List型
object Sp16_02 {
  val fruit: List[String] = List("apples", "oranges", "pears")
  val nums: List[Int] = List(1, 2, 3, 4)
  val diag3: List[List[Int]] =
    List(
      List(1, 0, 0),
      List(0, 1, 0),
      List(0, 0, 1)
    )
  val empty: List[Nothing] = List()

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// リストの構築
object Sp16_03 {
  val fruit = "apples" :: ("oranges" :: ("pears" :: Nil))
  val nums1 = 1 :: (2 :: (3 :: (4 :: Nil)))
  val nums2 = 1 :: 2 :: 3 :: 4 :: Nil
  val diag3 = (1 :: (0 :: (0 :: Nil))) ::
                (0 :: (1 :: (0 :: Nil))) ::
                (0 :: (0 :: (1 :: Nil))) :: Nil

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// リストに対する基本操作
object Sp16_04_01 {
  val fruit: List[String] = List("apples", "oranges", "pears")
  val nums: List[Int] = List(1, 2, 3, 4)
  val diag3: List[List[Int]] =
    List(
      List(1, 0, 0),
      List(0, 1, 0),
      List(0, 0, 1)
    )
  val empty: List[Nothing] = List()
  println("empty.isEmpty: " + empty.isEmpty)
  println("fruit.isEmpty: " + fruit.isEmpty)
  println("fruit.head: " + fruit.head)
  println("fruit.tail.head: " + fruit.tail.head)
  println("diag3.head: " + diag3.head)
  //println("Nil.head: " + Nil.head)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 挿入ソート
object Sp16_04_02 {
  def isort(xs1: List[Int]): List[Int] = {
    if (xs1.isEmpty) {
      println("A: " + xs1.isEmpty)
      Nil
    } else {
      println("B: " + xs1.isEmpty  + "|" + xs1.head + "|" + xs1.tail)
      // isortを再帰処理し、xs1.tailがisEmptyになった時点（最初はNilが戻り値となる）から、insertを実行する。
      insert(xs1.head, isort(xs1.tail))
    }
  }
  def insert(x: Int, xs2: List[Int]): List[Int] = {
    if (xs2.isEmpty || x <= xs2.head) {
      println("C: " + xs2.isEmpty + "|" + x + "|" + xs2)
      x :: xs2
    } else {
      println("D: " + xs2.isEmpty + "|" + x + "|" + xs2)
      // xがxs2.tailより小さくなるかisEmptyになるまで再帰処理し、戻り値（x :: xs2）をxs2.head ::とする。
      xs2.head :: insert(x, xs2.tail)
    }
  }
  println(isort(List(8, 1, 2, 7, 4, 6, 3, 5)))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// リストパターン
object Sp16_05_01 {
  val fruit: List[String] = List("apples", "oranges", "pears" , "banana")

  val List(a, b, c ,d) = fruit
  println(a + ":" + b + ":" + c + ":" + d)

  val x :: y :: rest = fruit
  println(x + ":" + y + ":" + rest)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

object Sp16_05_02 {
  def isort(xs: List[Int]): List[Int] =
    xs match {
      case List() => List()
      case x :: rest => insert(x, isort(rest))
    }
  def insert(x: Int, xs: List[Int]): List[Int] = xs
    match {
      case List() => List(x)
      case y :: rest => if(x <= y) x :: xs
                         else y :: insert(x, rest)
    }
  println(isort(List(8, 1, 2, 7, 4, 6, 3, 5)))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// Listクラスの一階メソッド
// 2個のリストの連結
object Sp16_06_01 {
  val list1 = List(1, 2) ::: List(3, 4, 5)
  println(list1)

  val list2 = List() ::: List(1, 2, 3)
  println(list2)

  val list3 = List(1, 2, 3) ::: List(4)
  println(list3)

  val list4 = List(1, 2) ::: (List(3, 4) ::: List(5, 6))
  println(list4)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 分割統治原則
object Sp16_06_02 {
  // メソッドのみに適用するジェネリクスは、メソッド名の後ろに角括弧で型パラメーターを記述する。
  // cf. http://www.ne.jp/asahi/hishidama/home/tech/scala/generics.html
  def append[T](xs: List[T], ys: List[T]): List[T] =
    xs match {
      case List() => ys
      case x :: rest => x :: append(rest, ys)
    }
  println(append(List(1, 2), List(3, 4)))
  println(append(List(), List(3, 4)))
  println(append(List(1,2), List('a','b')))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// リストの長さを計算する:length
object Sp16_06_03 {
  println(List(1, 2, 3).length)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// リストの末尾へのアクセス:initとlast
object Sp16_06_04 {
  val abcde = List('a', 'b', 'c', 'd', 'e')
  // ★の箇所の値を取得
  println("head: " + abcde.head)  // [★,2,3,4] 処理は速い
  println("tail: " + abcde.tail)  // [1,★,★,★] 〃
  println("init: " + abcde.init)  // [★,★,★,4] 処理は遅い：理由は全体をたどる為
  println("last: " + abcde.last)  // [1,2,3,★] 〃

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// リストの反転:reverse
object Sp16_06_05_01 {
  val abcde = List('a', 'b', 'c', 'd', 'e')
  println("reverse: " + abcde.reverse)
  println("reverse.init=tail.reverse: " + abcde.reverse.init)  // 処理は遅い
  println("reverse.tail=init.reverse: " + abcde.reverse.tail)  // 処理は速い
  println("reverse.head=last: " + abcde.reverse.head)           // 処理は速い
  println("reverse.last=head: " + abcde.reverse.last)           // 処理は遅い
  println("normal: " + abcde)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

object Sp16_06_05_02 {
  def rev[T](xs: List[T]): List[T] =
    xs match {
      case List() => xs
      case x :: rest => rev(rest) ::: List(x)
    }
  println(rev(List(1, 2, 3, 4)))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// プレフィックスとサフィックス:drop,take,splitAt
object Sp16_06_06 {
  val abcde = List('a', 'b', 'c', 'd', 'e')
  println("take: " + (abcde take 2))
  println("drop: " + (abcde drop 2))
  println("splitAt: " + (abcde splitAt 2))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 要素の選択:applyとindices
object Sp16_06_07 {
  val abcde = List('a', 'b', 'c', 'd', 'e')
  println(abcde apply 2)
  println(abcde(2))
  println((abcde apply 3) equals (abcde drop 3).head)
  println(abcde.indices)
  println((abcde take 2).indices)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// リストのリストから単層のリストへ:flatten
object Sp16_06_08 {
  val list1 = List(List(1, 2), List(3), List(), List(4, 5)).flatten
  println(list1)
  //val list2 = List(1, 2, 3).flatten
  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// リストのジッパー操作:zipとunzip
object Sp16_06_09 {
  val abcde = List('a', 'b', 'c', 'd', 'e')
  println(abcde.indices zip abcde)

  val zipped = abcde zip List(1, 2, 3)
  println(zipped)
  println(abcde.zipWithIndex)
  println(zipped.unzip)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// リストの表示:toStringとmkString
object Sp16_06_10 {
  val abcde = List('a', 'b', 'c', 'd', 'e')
  println(abcde.toString)
  println(abcde.mkString ("アルファベット[", "-", "]を書きましょう"))
  println(abcde.mkString ("-"))
  println(abcde.mkString)

  val buf = new StringBuilder
  abcde addString (buf, "アルファベット[", "-", "]を書きましょう")
  println(buf)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// リストの変換:iterator,toArray,copyToArray
object Sp16_06_11 {
  val abcde = List('a', 'b', 'c', 'd', 'e')
  val arr = abcde.toArray
  println(arr.toList)

  val arr2 = new Array[Int](10)
  List(1, 2, 3) copyToArray (arr2, 3)
  println(arr2.toList)

  val it = abcde.iterator
  println(it.next + ":" + it.hasNext)
  println(it.next + ":" + it.hasNext)
  println(it.next + ":" + it.hasNext)
  println(it.next + ":" + it.hasNext)
  println(it.next + ":" + it.hasNext)

  val el = abcde.elements // elementは古い、2.8.1では不使用推奨
  println(el.next + ":" + el.hasNext)
  println(el.next + ":" + el.hasNext)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// サンプル:マージソート
object Sp16_06_12 {
  // カリー化を使って比較ルールとソート対象リストを別に受け付けるようにしますよ
  def msort[T](less: (T,T) => Boolean)(xs: List[T]): List[T] = {
    // ソート比較用メソッドデス
    def merge(xs: List[T], ys:List[T]): List[T] =
      (xs, ys) match {
        // 比較対象のリストの片方が空の場合は残ったほうを返します
        case (Nil, _) => ys
        case (_, Nil) => xs
        // 各々のリストの先頭の値を比較して
        // 条件に合う方を先頭にしたリストを再帰的に構築します
        // (残った方に再帰的に同じ処理を繰り返します)
        case (x :: xs1, y :: ys1) => {
          if(less(x,y)) {
            //println("marge: " + (x :: merge(xs1, ys)))
            x :: merge(xs1, ys)
          } else {
            //println("marge: " + (y :: merge(xs, ys1)))
            y :: merge(xs, ys1)
          }
        }
      }
    // リストの真ん中の位置を取得します
    val n = xs.length / 2
    //println("divide: " + xs + "|" + (xs.length/2))
    //println("xs,n: " + xs + "|" + (xs.length.toDouble/2))
    // 要素が1つのみの場合はそのまま返します
    if (n == 0 ) xs
    else {
      // 前半、後半のリストに分けます
      val (ys, zs) = xs.splitAt(n)
      // 再帰的に分割したリスト要素を使って比較します
      // lessはパラメータとして与えられた比較ルールですね
      merge(msort(less)(ys), msort(less)(zs))
    }
  }

  println(msort((x:Int, y:Int) => x < y)(List(4,0,6,5,3,1,7,2)))
  println(msort((x:Double, y:Double) => x < y)(List(1.5, 1.2, 2.3, 2.1, 0.8)))

  val intSort = msort((x:Int, y:Int) => x < y) _
  println(intSort(List(3,78,4,5,8,23,54,3)))

  val mixedInts = List(4,5,6,1,2,3)
  println(intSort(mixedInts))

  val revSort = msort((x:Int, y:Int) => x > y) _
  println(revSort(List(3,78,4,5,8,23,54,3)))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// Listクラスの高階メソッド
object Sp16_07_01 {
  println(List(1, 2, 3) map (_ + 1))  // List(2, 3, 4)

  val words = List("the", "quick", "brown", "fox")
  println(words map (_.length))   // List(3, 5, 5, 3)
  println(words map (_.toList.reverse.mkString))  // List(eht, kciuq, nworb, xof)
  println(words map (_.reverse.mkString)) // 上に同じ List(eht, kciuq, nworb, xof)

  println(words map (_.toList)) // List(List(t, h, e), List(q, u, i, c, k), List(b, r, o, w, n), List(f, o, x))
  println(words flatMap (_.toList)) // List(t, h, e, q, u, i, c, k, b, r, o, w, n, f, o, x)

  println("List.range(1, 5): " + List.range(1,5))
  val list1 = List.range(1, 5) flatMap (
    i => List.range(1, i) map (j => (i, j))
    // なぜ、jはj<iを満たす数値になるのか？ ... List.range(1,i)の範囲でjの値が生成されるから
  )
  println(list1)

  val list2 = for(i <- List.range(1,5); j <- List.range(1,i)) yield (i,j)
  // なぜ、jはj<iを満たす数値になるのか？ ... List.range(1,i)の範囲でjの値が生成されるから
  println(list2)

  var sum = 0
  List(1, 2, 3, 4, 5) foreach (sum += _)  // foreachの結果型がUnitなので、var sumに加算させる方法を取る
  println("sum:" + sum)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// リストのフィルタリング:filter,partition,find,takeWhile,dropWhile,span
object Sp16_07_02 {
  val words = List("the", "tquick", "the2", "brown", "fox", "the3")

  println(List(1, 2, 3, 4, 5) filter (_ % 2 == 0)) // List(2, 4) ... 2で割りきりる値を抽出
  println(words filter (_.length == 3)) // List(the, fox) ... 文字列の長さが3の値を抽出
  //println(words filter (x => x.length == 3))

  println(List(1, 2, 3, 4, 5) partition (_ % 2 == 0)) // (List(2, 4),List(1, 3, 5))
  println(words partition (_.length == 3))  // (List(the, fox),List(tquick, the2, brown, the3))

  println(List(1, 2, 3, -4, 5) takeWhile (_ > 0)) // List(1, 2, 3)
  println(words dropWhile (_ startsWith "t")) // List(brown, fox, the3)

  println(List(1, 2, -2, 3, -4, 5) span (_ > 0))  // (List(1, 2),List(-2, 3, -4, 5))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// リストを対象とする述語関数:forallとexists
object Sp16_07_03 {
  val diag3: List[List[Int]] =
    List(
      List(0, 0, 1),
      List(0, 0, 0),
      List(1, 0, 0)
    )

  def hasZeroRow(m: List[List[Int]]) =
    m exists (row => row forall (_ == 0))
    //m exists (_ forall (_ == 0))
  println(hasZeroRow(diag3))

  println(List(0,1,2,3,4,5).exists(_ == 0)) // 0が一つでもあればtrue
  println(List(1,2,3,4,5).exists(_ == 0)) // 0がひとつも無いのでfalse
  println(List(0,0,0,0,0).forall(_ == 0)) // すべてが0なのでtrue
  println(List(0,0,1,0,0).forall(_ == 0)) // すべてが0で無いのでfalse

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// リストの畳み込み: /:と:\
object Sp16_07_04 {
  def sum(xs: List[Int]): Int = (1 /: xs)(_ + _)
  println(sum(List(1,2,3,4,5)))

  val words = List("the", "quick", "brown", "fox")
  println(("" /: words)(_ + "-" + _))
  println((words.head /: words.tail)(_ + "-" + _))

  val diag3: List[List[Int]] =
    List(
      List(0, 0, 1),
      List(0, 0, 0),
      List(1, 0, 0)
    )

  def flattenLeft[T](xss: List[List[T]]) =
    (List[T]() /: xss)(_ ::: _)
  println(flattenLeft(diag3))

  def flattenRight[T](xss: List[List[T]]) =
    (xss :\ List[T]())(_ ::: _)
  println(flattenRight(diag3))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// foldを使ったリストの反転
object Sp16_07_05 {
  def reverseLeft[T](xs: List[T]) =
    (List[T]() /: xs) {(ys, y) => y :: ys}
  println(reverseLeft(List(1, 2, 3, 4, 5)))

  def reverseRight[T](xs: List[T]) =
    (xs :\ List[T]()) {(ys, y) => y ::: List(ys)}
  println(reverseRight(List(1, 2, 3, 4, 5)))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// リストのソート:sortWith
object Sp16_07_06 {
  println(List(1, -3, 4, 2, 6) sortWith (_ < _))

  val words = List("the", "quick", "brown a", "fox")
  println(words sortWith (_.length > _.length))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// Listオブジェクトのメソッド
// 要素からリストを作る:List.apply
object Sp16_08_01 {
  val list1 = List.apply(1, 2, 3)
  println(list1)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 数値の範囲を作る:List.range
object Sp16_08_02 {
  val list1 = List.range(1, 5)  // from, until
  val list2 = List.range(1, 9, 2) // from, until, step
  val list3 = List.range(9, 1, -3) // from, untuil, -step

  println(list1 + "|" + list2 + "|" + list3)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 同じ値のリストを作る:List.fill
object Sp16_08_03 {
  val list1 = List.fill(5)('a')
  val list2 = List.fill(3)("hello")
  val list3 = List.fill(2,3)('b')

  println(list1 + "|" + list2 + "|" + list3)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 関数の実行結果による表の作成:List.tabulate
object Sp16_08_04 {
  val squares = List.tabulate(5)(n => n * n)  // 0*0, 1*1, 2*2, 3*3, 4*4
  // tabulate: ～を表にする、～を一覧にする
  println(squares)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 複数のリストの連結:List.concat
object Sp16_08_05 {
  val list1 = List.concat(List('a', 'b'), List('c'))
  val list2 = List.concat(List(), List('b'), List('c'))
  val list3 = List.concat()
  println(list1 + "|" + list2 + "|" + list3)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 複数のリストをまとめて処理する方法
object Sp16_09 {
  val list1 = (List(10, 20), List(3, 4, 5)).zipped.map(_ * _) // List(30, 80)
  // zip & map(右側処理を実施）
  println(list1)

  println((List("abc123", "de45"), List(6, 4)).zipped.forall(_.length() == _)) // true
  println((List("abc", "de"), List(3, 2)).zipped.exists(_.length() != _)) // false

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// Scalaの型推論アルゴリズムを理解する
object Sp16_10 {
  import com.example.scalaprog.Sp16_06_12.msort

  // カリー化を使って比較ルールとソート対象リストを別に受け付けるようにしますよ
  def msortSwapped[T](xs: List[T])(less: (T,T) => Boolean): List[T] = {
    // ソート比較用メソッドデス
    def merge(xs: List[T], ys:List[T]): List[T] =
      (xs, ys) match {
        // 比較対象のリストの片方が空の場合は残ったほうを返します
        case (Nil, _) => ys
        case (_, Nil) => xs
        // 各々のリストの先頭の値を比較して
        // 条件に合う方を先頭にしたリストを再帰的に構築します
        // (残った方に再帰的に同じ処理を繰り返します)
        case (x :: xs1, y :: ys1) => {
          if(less(x,y)) {
            //println("marge: " + (x :: merge(xs1, ys)))
            x :: merge(xs1, ys)
          } else {
            //println("marge: " + (y :: merge(xs, ys1)))
            y :: merge(xs, ys1)
          }
        }
      }
    // リストの真ん中の位置を取得します
    val n = xs.length / 2
    //println("divide: " + xs + "|" + (xs.length/2))
    //println("xs,n: " + xs + "|" + (xs.length.toDouble/2))
    // 要素が1つのみの場合はそのまま返します
    if (n == 0 ) xs
    else {
      // 前半、後半のリストに分けます
      val (ys, zs) = xs.splitAt(n)
      // 再帰的に分割したリスト要素を使って比較します
      // lessはパラメータとして与えられた比較ルールですね
      merge(msort(less)(ys), msort(less)(zs))
    }
  }

  val abcde = List('a', 'b', 'c', 'd', 'e')
  println(msort((x: Char, y: Char) => x > y)(abcde))
  //println(msort(_ > _)(abcde))
  println(msort[Char](_ > _)(abcde))
  println(msortSwapped(abcde)(_ > _))

  println(abcde sortWith (_ > _))

  println((List(1,2,3,4,5) :\ List(1))(_ :: _))
  println((List(1,2,3,4,5) :\ List[Int]())(_ :: _))
  //println((List(1,2,3,4,5) :\ List())(_ :: _))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

