package com.example.scalaprog

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/10/27
 * Time: 9:37
 * To change this template use File | Settings | File Templates.
 */

// 第25章 Scalaコレクションのアーキテクチャ
object ScalaProg25

// ビルダー
object Sp25_01 {
  /*
  // Builderクラスのアウトライン

  package scala.collection.generic

  class Builder[-Elem, +To] {
    def +=(elem: Elem): this.type
    def result(): To
    def clear()
    def mapResult(f: To => NewTo): Builder[Elem,  NewTo] = ...
  }
  */
  import collection.mutable.ArrayBuffer
  val buf = new ArrayBuffer[Int]
  // buf: scala.collection.mutable.ArrayBuffer[Int] = ArrayBuffer()
  buf += 1
  // res0: buf.type = ArrayBuffer(1)
  buf += 10
  // res1: buf.type = ArrayBuffer(1, 10)

  val bldr = buf mapResult (_.toArray)
  // bldr: scala.collection.mutable.Builder[Int,Array[Int]] = ArrayBuffer(1, 10)
  bldr += 15
  // res2: bldr.type = ArrayBuffer(1, 10, 15)

  buf
  // res3: scala.collection.mutable.ArrayBuffer[Int] = ArrayBuffer(1, 10, 15)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 共通演算の括り出し
object Sp25_02 {
  /*
  // TraversableLikeでのfilterの実装

  package scala.collection

  class TraversableLike[+Elem, +Repr] {
    def newBuilder: Builder[Elem, Repr] // deferred
    def foreach[U](f: Elem => U)        // deferred
            ...
    def filter(p: Elem => Boolean): Repr = {
      val b = newBuilder
      foreach { elem => if (p(elem)) b += elem }
      b.result
    }
  }
   */

  import collection.immutable.BitSet
  val bits = BitSet(1, 2, 3)
  // bits: scala.collection.immutable.BitSet = BitSet(1, 2, 3)
  bits map  (_ * 2)
  // res8: scala.collection.immutable.BitSet = BitSet(2, 4, 6)
  // 関数の結果型が再びIntなら、mapの結果型はBitSetになるが、関数の結果型がそれ以外なら、
  // mapの結果型はSetになる。
  bits map (_.toFloat)
  // res9: scala.collection.immutable.Set[Float] = Set(1.0, 2.0, 3.0)


  // 元のマップが反転可能な場合に、反転したマップを作り出す。
  Map("a" -> 1, "b" -> 2) map  { case (x, y) => (y, x) }
  // res2: scala.collection.immutable.Map[Int,java.lang.String] = Map((1,a), (2,b))

  // キー/値のペアを整数（値）の部分だけに変換する。結果からMapを作り出すことはできないが、
  // MapのスーパートレイトであるIterableを作ることはできる。
  Map("a" -> 1, "b" -> 2) map  { case (x, y) => y }
  // res5: scala.collection.immutable.Iterable[Int] = List(1, 2)

  // 上記の置換は、MapはIterableであるというリスコフ置換の原則に従っている。
  // Iterableで正当な演算は、mapでも正当でなければならない。
  // リスコフ置換の原則 = スーパークラスとサブクラスを定義するとき、サブクラスはスーパークラスを置き換えることができなければならない

  /*
  // TraversableLikeにおけるmapの実装

  def map[B, That](p: Elem => B)
      (implicit bf: CanBuildFrom[B, That, This]): That = {
    val b = bf(this)
    for (x <- this) b += f(x)
    b.result
  }

  // CanBuildFromトレイト
  package scala.collection.generic

  trait CanBuildFrom[-From, -Elem, +To] {
    // Creates a new builder
    def apply(from: From): Builder[Elem, To]
  }
   */

  val xs: Iterable[Int] = List(1, 2, 3)
  // xs: Iterable[Int] = List(1, 2, 3)
  val ys = xs map (x => x * x)
  // ys: Iterable[Int] = List(1, 4, 9)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 新しいコレクションの統合 - シーケンスの統合 RNA ver.1
object Sp25_03_01a {
  import com.example.scalapkg._

  val xs = List(A, G, T, A) // RNA塩基クラスをList化してxsに代入している。
  // xs: List[Product with com.example.rna.Base] = List(A, G, T, A)
  RNA1.fromSeq(xs)
  // res0: com.example.rna.RNA1 = RNA1(A, G, T, A)
  val rna1 = RNA1(A, U, G, G, T)
  // rna1: com.example.rna.RNA1 = RNA1(A, U, G, G, T)

  println(rna1.length)
  // res1: Int = 5
  println(rna1.last)
  // res2: com.example.rna.Base = T

  // 静的な結果型はIndexedSeq[Base]なのに、動的な結果型はVectorになっている。
  // 本当はRNA1型を期待していたが、RNA1はIndexedSeqを拡張しただけなので不可能。
  // IndexedSeqクラスはIndexedSeqを返すtakeメソッドを持っており、しかもIndexSeqのデフォルト実装はVectorである。
  println(rna1.take(3))
  // res3: IndexedSeq[com.example.rna.Base] = Vector(A, U, G)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 新しいコレクションの統合 - シーケンスの統合 RNA ver.2
object Sp25_03_01b {
  import com.example.scalapkg._

  val xs = List(A, G, T, A) // RNA塩基クラスをList化してxsに代入している。
  // xs: List[Product with com.example.rna.Base] = List(A, G, T, A)
  RNA1.fromSeq(xs)
  // res0: com.example.rna.RNA1 = RNA1(A, G, T, A)
  val rna2 = RNA2(A, U, G, G, T)
  // rna2: com.example.rna.RNA2 = RNA2(A, U, G, G, T)

  println(rna2.length)
  // res1: Int = 5
  println(rna2.last)
  // res2: com.example.rna.Base = T
  println(rna2.take(3))
  // res3: com.example.rna.RNA2 = RNA2(A, U, G)
  println(rna2 filter (U !=))
  // res4: com.example.rna.RNA2 = RNA2(A, G, G, T)

  // mapと類似コレクションの扱い
  val rna = RNA1(A, U, G, G, T)
  // rna: com.example.rna.RNA1 = RNA1(A, U, G, G, T)
  // 本来は、res7: RNA = RNA(T, U, G, G, T)のように、RNA型の結果型が使われるようになって欲しい。
  rna map { case A => T case b => b}
  // res7: IndexedSeq[com.example.rna.Base] = Vector(T, U, G, G, T)
  // 2個のRNA型を合わせた場合も、RNA型の結果型が使われるようになって欲しい。
  rna ++ rna
  // res1: IndexedSeq[com.example.rna.Base] = Vector(A, U, G, G, T, A, U, G, G, T)

  // 下記理想的な動作の例
  // RNA型内の塩基を他の型に変換しても、別のRNA型は生成できない。
  rna map Base.toInt
  // res2: IndexedSeq[Int] = Vector(0, 3, 2, 2, 1)
  // Base型でない要素をRNA型に追加しても、新しいRNA型ではなく、一般的なシーケンスが生成される。
  rna ++ List("missing", "data")
  // res3: IndexedSeq[java.lang.Object] = Vector(A, U, G, G, T, missing, data)

}

// 新しいコレクションの統合 - シーケンスの統合 RNA ver.Final
object Sp25_03_01c {
  import com.example.scalapkg._

  val rna = RNA(A, U, G, G, T)
  // rna: com.example.rna.RNA = RNA(A, U, G, G, T)

  rna map { case A => T case b => b}
  // res0: com.example.rna.RNA = RNA(T, U, G, G, T)

  rna ++ rna
  // res1: com.example.rna.RNA = RNA(A, U, G, G, T, A, U, G, G, T)
}

// 新しい集合とマップの統合
object Sp25_03_02 {
  import com.example.scalapkg._

  val pm = PrefixMap("hello" -> 5, "hi" -> 2)
  // pm: com.example.scalapkg.PrefixMap[Int] = Map((hello,5), (hi,2))
  pm withPrefix "h"
  // res10: com.example.scalapkg.PrefixMap[Int] = Map((ello,5), (i,2))
  PrefixMap.empty[String]
  // res1: com.example.scalapkg.PrefixMap[String] = Map()
  val mp = pm map { case (k, v) => (k + "!", "x" * v) }
  // mp: com.example.scalapkg.PrefixMap[String] = Map((hello!,xxxxx), (hi!,xx))
  println(mp)

  val m = PrefixMap("abc" -> 0, "abd" -> 1, "al" -> 2, "all" -> 3, "xy" -> 4)
  // m: PrefixMap[Int] = Map((abc,0), (abd,1),(al,2), (all,3), (xy,4))
  val m2 = m withPrefix "a"
  // m2: com.example.scalapkg.PrefixMap[Int] = Map((bc,0), (bd,1), (l,2), (ll,3))
  m2 withPrefix "b"
  // res15: com.example.scalapkg.PrefixMap[Int] = Map((c,0), (d,1))
  m2 withPrefix "l"
  // res16: com.example.scalapkg.PrefixMap[Int] = Map((,2), (l,3))
  val m3 = m withPrefix "x"
  // m3: com.example.scalapkg.PrefixMap[Int] = Map((y,4))
  m3 withPrefix "y"
  // res17: com.example.scalapkg.PrefixMap[Int] = Map((,4))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}
