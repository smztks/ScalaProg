package com.example.scalaprog

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/10/29
 * Time: 18:25
 * To change this template use File | Settings | File Templates.
 */

// 第30章 オブジェクトの等価性
object ScalaProg30

// Scalaにおける等価性
object Sp30_01 {
  /*
  ①Java
  ==メソッド ... （値型では自然な、参照型では参照）等価性の比較
  equalsメソッド ...参照型の値等価性
  例えばx, yの2つの文字列が同じ順序で同じ文字を含んでいた場合に、
  x == yはオブジェクトが異なるのでfalseになるのに対して、x equals yはtrueになる

  ②Scala
  ==メソッド ... 全ての型での「自然な」等価性の判定が使われる。
  値型ではJavaの==と同じ動作、参照型ではequalsと同じ意味で使われる

  equals（==も）メソッドはAnyから継承されるメソッドでデフォルトではeq(参照等価性)的な動作をする
   */

  println("### 1.var ###")
  // == & equals ... 値型
  println("# x, y")
  var x, y = 0
  println(x == y) // true ... 値型での等価性の一致
  println(x equals y) // true ... 値型での等価性の一致
  
  println("# x, z")
  var z = x
  x = 1
  println(x == z) // true ... 値型での等価性の不一致
  println(x equals z) // true ... 値型での等価性の不一致
  println("x: "+ x + " z: "+ z) // x: 1 z: 0

  println("### 2.List ###")
  // == & equals ... 値型
  println("# xs2, ys2")
  val xs2, ys2 = List(1, 2, 3)
  println(xs2 == ys2) // true ... 値型での等価性の一致
  println(xs2 equals ys2) // true ... 値型での等価性の一致
  println(xs2 eq ys2) // false ... 参照による等価性の不一致

  println("# xs2, zs2")
  val zs2 = xs2
  println(xs2 == zs2) // true ... 値型での等価性の一致
  println(xs2 equals zs2) // true ... 値型での等価性の一致
  println(xs2 eq zs2) // true ... 参照による等価性の一致

  println("### 3.Array ###")
  // == & equals ... 参照
  println("# xs1, ys1")
  val xs1, ys1 = Array[Int](5)
  xs1(0) = 1
  ys1(0) = 1
  println(xs1 == ys1) // false ... 参照による等価性の不一致
  println(xs1 equals ys1) // false ... 参照による等価性の不一致
  println(xs1 eq ys1) // false ... 参照による等価性の不一致

  println("# xs1, zs1")
  val zs1 = xs1
  xs1(0) = 2
  println(xs1 == zs1) // true ... 参照による等価性の一致
  println(xs1 equals zs1) // true ... 参照による等価性の一致
  println(xs1 eq zs1) // true ... 参照による等価性の一致
  println("xs: "+ xs1(0) + " zs: "+ zs1(0)) // xs: 2 zs: 2

  /*
  ==はAnyクラスでファイナルメソッドと定義されているので、直接オーバーライドすることはできない。
  final def == (that: Any): Boolean =
    if (null eq this) {null eq that} else {this equals that}
   */

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 等価メソッドの開発
object Sp30_02 {
  /*
  var hashSet: Set[C] = new collection.immutable.HashSet[Any]
  hashSet += elem1
  hashSet contains elems2 // falseを返す
   */

  /*
  equalsの実装によく見られる誤りとして以下の4つが挙げられる。
  1.誤ったシグネチャーでequalsを定義
  2.hashCodeに変更を加えずにequalsだけを定義
  3.ミュータブルなフィールドによってequalsを定義
  4.等価関係を表すものとしてequalsを定義出来ていない
   */

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 落とし穴1：誤ったシグネチャーでequalsを定義する (1)
object Sp30_02_01_a {

  import com.example.scalapkg._

  val p1, p2 = new Point1(1, 2)
  // p1: Point = Point@39b99d, p2: Point = Point@1c8f59c
  val q = new Point1(2, 3)
  // q: Point = Point@860315
  println(p1 equals p2)
  // res0: Boolean = true
  println(p1 equals q)
  // res1: Boolean = false

  import scala.collection.mutable._
  val coll = HashSet(p1)
  // coll: scala.collection.mutable.HashSet[Point] = Set(Point@39b99d)
  println(coll contains p2)
  // res2: Boolean = false

  val p2a: Any = p2
  // p2a: Any = Point@1c8f59c
  println(p1 equals p2a)
  // res3: Boolean = false

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 落とし穴1：誤ったシグネチャーでequalsを定義する (2)
object Sp30_02_01_b {

  import com.example.scalapkg._

  val p1, p2 = new Point2(1, 2)
  // p1: Point = Point@39b99d, p2: Point = Point@1c8f59c
  val q = new Point2(2, 3)
  // q: Point = Point@860315
  println(p1 equals p2)
  // res0: Boolean = true
  println(p1 equals q)
  // res1: Boolean = false

  import scala.collection.mutable._
  val coll = HashSet(p1)
  // coll: scala.collection.mutable.HashSet[Point] = Set(Point@39b99d)
  println(coll contains p2)
  // res2: Boolean = false

  val p2a: Any = p2
  // p2a: Any = Point@1c8f59c
  println(p1 equals p2a)
  // res3: Boolean = false ... Point2改良版では、true

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}
// 落とし穴2：hashCodeに変更を加えずequalsだけを変更する
object Sp30_02_02 {

  import com.example.scalapkg._

  val p1, p2 = new Point3(1, 2)
  // p1: Point = Point@39b99d, p2: Point = Point@1c8f59c
  val q = new Point3(2, 1)
  // q: Point = Point@860315
  println(p1 equals p2)
  // res0: Boolean = true
  println(p1 equals q)
  // res1: Boolean = false

  import scala.collection.mutable._
  val coll = HashSet(p1)
  // coll: scala.collection.mutable.HashSet[Point] = Set(Point@39b99d)
  println(coll contains p2)
  // res2: Boolean = false ... Point3改良版では、true
  println(coll contains q)
  // res2: Boolean = false ... Point3改良版でも、false

  val p2a: Any = p2
  // p2a: Any = Point@1c8f59c
  println(p1 equals p2a)
  // res3: Boolean = false ... Point2改良版では、true

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 落とし穴3：ミュータブルなフィールドによってequalsを定義する (1)
object Sp30_02_03a {

  import com.example.scalapkg._

  val p = new Point4a(1, 2)

  import scala.collection.mutable._
  val coll = HashSet(p)

  println(coll contains p)
  // true

  println("p.x: " + p.x + "/p.y: " + p.y)
  p.x += 1
  println("p.x: " + p.x + "/p.y: " + p.y)
  println(coll contains p)
  // false

  // イテレータで各要素ごとに評価するとtrueになる
  println(coll.iterator contains p)
  // true

  // xフィールドに変更を加えると、pが振り分けられるハッシュバケットが変わってしまったからである。
  // つまり、もとのハッシュバケットは、新しい値から計算されるハッシュコードに対応するものはない。
  // pはcoll集合の中に含まれているにも関わらず、collの「視界から外れた」とでもいうべき状態となる。

  // オブジェクトの現在の状態を考慮に入れた比較が必要なら、equalsではなく、別の名前を付ける必要がある。

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 落とし穴3：ミュータブルなフィールドによってequalsを定義する (2)
object Sp30_02_03b {

  import com.example.scalapkg._

  val p = new Point4b(1, 2)

  import scala.collection.mutable._
  val coll = HashSet(p)

  println(coll contains p)
  // true

  println("p.x: " + p.x + "/p.y: " + p.y)
  p.x += 1
  println("p.x: " + p.x + "/p.y: " + p.y)
  println(coll contains p)
  // true

  // イテレータで各要素ごとに評価するとtrueになる
  println(coll.iterator contains p)
  // true

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 落とし穴4：数学における同値関係を表すものとしてequalsを定義できていない (1)対称律
object Sp30_02_04a {
  /*
  equalsはnullでないオブジェクトどうしの数学的な同値関係(equivalence relation)を実装しなければならない。

  ①反射律を満たす：任意のnull以外の値xについて、x.equals(x)はtrueを返さなければならない。
  ②対称律を満たす：任意のnull以外の値x,yについて、x.equals(y)は、y.equals(x)がtrueを満たす場合に限り、
  　trueを返さなければならない。
  ③推移律を満たす：任意のnull以外のx,y,xについて、x.equals(y)がtrueを返し、y.equals(z)がtrueを返す場合、
  　x.equals(z)もtrueを返さなければならない。
  ④首尾一貫していなければならない：任意のnull以外の値x,yについて、x.equals(y)の複数回の呼び出しは、
  　equalsの比較に使われる情報が変更されない限り、一貫してtrueを返すか、一貫してfalseを返さなければならない。
  ⑤任意のnull以外の値xについて、x.equals(null)はfalseを返さなければならない。
   */

  import com.example.scalapkg._

  val p = new Point3(1, 2)
  val cp = new ColoredPoint1(1, 2, Color.Red)
  println((p equals cp) + " | " + (cp equals p))
  // true | false ... 「②対称律をを満たさない」
  // 「p equals cp」という比較は、pのequalsメソッド（Point3クラスで定義された「2つの点の座標しか考慮しない」機能）を実行する。
  // 「cp equals p」という比較は、cpのequalメソッド（ColoredPointクラスで定義された機能（pがColordPointでないのでfalse）を実行する。

  // 対象律を満たさなくなると、コレクションで予想外の結果を引き起こすことがある。
  import scala.collection.mutable._
  println((HashSet[Point3](p) contains cp) + " | " + (HashSet[Point3](cp) contains p))
  // true | false

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 落とし穴4：数学における同値関係を表すものとしてequalsを定義できていない (2)推移律 - 一般性を上げた対処方法
object Sp30_02_04b {

  import com.example.scalapkg._

  val p = new Point3(1, 2)
  val redp = new ColoredPoint2(1, 2, Color.Red)
  val bluep = new ColoredPoint2(1, 2, Color.Brue)

  // 対象律を満たす
  println((p equals redp) + " | " + (redp equals p))
  // true | true
  println((p equals bluep) + " | " + (bluep equals p))
  // true | true

  // 推移律は満たさない
  println((redp equals p) + " | " + (p equals bluep) + " | " + (bluep equals redp))
  // true | true | false

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 落とし穴4：数学における同値関係を表すものとしてequalsを定義できていない (3)推移律 - 厳密にする対処方法
object Sp30_02_04c {
  import com.example.scalapkg._

  val p = new Point5(1, 2)  // 技術的に誤りではないが、満足のいかないequalsメソッドに変更
  val redp = new ColoredPoint1(1, 2, Color.Red)
  val redp2 = new ColoredPoint1(1, 2, Color.Red)
  val redp3 = new ColoredPoint1(1, 2, Color.Red)
  val bluep = new ColoredPoint1(1, 2, Color.Brue)

  // 対象律を満たす
  println((p equals redp) + " | " + (redp equals p))
  // false | false
  println((p equals bluep) + " | " + (bluep equals p))
  // false | false
  println((redp equals redp2) + " | " + (redp2 equals redp))
  // true | true
  println((redp equals bluep) + " | " + (bluep equals redp))
  // false | false

  // 推移律を満たす
  println((redp equals p) + " | " + (p equals bluep) + " | " + (bluep equals redp))
  // false | false | false
  println((redp equals redp2) + " | " + (redp2 equals redp3) + " | " + (redp equals redp3))
  // true | true | true

  // クラス階層の等価関係を満たさない
  val pAnon = new Point5(1, 1) { override val y = 2 }
  println(p equals pAnon)
  // false ... pとpAnonに与えられたjava.lang.Classオブジェクトが異なるためfalse。
  // pはPoint5クラスだが、pAnonはPoint5の無名サブクラスとなっている。
  // しかし、pAnonは、(1,2)という座標を持つ点に過ぎず、pと異なるものとして扱うのは妥当でない。

  // 下記比較参考
  val pAnon2 = new Point5(1, 2)
  println(p equals pAnon2)
  // true

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 落とし穴4：数学における同値関係を表すものとしてequalsを定義できていない (3)クラス階層の等価関係の定義
object Sp30_02_04d {

  import com.example.scalapkg._

  val p = new Point6(1, 2)  // 技術的に誤りではないが、満足のいかないequalsメソッドに変更
  val redp = new ColoredPoint3(1, 2, Color.Red)
  val redp2 = new ColoredPoint3(1, 2, Color.Red)
  val redp3 = new ColoredPoint3(1, 2, Color.Red)
  val bluep = new ColoredPoint3(1, 2, Color.Brue)

  // 対象律を満たす
  println((p equals redp) + " | " + (redp equals p))
  // false | false
  println((p equals bluep) + " | " + (bluep equals p))
  // false | false
  println((redp equals redp2) + " | " + (redp2 equals redp))
  // true | true
  println((redp equals bluep) + " | " + (bluep equals redp))
  // false | false

  // 推移律を満たす
  println((redp equals p) + " | " + (p equals bluep) + " | " + (bluep equals redp))
  // false | false | false
  println((redp equals redp2) + " | " + (redp2 equals redp3) + " | " + (redp equals redp3))
  // true | true | true

  // クラス階層の等価関係を満たす
  val pAnon = new Point6(1, 1) { override val y = 2 }
  println(p equals pAnon)
  // true

  // 下記比較参考
  val pAnon2 = new Point6(1, 2)
  println(p equals pAnon2)
  // true

  val cp = new ColoredPoint3(1, 2, Color.Indigo)
  val coll = List(p)

  println(coll contains p)
  // true
  println(coll contains cp)
  // false ... Pointが期待されている場所でColoredPointを使うことができないため
  // 同じ座標でも色が違う点（pは無色と仮定する）は異なるものと定義
  println(coll contains pAnon)
  // true
  
  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// パラメーター化された型の等価性の定義 (1)
object Sp30_03a {

  import com.example.scalapkg._

  // Tのパターン型がチェックできませんという趣旨の警告
  // 19章で説明した、パラメーター化された型の要素型は、コンパイラーによって消去されるので、実行時にはチェックできない
  // : non variable type-argument T in type pattern com.example.scalaprog.Sp30_03.Branch[T] is unchecked since it is eliminated by erasure
  // case that: Branch[T] => this.elem == that.elem &&
  //            ^

  // 2個のBranchを比較するために要素型をチェックする必要ない。フィールドが等しい限り、要素型の異なる2個の
  // Branchを等価と見なすことに問題はない。

  val b1 = new Branch1[List[String]](Nil, EmptyTree, EmptyTree)
  val b2 = new Branch1[List[Int]](Nil, EmptyTree, EmptyTree)
  println(b1 == b2)
  // true

  /*
  型パラメータがコンパイル時にしか存在しないモデル ... Scalaは型消去モデルを採用している
    上記b1, b2の2つのBranchが等しいと見るのが自然
  型パラメータをオブジェクトの値の一部とするモデル
    上記b1, b2の2つのBranchは異なると見るのが自然
   */

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// パラメーター化された型の等価性の定義 (2)
object Sp30_03b {

  import com.example.scalapkg._

  val b1 = new Branch2[List[String]](Nil, EmptyTree, EmptyTree)
  val b2 = new Branch2[List[Int]](Nil, EmptyTree, EmptyTree)
  println(b1 equals b2)
  // false

  import scala.collection.mutable._
  val coll = HashSet(b1)
  println(coll contains b1)
  // true
  //println(coll contains b2)
  // type mismatch;

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// equalsとhashCodeのレシピ
object Sp30_04 {
  
  import com.example.scalapkg._
  
  val r1 = new RationalEq(1,2) // Rationalから変更
  val r2 = new RationalEq(1,2)
  val r3 = new RationalEq(1,3)
  println(r1 + " | "+ r2 + " | "+ r3)
  // 1/2 | 1/2 | 1/3

  println(r1 equals r2)
  // false -> true
  println(r1 equals r3)
  // false

  import scala.collection.mutable._
  val coll = HashSet(r1)
  println(coll contains r1)
  // true
  println(coll contains r2)
  // false -> true
  println(coll contains r3)
  // false

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}
