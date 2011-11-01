package com.example.scalapkg

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/10/20
 * Time: 11:18
 * To change this template use File | Settings | File Templates.
 */

/*
// ①Currencyクラスの最初の（誤りがある）設定
// 他通貨を加算できてしまうので個々の通貨に特化した+メソッドが必要...しかし通貨単位で作ることになり煩雑になる。
abstract class Currency {
  val amount: Long  // amount:金額
  def designation: String // designation:通貨単位
  override def toString = amount + " " + designation
  def + (that: Currency): Currency =
  def * (x: Double): Currency =
}
new Currency {
  val amont = 79L
  def designation = "USD"
}

// ②Currencyクラスの第2の（まだ不完全な）設計
// +メソッドで定義したCurrencyクラスが問題である。
// Scalaの抽象型には、インスタンスを作ったり、他のクラスのスーパー型にはできない制限がある。
// ファクトリーメソッドを使えば解決できるが、外部からファクトリーメソッドを操作できてしまうし、
// 最初のCurrencyオブジェクトを手に入れるため、別の作成メソッドが必要となる…怪しげなコードになってしまう前兆
abstract class AbstractCurrency {
  type Currency <: AbstractCurrency
  //def make(amount: Long) : Currency // ファクトリーメソッド ... 不採用
  val amount: Long  // amount:金額
  def designation: String // designation:通貨単位
  override def toString = amount + " " + designation
  // error: class type required but AbstractCurrency.this.Currency found
  def + (that: Currency): Currency = new Currency {
    val amount = this.amount + that.amount
  }
  def * (x: Double): Currency =
}
abstract class Dollar extends AbstractCurrency {
  type Currency  = Dollar
  def designation = "USD"
}
*/

// ③抽象型とファクトリメソッドをAbstractCurrencyクラスの外に出す設計
abstract class CurrencyZone {
  type Currency <: AbstractCurrency
  def make(amount: Long) : Currency // ファクトリーメソッド
  val CurrencyUnit : Currency // 標準通貨定義

  abstract class AbstractCurrency {
    val amount: Long  // amount:金額単位
    def designation: String // designation:通貨単位

    def + (that: Currency): Currency =
      make(this.amount + that.amount)
    def * (x: Double): Currency =
      make((this.amount * x).toLong)
    def * (that: Currency) = {
      println("[*]" + (this.amount/CurrencyUnit.amount).toDouble + "|" + (that.amount/CurrencyUnit.amount))
      (this.amount/CurrencyUnit.amount).toDouble * (that.amount/CurrencyUnit.amount)
    }
    def - (that: Currency): Currency =
      make(this.amount - that.amount)
    def / (that: Double): Currency =
      make((this.amount / that).toLong)
    def / (that: Currency) = {
      println("[/]" + this.amount.toDouble + "|" + that.amount)
      this.amount.toDouble / that.amount
    }
    def from(other: CurrencyZone#AbstractCurrency): Currency =  // P.390 パス依存型 型パラメータはCurrencyZoneのAbstractCurrency型
      make(math.round(other.amount.toDouble * Converter.exchangeRate(other.designation)(this.designation)))

    // ex. decimals(10)=1, decimals(100)=2 ... 小数点以下の桁数を算出
    def decimals(n: Long): Int =
      if (n == 1) 0 else 1 + decimals(n / 10)
    // override def toString = amount + " " + designation
    override def toString =
      ((amount.toDouble / CurrencyUnit.amount.toDouble)
      formatted ("%." + decimals(CurrencyUnit.amount) + "f") + " D:" + designation + " A:" + CurrencyUnit.amount)
    // CurrencyUnit.amount=100  }
  }
}

object US extends CurrencyZone {
  abstract class Dollar extends AbstractCurrency {
    def designation = "USD"
  }
  type Currency = Dollar
  def make(cents: Long) = new Dollar {
    val amount = cents
  }
  val Cent = make(1)  // 1セントは1セント Dollar型になる
  val Dollar = make(100)  // 1ドルは100セント Dollar型になる
  val CurrencyUnit = Dollar
  // ドルが標準通貨単位であることを宣言 ... US.make(100)で1ドルということ
}

object Europe extends CurrencyZone {
  abstract class Euro extends AbstractCurrency {
    def designation = "EUR"
  }
  type Currency = Euro
  def make(cents: Long) = new Euro {
    val amount = cents
  }
  val Cent = make(1)
  val Euro = make(100)
  val CurrencyUnit = Euro
}

object Japan extends CurrencyZone {
  abstract class Yen extends AbstractCurrency {
    def designation = "JPY"
  }
  type Currency = Yen
  def make(yen: Long) = new Yen {
    val amount = yen
  }
  val Yen = make(1)
  val CurrencyUnit = Yen
}

object  Converter {
  // なんでvar?
  var exchangeRate = Map(
    "USD" -> Map("USD" -> 1.0,    "EUR" -> 0.7596, "JPY" -> 1.211,  "CHF" -> 1.223),
    "EUR" -> Map("USD" -> 1.316 , "EUR" -> 1.0   , "JPY" -> 1.594 , "CHF" -> 1.623),
    "JPY" -> Map("USD" -> 0.8257, "EUR" -> 0.6272, "JPY" -> 1.0   , "CHF" -> 1.018),
    "CHF" -> Map("USD" -> 0.8108, "EUR" -> 0.6160, "JPY" -> 0.982 , "CHF" -> 1.0  )
  )
}

