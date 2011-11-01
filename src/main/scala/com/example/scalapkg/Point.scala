package com.example.scalapkg

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/10/30
 * Time: 11:38
 * To change this template use File | Settings | File Templates.
 */

// 初期Pointクラス
@deprecated("use `Point2' instead of `Point1'")
class Point1(val x: Int, val y: Int) {
  def equals(other: Point1): Boolean =
    this.x == other.x && this.y == other.y
}

// 30.2.1 改良版 ... 正しいシグネチャを使用
// Point1の定義ではequalsのオーバーライドしていないのでequalsのパラメータはPoint型しかとらないため、
// p2a:Anyを引数としたことでAny型（大元）のequalsが呼ばれて比較に失敗 -> 改良
@deprecated("use `Point3' instead of `Point2'")
class Point2(val x: Int, val y: Int) {
  override def equals(other: Any) =
    other match {
      case that: Point2 => this.x == that.x && this.y == that.y
      case _ => false
    }
  // def  ==(other: Any):Boolean = false  // Error: overriding method == in class Any of type (x$1: Any)Boolean;
  // def  ==(other: Point):Boolean = false // Anyクラスにおける同名のメソッドを多重定義したものとしてコンパイルを通る
  // ただし、プログラムの動作は、誤ったシグネチャーでequalsを定義したときと同じようにおかしくなる。
}

// 30.2.2 改良版 ...正しいHashCodeを使用
// 問題は、Anyクラスで定義されていたHashCodeについての次の契約をPointクラスの実装が破っていた。
// 契約：2つのオブジェクトがequalsメソッドの基準で等しい場合、2つのオブジェクトをレシーバーとして
// hashCodeを呼び出したときの結果値は、同じ整数値でなければならない。
// ... 同じ座標のものは同じ整数値をもつようにhashCodeを定義
@deprecated("use `Point6' instead of `Point3'")
class Point3(val x: Int, val y: Int) {
  // これは、HashCodeの実装のうちのほんの一例にすぎない。
  // xフィールドに41という定数を加え、素数である41を掛けた上でもう1つの整数フィールドのyを加えると、
  // 実行時間やコードサイズの面でコストかけずに、適度に分散したハッシュコードを得られる。
  override def hashCode = {
    println("x: " + x + "/y: " + y + " hashCode: " +  (41 * (41 + x) + y))
    41 * (41 + x) + y
    /*
    下記、座標が、x=1,y=2 と x=2,y=1の場合に同じハッシュコードが生成されるように変更しても、
    equalsメソッドが等しくならないのでcontainsはfalseとなる。
    println("x: " + x + "/y: " + y + " hashCode: " +  (x + y))
    x + y
     */
  }
  override def equals(other: Any) =
    other match {
      case that: Point3 => this.x == that.x && this.y == that.y
      case _ => false
    }
}

// 30.2.3 改悪版 .., ミュータブルなフィールドを使用
@deprecated("use `Point3' instead of `Point4'")
class Point4a(var x: Int, var y: Int) {
  override def hashCode =  {
    println("x: " + x + "/y: " + y + " hashCode: " +  (41 * (41 + x) + y))
    41 * (41 + x) + y
  }
  override def equals(other: Any) =
    other match {
      case that: Point4a => this.x == that.x && this.y == that.y
      case _ => false
    }
}

// 30.2.3 ミュータブルを使ったオブジェクトの現在の状態を考慮に入れた比較が必要な場合
class Point4b(var x:Int, var y:Int){
  //hashCode, equalsはオーバーライドしません
  // かわりにequalsとは異なる等価比較メソッドを用意してやります
  def equalsContent(other: Any) =
    other match {
      case that: Point4b => this.x == that.x && this.y == that.y
      case _ => false
    }
}

// 30.2.4 改悪版 ...オブジェクトどおしの数学的な同値関係が実装されない
// 列挙型Color
object Color extends Enumeration {
  val Red, Orange, Yellow, Green, Brue, Indigo, Violet = Value
}

// equalsが対称律を満たさない
@deprecated("use `ColoredPoint3' instead of `ColoredPoint1'")
class ColoredPoint1(x: Int, y: Int, val color: Color.Value) extends Point3(x, y) {
  override def equals(other: Any) =
    other match {
      case that: ColoredPoint1 => (this.color == that.color) && super.equals(that)
      case _ => false
    }
}

// equalsが推移律を満たさない ... equalの一般性を上げた対処方法
@deprecated("use `ColoredPoint1' instead of `ColoredPoint2'")
class ColoredPoint2(x: Int, y: Int, val color: Color.Value) extends Point3(x, y) {
  override def equals(other: Any) =
    other match {
      case that: ColoredPoint2 => super.equals(that) && (this.color == that.color)
      case that: Point3 => that equals this // 追加：パラメーターのオブジェクトがColoredPoit2aではなく、Point3である場合、Point3のequalsを呼び出す
      case _ => false
    }
}

// equalsが推移律を満たさない ... equalの厳密にする対処方法
// Pointクラスのインスタンスは、オブジェクトの座標が同じで実行時クラスが等しいとき（両方のオブジェクトのgetClassメソッドが
// 同じ値を返してきたとき）に、Pointの他のインスタンスと等しいとみなされる。
// 異なるクラスに属するオブジェクトの比較は常にふfalseになるので、対称律、推移律の両方を満足させる。
// しかし、クラス階層の等価関係の定義が異なる為、対称律が異なってしまう場合がある。
@deprecated("use `Point6' instead of `Point5'")
class Point5(val x: Int, val y: Int) {
  override def hashCode =  {
    println("x: " + x + "/y: " + y + " hashCode: " +  (41 * (41 + x) + y))
    41 * (41 + x) + y
  }
  override def equals(other: Any) =
    other match {
      // 「that.y && this.getClass == that.getClass」の演算を追加
      case that: Point5 => this.x == that.x && this.y == that.y && this.getClass == that.getClass
      case _ => false
    }
}

// クラス階層の等価関係を正しく再定義する ... 最終バージョン
class Point6(val x: Int, val y: Int) {
  override def hashCode =  {
    println("Point6 - x: " + x + "/y: " + y + " hashCode: " +  (41 * (41 + x) + y))
    41 * (41 + x) + y
  }
  override def equals(other: Any) =
    other match {
      // 「that canEqual this」の演算を追加
      case that: Point6 => (that canEqual this) && (this.x == that.x) && (this.y == that.y)
      case _ => false
    }
  // otherオブジェクトがcanEqualを定義（再定義）したクラスのインスタンスならtrueを返す。
  // クラスがequals（およびhashCode）を再定義するとと同時に、クラスの異なるequal実装を持つ
  // スーパークラスのオブジェクトとは等しくならない仕様とする。
  // isInstanceOf：指定した型と互換性があるか評価する
  def canEqual(other: Any) = other.isInstanceOf[Point6]
}

class ColoredPoint3(x: Int, y: Int, val color: Color.Value) extends Point6(x, y) {
  override def hashCode = {
    println("ColoredPoint3 - x: " + x + "/y: " + y + " hashCode: " +  (41 * super.hashCode + color.hashCode))
    41 * super.hashCode + color.hashCode
  }
  override def equals(other: Any) =
    other match {
      case that: ColoredPoint3 => (that canEqual this) && super.equals(that) && (this.color == that.color)
      case _ => false
    }
  override def canEqual(other: Any) = other.isInstanceOf[ColoredPoint3]
}