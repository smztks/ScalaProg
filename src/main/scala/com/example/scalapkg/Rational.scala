package com.example.scalapkg

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/10/19
 * Time: 10:50
 * To change this template use File | Settings | File Templates.
 */

///////////////////////////////////////////////////////////////////////////////////////////////////
// 12章-12.4 Orderedトレイト
class Rational(n: Int, d: Int) extends Ordered[Rational] {
  require(d != 0)
  private val g = gcd(n.abs, d.abs)
  val numer = n / g
  val denom = d / g
  def this(n: Int) = this(n, 1)

  def +(that: Rational): Rational =
    new Rational(
     numer * that.denom + numer * denom,
     denom * that.denom
    )
  def +(i: Int): Rational =
    new Rational(numer + i * denom, denom)

  def -(that: Rational): Rational =
    new Rational(
     numer * that.denom - numer * denom,
     denom * that.denom
    )
  def -(i: Int): Rational =
    new Rational(numer - i * denom, denom)

  def *(that: Rational): Rational =
    new Rational(numer * that.numer, denom * that.denom)
  def *(i: Int): Rational =
    new Rational(numer * i, denom)

  def /(that: Rational): Rational =
    new Rational(numer * that.denom, denom * that.numer)
  def /(i: Int): Rational =
    new Rational(numer, denom * i)

  override def toString = numer + "/" + denom
  private def gcd(a: Int, b: Int): Int =
    if(b == 0) a else gcd(b, a % b)

  /*** 比較演算子使う用の定義(Orderedトレイト使う準備) ***/
  def compare(that:Rational) =
    (this.numer * that.denom) - (that.numer * this.denom)
}

///////////////////////////////////////////////////////////////////////////////////////////////////
// 20章-5 抽象valの初期化 RationalTrait
trait RationalTrait1 {
  val numerArg: Int
  val denomArg: Int
  // RationalTraitクラスが初期化された時にdenomArgの値が0（Int型のデフォルト値）で生成される為、
  // require呼び出しがエラーを起こす。
  require(denomArg != 0)
  private val g = gcd(numerArg, denomArg)
  val numer = numerArg / g
  val denom = denomArg / g
  private def gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b, a % b)
  override def toString = numer + "/" + denom
}
/*
単純なリテラルでない分子／分母式でこのトレイトを初期化しようとすると、例外が投げられる。
val x = 2
new RationalTrait {
  val numerArg = 1 * x
  val denomArg = 2 * x
}
*/
// error: Caused by: java.lang.IllegalArgumentException: requirement failed

// 名前付きのサブクラス
class RationalClass(n: Int, d: Int) extends {
  val numerArg = n
  val denomArg = d
  // val denomArg = this.numerArg * d ... error: value numerArg is not a member of object
} with RationalTrait1 {
  // 有理数の加法メソッドですな
  def + (that: RationalClass) = new RationalClass(
    numer * that.denom + that.numer * denom,
    denom * that.denom
  )
  def outputArg = {
    println(numer + "/" + denom)
  }
}

// 遅延評価valによるトレイトの初期化
trait RationalTrait2 {
  val numerArg: Int
  val denomArg: Int
  // RationalTraitクラスが初期化された時にdenomArgの値が0（Int型のデフォルト値）で生成される為、
  // require呼び出しがエラーを起こす。
  lazy val numer = numerArg / g // gの値はオンデマンドで初期化されるので、gの初期化処理が後ろに書かれていても問題ない。
  lazy val denom = denomArg / g
  private lazy val g = {
    require(denomArg != 0)
    gcd(numerArg, denomArg)
  }
  private def gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b, a % b)
  override def toString = numer + "/" + denom
}

///////////////////////////////////////////////////////////////////////////////////////////////////
// 30章-4 eqaulsとhashCodeが定義されたRationalクラス
class RationalEq(n: Int,  d: Int) {
  require(d != 0)
  private val g = gcd(n.abs, d.abs)
  var numer = (if (d < 0) -n else n)  /g  // すべての分数が正の分母を持つように正規化
  var denom  = d.abs /g
  private def gcd(a: Int,  b: Int):Int =
    if (b == 0) a else gcd(b, a % b)
  override def equals(other: Any): Boolean =
    other match {
      // オーバーライドしようとしているequalsメソッドがAnyRefのものでない場合には、
      // スーパークラスのメソッドを呼び出しを組み込む。例：super.equals(that) &&
      // 初めてcanEqualsを導入するためにequalsを定義している場合は、thisを引数として、
      // equalsの引数になっている値のcanEqualメソッドを呼び出す。例：(that canEqual this) &&
      case that: RationalEq => (that canEqual this) &&
                                numer == that.numer &&
                                denom == that.denom
      case _ => false
    }
  // ファイナルクラスでないクラスでequalsをオーバーライトするときには、canEqualを定義します
  def canEqual(other: Any): Boolean =
    other.isInstanceOf[RationalEq]
  override def hashCode: Int = {
    println("hashCode: " +
      41 * (
        41 + numer
      ) + denom
    )
    41 * (
      41 + numer
    ) + denom
  }
  override def toString =
    if (denom == 1) numer.toString else numer + "/" + denom
}
