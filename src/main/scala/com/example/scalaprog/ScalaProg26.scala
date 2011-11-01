package com.example.scalaprog

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/10/27
 * Time: 16:59
 * To change this template use File | Settings | File Templates.
 */

// 第26章 抽出子
object ScalaProg26

// サンプル：メールアドレスの抽出
object Sp26_01 {
  /*
  def isEMail(s: String): Boolean
  def domain(s: String): String
  def user(s: String): String

  if (isEMail(s)) println(user(s) + " AT " + domain(s))
  else println("not an email address")

  s match {
    case EMail(user, domain) => println(user + " AT " + domain)
    case _ => println("not an email address")
  }

  s match {
    case EMail(u1, d1) :: Email(us2, d2) :: _ if(u1 == u2) => ...
    ...
  }
   */

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 抽出子
object Sp26_02 {
  object EMail extends ((String, String) => String) {
    // 注入(injection)メソッド（オプション）
    def apply(user: String, domain: String) = user + "@" + domain
    // 抽出(extraction)メソッド（必須）
    def unapply(str: String): Option[(String, String)] = {
      val parts = str split  "@"
      if (parts.length == 2) Some(parts(0), parts(1)) else None
    }
  }

  // パターンマッチが抽出子オブジェクトを参照するパターンを検出したときには、
  // セレクター式を対象としてunapplyメソッドが呼び出される。
  def checkMail(s: Any) =
    s match {
       case EMail(user, domain) => println("split address: " + user + " AT " + domain)
       case EMail(u1, d1) :: EMail(u2, d2) :: _ if(u1 == u2) => println("same user: " + u1 + " AT " + d1)
       case _ => println("条件NG")
    }
  checkMail("hoge@example.com")
  checkMail("hoge!example.com")
  val x: Int = 123
  checkMail(x)
  checkMail(List("hoge1@example.com", "hoge1@example.com"))
  checkMail(List("hoge2@example.com", "hoge3@example.com"))

  //println(EMail("hoge", "example.com"))
  println(EMail.unapply(EMail.apply("hoge", "example.com")))
  // Some((hoge,example.com))
  // EMail(user, domain) こうなる必要があるとのことです

  /*
  def reverseApply(obj: String) = {
    EMail.unapply(obj) match {  // EMail.unapply(obj) ... Some("hoge", "example.com")
      case Some(u, d) => EMail.apply(u, d)  ... u = "hoge", d = "exmaple.com" ... "hoge@example.com"
    }
  }
  val obj = EMail.apply("hoge", "example.com")
  println(reverseApply(obj))
  */

  // 注入メソッドが含まれている場合、それは抽出メソッドと相補的になるようにしなければならない。

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 変数が1個以下のパターン
object Sp26_03 {

  // 一文内に重複する文字列を扱うオブジェクト
  object Twice {
    //def apply(s: String): String = s + s  // 今回の処理では必須ではない。
    def unapply(s: String): Option[String] = {
      val length = s.length / 2
      val half = s.substring(0, length)
      if (half == s.substring(length)) Some(half) else None
    }
  }

  // 大文字の判別オブジェクト
  object UpperCase {
    def unapply(s: String): Boolean = s.toUpperCase == s
  }

  // Sp26_02のEMailオブジェクトのインポート
  import com.example.scalaprog.Sp26_02.EMail

  def userTwiceUpper(s: String) =
    s match {
      case EMail(Twice(x @ UpperCase()), domain) =>
        "match: " + x + " in domain " + domain
      case _ =>
        "no match"
    }
  // 「EMail(Twice(x @ UpperCase()), domain)」の判定順序
  // 1.EMailオブジェクトのunapplyにより、HOGEHOGEとexample.comに分離
  // 2.Twiceオブジェクトのunapplyにより、HOGEを抽出
  // 3.UpperCaseオブジェクトのunapplyによりTrueと判定され、マッチしたパターンに変数xを束縛する。「x @ UpperCase()」
  // P.272 - 15.2.8 変数の束縛を参照

  println(userTwiceUpper("HOGEHOGE@example.com"))
  println(userTwiceUpper("HOGEPIYO@example.com"))
  println(userTwiceUpper("hogehoge@example.com"))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 可変個の引数をとる抽出子(1)
object Sp26_04_a {

  object Domain {
    // 注入メソッド（オプション）引数は可変長
    def apply(parts: String*): String =
      parts.reverse.mkString(".")

    // 抽出メソッド（必須）　可変個マッチ用のunapplySeqで定義します。
    def unapplySeq(whole: String): Option[Seq[String]] =
      // 文字列をピリオドで分割したものを逆順で返します
      // 区切り文字の"."はエスケープ
      Some(whole.split("\\.").reverse)
  }
  Domain.unapplySeq("java.sun.com")
  // res0: Option[Seq[String]] = Some(WrappedArray(com, sun, java))

  def seekDomain(s: String) =
    s match {
      // 引数が2個のパターンに対応しますよ
      case Domain("org", "acm") => println("acm.org")
      // 引数が3個のパターンにも対応しますよ
      case Domain("com", "sun", "java") => println("java.sun.com")
      // 可変長の引数パターンにも対応しますよ
      case Domain("net", _*) => println("a .net domain")
      case _ => println("none")
    }
  seekDomain("java.sun.com")
  // java.sun.com

  // Sp26_02のEMailオブジェクトのインポート
  import com.example.scalaprog.Sp26_02.EMail

  def isTomInDotCom(s: String): Boolean =
    s match {
      case EMail("tom", Domain("com", _*)) => true
      case _ => false
    }
  println(isTomInDotCom("tom@example.com")) // true
  println(isTomInDotCom("tom@test1.example.com")) // true
  println(isTomInDotCom("sam@example.com")) // false
  println(isTomInDotCom("tom@example.co.jp")) // false

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}
// 可変個の引数をとる抽出子(2)
object Sp26_04_b {

  object ExpandedEMail {
    def unapplySeq(email: String): Option[(String, Seq[String])] = {
      val parts = email split "@"
      if (parts.length == 2)
        Some(parts(0), parts(1).split("\\.").reverse)
      else
        None
    }
  }

  // オブジェクトに値を代入し、オブジェクトの引数に結果を代入する。
  val s1 = "tom@example.com"
  val ExpandedEMail(name1, domain @ _*) = s1
  // name: String = tom
  // domain: Seq[String] = WrappedArray(com, example)

  val s2 = "tom@test1.example.com"
  val ExpandedEMail(name2, topdom, sudoms @ _*) = s2
  // name: String = tom
  // topdom: String = com
  // sudoms: Seq[String] = WrappedArray(example, test1)


  // 応用版
  def isJackInHogeDotNetExpanded(s:String) =
    s match {
      case ExpandedEMail("jack", "net", "hoge",  _*) => println("OK")
      case _ => println("NG")
    }
  isJackInHogeDotNetExpanded("jack@hoge.net")
  isJackInHogeDotNetExpanded("jack2@hoge.net")

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 抽出子とシーケンスパターン
object Sp26_05 {
  /*
  package scala
  object List {
    def apply[T](elems: T*) = elems.toList
    def unapplySeq[T](x: List[T]): Option[Seq[T]] = Some(x)
  }
  */

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 抽出子とケースクラス
object Sp26_06  {
  /*
  ケースクラスとパターンマッチについては、P.260を参照

  ケースクラスは、色々な補強が自動で行われる上パターンマッチにも利用出来るという優れものだったのですが、
  パターンマッチに使うことでセレクターオブジェクトの具象表現型にクラスが対応している（どういう型になっているかを）
  晒してしまうという欠点がある。
  つまり、クライアントコードのパターンマッチでケースクラスを使いまくると、ケースクラスの変更が思い切り影響を
  及ぼしてしまうという

  同じように利用出来る抽出子は対象オブジェクトのデータ型とは無関係なパターンを書けるという表現独立性を持つので、
  クライアントの利用状況にかかわらずコンポーネントやライブラリの実装を変更するのに適している

  ①抽出子がケースクラスより優れている点…表現独立性
  　大規模開発の場合、ケースクラスの名前を変えたり、クラス階層に手を入れると、クライアントコードに影響が及ぶ。
  　データ表現とクライアントからの見え方（ビュー）との間にクッション層を入れる抽出子なら、
  　抽出子の更新を忘れなければ型の具体的な表現は変えられる。
  ②ケースクラスが方が抽出子よりも優れている点
  　1.ケースクラスはセットアップや定義が簡単であり、必要なコードが少ない。
  　2.抽出子によるパターンよりもケースクラスによるパターンの方が、パターンマッチの効率がよい。
    3.ケースクラスがシールドクラスを継承している場合、Scalaコンパイラーはパターンクラスを徹底的にチェックするが、
    　抽出子ではそこまで徹底したチェックはできない。

  パターンマッチではどちらの方法を選ぶべきか
    閉じたアプリケーションのためのコードを書く場合は、簡潔・高速で静的型チェックが行われるケースクラスの方が一般的に望ましい。
    未知のクライアントに対して型を公開しなければならない場合には、表現独立性を保てる抽出子の方がよい。
   */

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 正規表現 - 正規表現の形成
object Sp26_07_01 {
  // Sp23_99参考

  // Regexは下記クラスを必要とするが、「.r」は不要 ... StringOpsクラス内でRegexを呼んでいるので
  /* StringOpsクラスで定義されているrメソッド
  package scala.runtime
  import scal.util.matching.Regex
  class StringOps(self: String) ... {
    ...
    def r = new Regex(self)
  }
   */
  import scala.util.matching.Regex

  val Decimal1 = new Regex("(-)?(\\d+)(\\.\\d*)?")
  // Decimal1: scala.util.matching.Regex = (-)?(\d+)(\.\d*)?

  val Decimal2 = """(-)?(\d+)(\.\d*)?""".r
  // Decimal2: scala.util.matching.Regex = (-)?(\d+)(\.\d*)?

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 正規表現 - 正規表現による探索
object Sp26_07_02 {
  val Decimal1 = """(-)?(\d+)(\.\d*)?""".r
  val Decimal2 = """[a-zA-Z]\w*""".r
  val input = "for -1.0 to 99 by 3"

  // input文字列に含まれるすべての正規表現regexを探し、結果値をIteratorにして返す。
  for (s <- Decimal1 findAllIn input)
    print(s + ",")
  // -1.0,99,3,
  println()

  // input文字列に対して正規表現regexが最初に現れる場所を探し、結果値をOption値にして返す。
  println(Decimal1 findFirstIn input)
  // Some(-1.0)

  // input文字列の先頭から正規表現regexが含まれている部分を探し、結果値をOption値にして返す。
  println(Decimal1 findPrefixOf input)
  // None

  println(Decimal2 findPrefixOf input)
  // Some(for)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 正規表現 - 正規表現による抽出
object Sp26_07_03 {
  val Decimal = """(-)?(\d+)(\.\d*)?""".r
  val input = "for -1.0 to 99 by 3"

  val Decimal(sign1, integerpart1, decimalpart1) = "-1.23"
  // sign1: String = -
  // integerpart1: String = 1
  // decimalpart1: String = .23

  val Decimal(sign2, integerpart2, decimalpart2) = "1.0"
  // sign2: String = null
  // integerpart2: String = 1
  // decimalpart2: String = .0

  // for式を使って、抽出子と正規表現による検索を結合する。
  for (Decimal(s, i, d) <- Decimal findAllIn input)
    println("sign: " + s + ", integer: " + i + ", decimal: " + d)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}
