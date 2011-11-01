package com.example.scalapkg

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/10/27
 * Time: 11:25
 * To change this template use File | Settings | File Templates.
 */

abstract class Base
case object A extends Base
case object T extends Base
case object G extends Base
case object U extends Base

object Base {
  val fromInt: Int => Base = Array(A, T, G, U)
  val toInt: Base => Int = Map(A -> 0, T -> 1, G -> 2, U -> 3)
}

import collection.IndexedSeqLike
import collection.mutable.{Builder, ArrayBuffer}
import collection.generic.CanBuildFrom

// 第1引数 gropus : Int型の配列をとるコンストラクター、この配列には圧縮したRNAデータが含まれている。
// 1つの要素には16個(1塩基は2bit(ATGUの4個)であり、32bitのIntの場合は16個格納できる）の塩基が詰め込まれている。
// 第2引数 length : 配列（及びシーケンスに含まれる塩基の数を指定。パラメータフィールドによる実装
// IndexedSeqトレイトは、scala.collection.immutableパッケージに含まれており、lengthとapplyの2つの抽象メソッドを
// を定義している。具象サブクラスは、これらのメソッドを実装しなければならない。

// RNA Ver.1
final class RNA1 private (val groups: Array[Int], val length: Int)
  extends IndexedSeq[Base] {
  import RNA1._
  def apply(idx: Int): Base = {
    if (idx < 0 || length <= idx)
      throw new IndexOutOfBoundsException
    // groups配列から整数値を抜き出し、次に右シフト(>>)とマスク(&)をつかって、その整数から2ビットの数値を取り出す。
    Base.fromInt(groups(idx / N) >> (idx % N * S) & M)
  }
}

object RNA1 {
  private val S = 2             // グループを表現するために必要なビット数
  private val N = 32 / S        // Intに収まるグループの数
  private val M = (1 << S) - 1  // グループを分離するビットマスク（1ワードの最下位のSビットを取り出すためのビットマスク）

  // RNAシーケンスを作る方法①
  def fromSeq(buf: Seq[Base]): RNA1 = {
    val groups = new Array[Int]((buf.length + N - 1) / N)
    for (i <-0 until buf.length)
      groups(i / N) |= Base.toInt(buf(i)) << (i % N * S)
    new RNA1(groups, buf.length)
  }
  // RNAシーケンスを作る方法②
  def apply(bases: Base*) = fromSeq(bases)
}

// RNA Ver.2
final class RNA2 private (val groups: Array[Int], val length: Int)
  // 追加① IndexedSeqLikeトレイトは、IndexedSeqのすべての具象メソッドを拡張可能な形で実装
  // 例えば、take,drop,filter,init等のメソッドの結果型は、IndexedSeqLikeの第2パラメーターであり、RNA2自身になる。
  extends IndexedSeq[Base] with IndexedSeqLike[Base, RNA2] {
  import RNA2._
  // 追加② IndexSeqLikeトレイトのサブクラスは、自分の型のコレクションを返すようにnewBuilderをオーバーライトする必要がある。
  // RNA2クラスでは、newBuilderメソッドはBuilder[Base, RNA2]型のビルダーを返す。
  // ビルダーを作るために、ArrabyBuffer（中身は、Builder[Base, ArrayBuffer]）を作り、次にmapResultメソッドを
  // 呼び出してArrayBufferビルダーをRNA2ビルダーに変換する。
  // mapResultメソッドは、ArrayBufferからRNA2への変換関数（RNA2.fromSeq）がパラメーターとして指定されていることを想定している。
  override def newBuilder: Builder[Base, RNA2] =
    new ArrayBuffer[Base] mapResult fromSeq
  def apply(idx: Int): Base = {
    if (idx < 0 || length <= idx)
      throw new IndexOutOfBoundsException
    // groups配列から整数値を抜き出し、次に右シフト(>>)とマスク(&)をつかって、その整数から2ビットの数値を取り出す。
    Base.fromInt(groups(idx / N) >> (idx % N * S) & M)
  }
}

object RNA2 {
  private val S = 2             // グループを表現するために必要なビット数
  private val N = 32 / S        // Intに収まるグループの数
  private val M = (1 << S) - 1  // グループを分離するビットマスク（1ワードの最下位のSビットを取り出すためのビットマスク）

  // RNAシーケンスを作る方法①
  def fromSeq(buf: Seq[Base]): RNA2 = {
    val groups = new Array[Int]((buf.length + N - 1) / N)
    for (i <-0 until buf.length)
      groups(i / N) |= Base.toInt(buf(i)) << (i % N * S)
    new RNA2(groups, buf.length)
  }
  // RNAシーケンスを作る方法②
  def apply(bases: Base*) = fromSeq(bases)
}

// RNA Ver.Final
final class RNA private (val groups: Array[Int], val length: Int)
  extends IndexedSeq[Base] with IndexedSeqLike[Base, RNA] {
  import RNA._
  // 変更 IndexedSeqのnewBuilderに対する必須の再実装 ... newBuilderの実装がRNAクラスからコンパニオンオブジェクトに移動
  override protected[this] def newBuilder: Builder[Base, RNA] = RNA.newBuilder
  // IndexedSeqのapplyに対する必須の再実装
  def apply(idx: Int): Base = {
    if (idx < 0 || length <= idx)
      throw new IndexOutOfBoundsException
    // groups配列から整数値を抜き出し、次に右シフト(>>)とマスク(&)をつかって、その整数から2ビットの数値を取り出す。
    Base.fromInt(groups(idx / N) >> (idx % N * S) & M)
  }

  // 追加① 効率を上げるためのforeachのオプションの再実装
  override def foreach[U](f: Base => U): Unit = {
    var i = 0
    var b = 0
    while (i < length) {
      b = if (i % N == 0) groups(i / N) else b >>> S
      f(Base.fromInt(b & M))
      i += 1
    }
  }
}

object RNA {
  private val S = 2             // グループを表現するために必要なビット数
  private val N = 32 / S        // Intに収まるグループの数
  private val M = (1 << S) - 1  // グループを分離するビットマスク（1ワードの最下位のSビットを取り出すためのビットマスク）

  // RNAシーケンスを作る方法①
  def fromSeq(buf: Seq[Base]): RNA = {
    val groups = new Array[Int]((buf.length + N - 1) / N)
    for (i <-0 until buf.length)
      groups(i / N) |= Base.toInt(buf(i)) << (i % N * S)
    new RNA(groups, buf.length)
  }
  // RNAシーケンスを作る方法②
  def apply(bases: Base*) = fromSeq(bases)

  // 追加② RNAクラスから移動したnewBuilder
  def newBuilder: Builder[Base, RNA] = new ArrayBuffer mapResult fromSeq

  // 追加③ implicit付きのCanBuildFromの追加
  // このインスタンスは、引数としてRNA型と新しい要素型Baseを与えられると、RNA型の別のコレクションを構築できる。
  implicit def canBuildFrom: CanBuildFrom[RNA, Base, RNA] =
    new CanBuildFrom[RNA, Base, RNA] {
      def apply(): Builder[Base, RNA] = newBuilder
      def apply(from: RNA): Builder[Base, RNA] = newBuilder
    }

}
