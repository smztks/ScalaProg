package com.example.scalaprog

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/10/28
 * Time: 13:03
 * To change this template use File | Settings | File Templates.
 */

// 第27章 アノテーション
object ScalaProg27

// アノテーションを使う理由
object Sp27_01 {
  /*
  プログラムは、コンパイル実行以外にも多くの用途がある。
  ①Scaladocなどのドキュメントの自動生成
  　ドキュメントジェネレーターには、一部のメソッドを「使わない方がよいもの」として記述するよう指示できる
  ②好みのスタイルに合わせたコードの出力
  　出力整形プログラムには、手作業でていねいに整形してある部分を処理しないように指示できる
  ③ファイルをオープンしながら、クローズしない場合があるなど、よくあるエラーのチェック
  　クローズされていないファイルのチェックプログラムには、クローズされていることを別途確認してあるファイルを
  　無視するよう指示できる
  ④副作用の管理や所有者プロパティの確認などの目的とした実験的な型チェック
　　副作用チェッカーには、指示されたメソッドに副作用がないことを確かめるよう指示できる
   */

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// アノテーションの構文
object Sp27_02 {
  
  // @deprecated = このメソッドを使わないで欲しいという印
  @deprecated def bigMistake() = null

  // アノテーションが付けられるのは、メソッド以外に、val,var,def,class,object,trait,typeの宣言および定義
  @deprecated class QuickAndDirty {
    // ...
  }

  // 式にも適用できる。
  val e = null
  (e: @unchecked) match {
    case _ =>
    // 徹底的なチェックは不要の場合に、@uncheckedを付ける。
  }

  // アノテーションの一般形
  // @<アノテーション>(<式>1, <式>2, ... )
  // @cool val normal = "Hello"
  // @coolerThan(normal) val fonzy = "Heeyyy"

  // アノテーションの名前付き引数やデフォルト引数については、14章のP.253を参照

  // 引数として他のアノテーションをとるようなアノテーションの場合
  import annotation._
  class strategy(arg: Annotation) extends Annotation
  class delayed extends Annotation
  //@strategy(@delayed) def f(){}
  // Error: illegal start of simple expression
  @strategy(new delayed) def f(){}  // OK

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 標準アノテーション (1)使うべきでない機能の指定
object Sp27_03_01 {
  // 廃止予定 = @deprecated
  // このマークを付けておくと、そのメソッドやクラスを呼び出しているコードは「使うべきでない機能を使っている」
  // という警告を受けるようになる。
  // scalaインタプリタや、scalacでコンパイルする場合は、オプションに「-deprecation」が必要

  // メッセージ付き
  @deprecated("use new ShinyMethod() instead")
  def bigMistake1() = null
  def bigMistake2() = null

  bigMistake1
  // : method bigMistake1 in object Sp27_03_01 is deprecated: use new ShinyMethod() instead
  bigMistake2

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 標準アノテーション(2)その他
object Sp27_03_02_07 {
  // 27.3.2「揮発性」フィールド
  // @volatite
  // 適用されている変数が複数のスレッドから使われているものだということをコンパイラーに知らせる。詳細は、第32章


  // 27.3.3 バイナリーシリアライゼーション
  // @serializable
  // シリアライズしたいクラスにつける。

  // @SerialVersionUID(1234)
  // シリアライズできるクラスの書き換えに対応する。シリアル番号が与えることができる。

  // @transient
  // シリアライズすべてでないフィールドにつける。

  // 27.3.4 get/setメソッドの自動生成
  // @scala.reflect.BeanProperty
  // フィールドにこのアノテーションを追加すると、コンパイラーが自動的にget/setメソッドを生成していくれる。
  // crazyという名前のフィールドにアノテーションを付けると、getメソッドにはgetCrazy、setメソッドにはsetCrazyと
  // いう名前が付けられる。

  // 27.3.5 末尾再帰
  // @tailrec
  // 末尾再帰にする必要のあるメソッド
  // 末尾再帰によるメソッドの最適化をScalaコンパイラーに指示したい場合、メソッド定義の前に追加する。
  // 最適化できない場合には、できない理由とともに警告が表示される。

  import annotation.tailrec
  @tailrec def withdraw(amount: Int): Boolean = {
    print(amount + ",")
    if (amount == 0)
      false
    else
      withdraw(amount - 1)
  }
  withdraw(10)
  println()

  /*
  @tailrec def withdraw2(amount: Int): String = {
    print(amount + ",")
    if (amount == 0)
      false
    else
      withdraw2(amount - 1)
    (amount + 1).toString
  }
  withdraw2(10)
  println()
  */
  // Error: could not optimize @tailrec annotated method: it contains a recursive call not in tail position

  // 27.3.6 パターンのチェック回避
  // @unchecked
  // コンパイラーがパターンマッチ中に解釈する。
  // マッチ式に抜けているケースがあるように見えても、エラーを出さないようにコンパイラーに指示する。
  // 注意：マッチ式のルーチン前にチェックされていれば、このアノテーションを使うメリットはあるが、
  // チェックがない場合に、このアノテーションを使うと危険である。

  // 27.3.7 ネイティブメソッド
  // @native
  // メソッドの実装がScalaコードではなく、ランタイムによって提供されることをコンパイラーに知らせる。
  // コンパイラーは適切なフラグを付けて出力を行なうことで、開発者がJNI(Java Native Interface)などを
  // 使ってメソッドの実装を提供できるという機会を用意する。

  @native
  def biginCountdown() {}

}