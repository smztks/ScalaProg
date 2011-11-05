package com.example.scalaprog

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/11/02
 * Time: 16:45
 * To change this template use File | Settings | File Templates.
 */

// 第33章 パーサーコンビネーター
object ScalaProg33

// サンプル：算術式・パーサーの実行
object Sp33_01 {

  import com.example.scalapkg._

  object ParserExpr extends Arith {
    def main(arg: String) {
      println("input: " + arg)
      // この式は、与えられたarg（入力値）に対してパーサーのexpr（Arithクラス内のメソッド）を適用する。
      println(parseAll(expr, arg))
    }
  }

  ParserExpr.main("2 * (3 + 7)")
  // input: 2 * (3 + 7)
  // [1.12] parsed: ((2~List((*~(((~((3~List())~List((+~(7~List())))))~)))))~List())

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 基本正規表現パーサー
// cf. http://d.hatena.ne.jp/seratch2/20111010/1318254084
object Sp33_03 {

  import util.parsing.combinator._

  object MyParsers extends RegexParsers {
    def firstName: Parser[String] = "[a-zA-Z]+".r
    def lastName: Parser[String] = "[a-zA-Z]+".r
    def fullName = firstName ~ lastName   // ~ = 逐次合成 P.657
    def parse(input: String) = parseAll(fullName, input)
  }

  val res = MyParsers.parse("Martin Odersky")
  println(res)
  // [1.15] parsed: (Martin~Odersky)
  println(res.get._1)
  // Martin
  println(res.get._2)
  // Odersky

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 他のパーサーの使用例：JSON
object Sp33_04 {

  // 手動の場合は、下記内容で「address-book.json」ファイルをScalaProg直下に作成する。
  val addressBook =
  """
  {
    "address book": {
      "name": "John Smith",
      "address": {
        "street": "10 Market Street",
        "city"  : "San Francisco, CA",
        "zip"   : 94111
      },
      "phone numbers": [
        "408 338-4238",
        "408 111-6892"
      ]
    }
  }
  """
  // ファイル作成
  import java.io.{PrintWriter, File}

  def withPrintWriter(file: File, op: PrintWriter => Unit) {
    val writer = new PrintWriter(file)
    try {
      op(writer)
    } finally {
      writer.close()
    }
  }
  withPrintWriter(
    new File("address-book.json"),
    _.print(addressBook)
  )

  import com.example.scalapkg._
  import java.io.FileReader

  object ParseJSON extends JSON1 {
    def main(arg: String) {
      val reader = new FileReader(arg)
      // readerから返された文字列をJSON文法の生成規則であるvalue（JSONクラス内のメソッド）に渡して構文解析を行なう。
      println(parseAll(value, reader))
    }
  }

  ParseJSON.main("address-book.json")
  // [15.1] parsed: (({~List((("address book"~:)~(({~List((("name"~:)~"John Smith"), (("address"~:)~(({~List((("street"~:)~"10 Market Street"), (("city"~:)~"San Francisco, CA"), (("zip"~:)~94111)))~})), (("phone numbers"~:)~(([~List("408 338-4238", "408 111-6892"))~]))))~}))))~})

  /* 削除失敗なぜ？
  val file = new File("address-book.json")
  if (file.exists)
    file.delete
   */

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// パーサーの出力
object Sp33_05 {

  import java.io.File
  import com.example.scalaprog.Sp33_04.{withPrintWriter, addressBook}

  withPrintWriter(
    new File("address-book.json"),
    _.print(addressBook)
  )

  import com.example.scalapkg._
  import java.io.FileReader

  object ParseJSON extends JSON2 {
    def main(arg: String) {
      val reader = new FileReader(arg)
      // readerから返された文字列をJSON文法の生成規則であるvalue（JSONクラス内のメソッド）に渡して構文解析を行なう。
      println(parseAll(value, reader))
    }
  }

  ParseJSON.main("address-book.json")
  // [16.3] parsed: Map("address book" -> Map("name" -> "John Smith", "address" -> Map("street" -> "10 Market Street", "city" -> "San Francisco, CA", "zip" -> 94111.0), "phone numbers" -> List("408 338-4238", "408 111-6892")))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// thisの別名（別名記法）
// 別名記法として、29.4節の「トレイトに自分型を与える場合」も参照
object Sp33_06_04 {

  class Outer { outer =>
    class Innter {
      println(Outer.this + " | " + outer)
      // com.example.scalaprog.Sp33_06_04$Outer@29e357 | com.example.scalaprog.Sp33_06_04$Outer@29e357
      println(Outer.this eq outer)
      // true
    }
  }
  val o = new Outer
  val i = new o.Innter
  i // true


  println("end of: " + Thread.currentThread.getStackTrace()(1))
}