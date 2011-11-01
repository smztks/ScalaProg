package com.example.scalaprog

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/10/20
 * Time: 14:00
 * To change this template use File | Settings | File Templates.
 */

// 第21章 暗黙の型変換とパラメーター
object ScalaProg21

// 暗黙の型変換
object Sp21_01 {
  import java.awt.event.ActionEvent
  import java.awt.event.ActionListener
  import javax.swing.JButton

  val button = new JButton

  // ①通常
  button.addActionListener(
    new ActionListener {
      def actionPerformed(event: ActionEvent) {
        println("pressed!")
      }
    }
  )

  // ②関数からアクションリスナーへの型変換
  def functional2ActionListener1(f: ActionEvent => Unit) =
    new ActionListener {
      def actionPerformed(event: ActionEvent) = f(event)
    }

  button.addActionListener(
    functional2ActionListener1(
      (_: ActionEvent) => println("pressed!")
    )
  )

  // ③ 暗黙:implicit修飾子を付ける
  implicit def functional2ActionListener2(f: ActionEvent => Unit) =
    new ActionListener {
      def actionPerformed(event: ActionEvent) = f(event)
    }

  // 暗黙の型変換を行わず下記コードを実行した場合、addActionListenerは引数としてアクションリスナーを
  // 求めているのに、関数を与えているから。
  // 「functional2ActionListener」はimplicitが付いているので、functional2ActionListener2の
  // 呼び出しは省略してもコンパイラーが自動的に挿入してくれる。
  button.addActionListener(
    (_: ActionEvent) => println("pressed!")
  )


  println("abc123" exists (_.isDigit))
  /*
  // implicit修飾子による暗黙の型変換を行なうWrapperを定義します
  implicit def stringWrapper(s: String) = {
    // StringにもかかわらずRandomAccessSeq[Char]に変換します
    new RandomAccessSeq[Char] {
      // 足りないメソッドを追加してやります
      def length = s.length
      def apply(i:Int) = s.charAt(i)
    }
  }
  println(stringWrapper("abc123") exists (_.isDigit))
  */

  def printWithSpaces(seq: IndexedSeq[Char]) = seq mkString " "
  println(printWithSpaces("abcde"))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// implicitの規則
object Sp21_02 {
  //implicit def intToString(x: Int) = x.toString

  class AAA(val n: Int) {
    println("class AAA")
    def addA = n + "a"
  }
  class BBB(val n: String) {
    println("class BBB")
    def addB = n + "b1"
  }
  class CCC(val n: String) {
    println("class CCC")
    def addC = n + "c"
    def addB = n + "b2" // 暗黙変換の複数の候補に同じメソッドがあると、解決できなくてエラー
                        // importにて指定した暗黙の型変換のみに絞りこめば、問題ない
  }

  // 暗黙変換では引数の型と戻り値の型が必要で、関数名は使わないのでわかりやすい任意の名称でよい。
  object MyConv {
    implicit def int2a(n: Int) = new AAA(n)
    implicit def str2b(n: String) = new BBB(n)
    implicit def str2c(n: String) = new CCC(n)
  }
  import MyConv.{int2a,str2c}

  println(111)
  println("---")
  println(111 addA)
  println()

  println("222")
  println("---")
  println("222" addB)
  println()

  println("333")
  println("---")
  println("333" addC)

  // 暗黙の型変換のインポート
  object MyConversions {
    implicit def stringWrapper(s: String): IndexedSeq[Char] = null
    implicit def intToString(x: Int): String = null
  }
  import MyConversions.stringWrapper

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 要求された方への暗黙の型変換
object Sp21_03 {
  implicit def doubleToInt(x: Double): Int = x.toInt

  val i1: Int = 3.5
  val j1: Double = doubleToInt(3.5)
  println(i1 + "|" + j1)

  val x: Int = 3
  val i2: Double = x
  //scala.Predefオブジェクトには、implicit def intToDouble(x: Int): Double = x.toDouble のような変換がある。
  println(i2)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// レシーバの変換
object Sp21_04 {
  import com.example.scalapkg._

  val oneHalf = new Rational(1,2)
  println(oneHalf)
  println(oneHalf + oneHalf)
  println(oneHalf + 1)
  //println(1 + oneHalf) // Error: overloaded method value + with alternatives:

  implicit def intToRational(x: Int) = new Rational(x, 1)
  println(1 + oneHalf)  // IntからRational型への暗黙の型変換でエラー回避
  println(intToRational(1) + oneHalf)
  println(new Rational(1, 1) + oneHalf)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 暗黙のパラメータ(1)
object Sp21_05_01 {
  class PreferredPrompt(val preference: String)

  object Greeter {
    def greet(name: String)(implicit prompt: PreferredPrompt) {
      println("Welcome, " + name + ". The system is ready.")
      println(prompt.preference)
    }
  }

  // プロンプト文字列を明示的に指定している。
  val bobsPrompt = new PreferredPrompt("relax> ")
  Greeter.greet("Bob")(bobsPrompt)

  //Greeter.greet("Joe")  // Error: could not find implicit value for parameter prompt: PreferredPrompt

  object JoesPrefs {
    implicit val prpt = new PreferredPrompt("Yes, master> ")
  }
  import JoesPrefs._
  Greeter.greet("Joe")  // JoesPrefsオブジェクトをインポートし、カリー化された第二パラメータリストに暗黙の型変換が適用されたのでOK

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 暗黙のパラメータ(2) 複数のパラメーターを持つ暗黙のパラメーターリスト
object Sp21_05_02 {
  class PreferredPrompt(val preference: String)
  class PreferredDrink(val preference: String)

  object Greeter {
    def greet(name: String)(implicit prompt: PreferredPrompt, drink: PreferredDrink) {
      println("Welcome, " + name + ". The system is ready.")
      print("But while you work, ")
      println("why not enjoy a cup of " + drink.preference + "?")
      println(prompt.preference)
    }
  }

  object JoesPrefs {
    implicit val prpt = new PreferredPrompt("Yes, master> ")  // 暗黙の型変換時に偶然の一致がおきないように通常使わない特別な型を使うようにする。
    implicit val drnk = new PreferredDrink("tea")               // PreferredPromptやPreferredDrinkが特別な型となっている。
  }

  import JoesPrefs._
  Greeter.greet("Joe")(prpt, drnk)
  Greeter.greet("Joe")
  Greeter.greet("Joe")( new PreferredPrompt("No, sir> "), new PreferredDrink("beer"))
  Greeter.greet("Joe")( new PreferredPrompt("No, sir> "), drnk)
  // Greeter.greet("Joe")("No, sir> ", "beer") //特別な型(PreferredPrompt, PreferredDrink)を使わずにgreetをコールしてエラーとなる。

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 暗黙のパラメータ(3) 上限境界を使っている関数
object Sp21_05_03 {

  // ①要素型がOrderedのサブ型になっていないリストではこの関数は使えない。
  // 上限境界で型を制限しますよ
  def maxListUpBound[T <: Ordered[T]](elements: List[T]): T =
    elements match {
      // 空リストは例外エラーです
      case List() => throw new IllegalArgumentException("empty list!")
      // 要素がひとつだけだったら返します
      case List(x) => x
      // 複数要素がある場合
      case x :: rest => {
        // 再帰的に処理しますね
        val maxRest = maxListUpBound(rest)
        // 再帰処理の結果大きい方を返します
        if(x > maxRest) x else maxRest
    }
  }

  // ②引数List[T]のほかに、第2引数リストとしてTをOrdered[T]に変換する関数をとるようにすれば、汎用的に使える。
  // T型の順序関係を明らかにするパラメータを追加します
  def maxListImpParm[T](elements: List[T])(implicit orderer: T => Ordered[T]): T =
    elements match {
      case List() => throw new IllegalArgumentException("empty list!")
      case List(x) => x
      case x :: rest => {
        // 再帰的に処理デス
        //第2パラメータリストにて、「T => Ordered[T]」になる。
        // maxRestは再帰maxListImpParmの最終段階では「T => Ordered[T]」のxが戻り値となる。
        val maxRest = maxListImpParm(rest)(orderer)
        // 単体要素もOrdered[T]型に変換orderer(x)します
        if(orderer(x) > maxRest) x else maxRest
    }
  }

  println(maxListImpParm(List(1, 3, 15, 2)))  // 第2パラメータリストは暗黙の型変換（orderer）が隠れている。
  println(maxListImpParm(List(1.3, 10.4, 1.9, 3.2)))
  println(maxListImpParm(List("one", "two", "three")))
  println(maxListImpParm(List(1.3, 10.4, 1.9, 3.2, 1))) // IntとDoubleの混在はOK

  // 特殊な名前の型を使わないで書いてみます
  // ordererの呼び出し元は(T, T) => Boolean型のordererパラメータの供給が必要となり、
  // 汎用性の高い型となってテストが困難
  //def maxListPoorStyle[T](elements:List[T])(implicit orderer: (T, T) => Boolean): T

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 可視境界
object Sp21_06 {

  // ①(orderer)の省略
  def maxList1[T](elements: List[T])(implicit orderer: T => Ordered[T]): T =
    elements match {
      case List() => throw new IllegalArgumentException("empty list!")
      case List(x) => x
      case x :: rest =>
        val maxRest = maxList1(rest) // (orderer)を暗黙の型変換で使用した為、省略
        if (x > maxRest) x else maxRest
    }
  println(maxList1(List(1, 3, 15, 2)))

  // ②implicit自体の省略
  // [T]型宣言部分に「 <% Ordered[T]」を追加し、「(implicit orderer: T => Ordered[T])」を削除
  // 「T <% Ordered[T]」の意味は、「Ordered[T]として扱える任意のTを使える。
  def maxList2[T <% Ordered[T]](elements: List[T]): T =
    elements match {
      case List() => throw new IllegalArgumentException("empty list!")
      case List(x) => x
      case x :: rest =>
        val maxRest = maxList2(rest) // (orderer)を暗黙の型変換で使用した為、省略
        if (x > maxRest) x else maxRest
    }
  println(maxList2(List(1, 3, 15, 2)))

  // なお、可視境界適用パターンでの暗黙の型変換はPredefで宣言されている次の暗黙の恒等関数を利用
  // 単純に渡されたオブジェクトをそのまま返します
  // implicit def identity[A](x:A): A = x

  // 上限境界(<:)よりも可視境界(<%)を使っている関数の方が、多くの型を操作できる

  /*
  上限境界
    記法: T <: Ordered[T]
    TがOrdered[T]のサブ型であることを要求
  可視境界
    記法: T <% Ordered[T]
    TがOrdered[T]として（暗黙の型変換を使う）扱えることを要求

   */
  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 複数の型変換を適用できるとき
object Sp21_07 {
  def printLength(seq: Seq[Int]) = println(seq.length)  // Seq=シーケンストレイト
  implicit def intToRange(i: Int) = 1 to i // Range(1,2,3,...12)
  //implicit def intToDigits(i: Int) = i.toString.toList.map(_.toInt) // List(49, 50)
  implicit def intToDigits(i: Any) = i.toString.toList.map(_.toInt)
  // Int -> AnyにしたらIntより限定的でなくなり、intToRangeが優先される。

  printLength(12) // Error: Note that implicit conversions are not applicable because they are ambiguous:

  /*
  println(intToRange(12))
  println(intToDigits(12))  // Int -> string -> toInt（文字コードの数字）でマッピング（変換）する。

  println(12.toString.toList.map(_.toInt))
  println("ab".toString.toList.map(_.toInt))
   */

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 暗黙の型変換のデバッグ
object Sp21_08 {
  //val chars: String = "xyz" // OK
  //val chars: List[Char] = "xyz" // Error: type mismatch;

  //val chars: String  = wrapString("xyz")  // Ok wrapString("xyz") 戻り値は、String
  //val chars: List[Char] = wrapString("xyz")  // Error: type mismatch;

  object Mocha extends Application {
    class PreferredDrink(val preference: String)
    implicit val pref = new PreferredDrink("mocha")
    def enjoy(name: String)(implicit drink: PreferredDrink) {
      print("Welcome, " + name)
      print(". Enjoy a ")
      print(drink.preference)
      println("!")
    }

    enjoy("reader")
    // 下記オプション「-Xprint:typer」をつけることにより、
    // Mocha.this.enjoy("reader")(Mocha.this.pref)として暗黙の型変換が行われたことがわかる。
  }

  /*
  scalac -Xprint:typer mocha.scala

  [[syntax trees at end of typer]]// Scala source: mocha.scala
  package <empty> {
    final object Mocha extends java.lang.Object with Application with ScalaObject {
      def this(): object Mocha = {
        Mocha.super.this();
        ()
      };
      class PreferredDrink extends java.lang.Object with ScalaObject {
        <paramaccessor> private[this] val preference: String = _;
        <stable> <accessor> <paramaccessor> def preference: String = PreferredDrink.this.preference;
        def this(preference: String): Mocha.PreferredDrink = {
          PreferredDrink.super.this();
          ()
        }
      };
      private[this] val pref: Mocha.PreferredDrink = new Mocha.this.PreferredDrink("mocha");
      implicit <stable> <accessor> def pref: Mocha.PreferredDrink = Mocha.this.pref;
      def enjoy(name: String)(implicit drink: Mocha.PreferredDrink): Unit = {
        scala.this.Predef.print("Welcome, ".+(name));
        scala.this.Predef.print(". Enjoy a ");
        scala.this.Predef.print(drink.preference);
        scala.this.Predef.println("!")
      };
      Mocha.this.enjoy("reader")(Mocha.this.pref)
    }
  }
   */

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}
