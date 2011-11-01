package com.example.scalaprog

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/10/28
 * Time: 14:36
 * To change this template use File | Settings | File Templates.
 */

// 第28章 XMLの操作
object ScalaProg28

// 準構造化データ
object Sp28_01 {
  // ScalaはXMLを処理するための特別なサポートを組み込んでいる。
  // ScalaでのXML構築、普通のメソッドによるXML操作、ScalaのパターンマッチによるXML操作

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// XMLの概要
object Sp28_02 {
  // XMLは、テキストとタグの2つの基本要素から構成されている。
  // XMLは入れ子構造の要素(element)を持つ。
  // 開始タグの直後に終了タグが続く場合、略記法（<peas/>」が使える。空要素(empty elements）である。
  // 開始タグは属性(attribute)を持つことができる。... <pod peas="3" string="true"/>

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// XMLリテラル
object Sp28_03 {
  <a>
    This is some XML.
    Here is a tag: <atag/>
  </a>
  /*
  res1: scala.xml.Elem =
<a>
    This is some XML.
    Here is a tag: <atag></atag>
  </a>
   */

  // エスケープとして「{}」を使えば、XMLリテラルの中でScalaコードを評価させることができる。
  <a> {"hello" + ", world"} </a>
  // res2: scala.xml.Elem = <a> hello, world </a>

  val yearMade = 1955
  // 下記インタプリターではNG
  <a> { if (yearMade < 2000) <old>{yearMade}</old>
          else xml.NodeSeq.Empty }
  </a>

  // 下記インタプリターではOK
  val tag1 = <a> { if (yearMade < 2000) <old>{yearMade}</old> else xml.NodeSeq.Empty } </a>
  // res4: scala.xml.Elem = <a> <old>1955</old> </a>

  // 「無」のXMLノード（＝何も追加しない）とは、xml.NodeSeq.Emptyのこと。

  // 中括弧の中の式は、XMLノードと評価されるものでなくてもよい。任意のScala値にひょうかされてよい。
  val tag2 = <a> {3 + 4} </a>
  // res6: scala.xml.Elem = <a> 7 </a>

  // テキストの中の<、>、&といった文字は、ノードを出力するときにはエスケープされる。
  val tag3 = <a> {"</a>potential security hole<a>"} </a>
  // res7: scala.xml.Elem = <a> &lt;/a&gt;potential security hole&lt;a&gt; </a>

  // 低水準文字操作でXMLを作ると、意図しない結果になる。
  // ユーザーから提供される文字列自体にXMLタグが含まれていた(</a>や<a>)と認識されてるからである。
  val tag4 = "<a>" + "</a>potential security hole<a>" + "</a>"
  // res8: java.lang.String = <a></a>potential security hole<a></a>

  println(tag1 + "\n" + tag2 + "\n" + tag3 + "\n" + tag4)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// シリアライゼーション
object Sp28_04 {

  import com.example.scalapkg.CCTherm

  val therm = new CCTherm {
    val description = "hot dog #5"
    val yearMade = 1952
    val dateObtained = "March 14, 2006"
    val bookPrice = 2199
    val purchasePrice = 500
    val condition = 9
  }

  // 注意！ CCThermは抽象クラスだが、上のサンプルのnew CCThermという式は動作する。
  // この式は、CCThermの無名サブクラスのインスタンスを作るのである。
  // 無名クラスについては、20.5節を参照(P.381-382)

  println(therm)
  println(therm.toString)
  println(therm.toXML)

  // 中括弧「{}」をScalaコードのエスケープではなく、XMLテキストとして使いたい場合は、単純に2個の中括弧を続けて書く
  val tag = <a> {{{{brace yourself!}}}} </a>
  println(tag)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// XMLの分解
object Sp28_05 {
  // XMLを分解する為のこれらのメソッドは、XML処理のために開発されているXPath言語を基礎としている。

  // テキストの抽出
  <a>Sounds <tag/> good</a>.text
  // res13: String = Sounds  good
  <a> input ---&gt; output </a>.text
  // res14: String =  input ---> output

  // サブ要素の抽出
  <a><b><c>hello</c></b></a> \ "b"
  // res15: scala.xml.NodeSeq = NodeSeq(<b><c>hello</c></b>)

  <a><b><c>hello</c></b></a> \ "b" \ "c"
  // res20: scala.xml.NodeSeq = NodeSeq(<c>hello</c>)

  <a><b><c>hello</c></b></a> \ "c"
  // res19: scala.xml.NodeSeq = NodeSeq()

  <a><b><c>hello</c></b></a> \\ "c"
  // res21: scala.xml.NodeSeq = NodeSeq(<c>hello</c>)

  <a><b><c>hello</c></b></a> \ "a"
  // res23: scala.xml.NodeSeq = NodeSeq()

  <a><b><c>hello</c></b></a> \\ "a"
  // res24: scala.xml.NodeSeq = NodeSeq(<a><b><c>hello</c></b></a>)

  // 属性の抽出
  //  同じ\や\\メソッドでタグ属性も抽出できる．属性名の前に@をつければよい。
  val joe = <employee
    name = "Joe"
    rank  = "code monkey"
    serial = "123" />
  // joe: scala.xml.Elem = <employee rank="code monkey" name="Joe" serial="123"></employee>

  joe \ "@name"
  // res27: scala.xml.NodeSeq = Joe
  joe \ "@rank"
  // res28: scala.xml.NodeSeq = code monkey
  joe \ "@serial"
  // res29: scala.xml.NodeSeq = 123

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// デシリアライゼーション
object Sp28_06 {

  import com.example.scalapkg.CCTherm

  /*
  val therm = new CCTherm {
    val description = "hot dog #5"
    val yearMade = 1952
    val dateObtained = "March 14, 2006"
    val bookPrice = 2199
    val purchasePrice = 500
    val condition = 9
  }
  */
  val node = com.example.scalaprog.Sp28_04.therm.toXML// therm.toXML
  println("node: " + node)
  // ここまでは、Sp28_04のシリアライゼーション

  def fromXML(node: scala.xml.Node): CCTherm =
    new CCTherm {
      val description   = (node \ "description").text
      val yearMade      = (node \ "yearMade").text.toInt
      val dateObtained  = (node \ "dateObtained").text
      val bookPrice     = (node \ "bookPrice").text.toInt
      val purchasePrice = (node \ "purchasePrice").text.toInt
      val condition     = (node \ "condition").text.toInt
    }

  val therm2 = fromXML(node)
  println("therm2.yearMade: " + therm2.yearMade)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// ロードと保存
object Sp28_07 {

  val node = com.example.scalaprog.Sp28_04.therm.toXML

  // XMLの保存 ... 保存パスはデフォルトの「C:\scala\project\ScalaProgramming」
  // saveの引数 = (filename: String, node: Node, enc: String, xmlDecl: Boolean, doctype: dtd.DocType)
  // xmlDeclがtrueだと、「<?xml version='1.0' encoding='UTF-8'?>」が出力される。
  scala.xml.XML.save("..\\bak\\therm1.xml", node, "UTF-8", true, null)

  // C:\scala\project\ScalaProgramming\therm1.xmlファイルの中身
  /*
  <?xml version='1.0' encoding='UTF-8'?>
  <cctherm>
      <description>hot dog #5</description>
      <yearMade>1952</yearMade>
      <dateObtained>March 14, 2006</dateObtained>
      <bookPrice>2199</bookPrice>
      <purchasePrice>500</purchasePrice>
      <condition>9</condition>
    </cctherm>
   */

  val loadnode = xml.XML.loadFile("..\\bak\\therm1.xml")
  val therm3 = com.example.scalaprog.Sp28_06.fromXML(loadnode)
  println("therm3.yearMade: " + therm3.yearMade)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// XMLを対象とするパターンマッチ (1)
object Sp28_08a {

  def proc1(node: scala.xml.Node): String =
    node match {
      case <a>{contents}</a> => "It's an a: " + contents
      case <b>{contents}</b> => "It's a b: " + contents
      case _ => "It's something else."
    }

  val res1 = proc1(<a>apple</a>)
  println(res1) // It's an a: apple
  val res2 = proc1(<b>banana</b>)
  println(res2) // It's a b: banana
  val res3 = proc1(<c>cherry</c>)
  println(res3) // It's something else.
  val res4 = proc1(<a>a <em>red</em> apple</a>)
  println(res4) // It's something else.
  val res5 = proc1(<a/>)
  println(res5) // It's something else.

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// XMLを対象とするパターンマッチ (2)
object Sp28_08b {

  def proc2(node: scala.xml.Node): String =
    node match {
      case <a>{contents @ _*}</a> => "It's an a: " + contents
      case <b>{contents @ _*}</b> => "It's a b: " + contents
      case _ => "It's something else."
    }

  val res6 = proc2(<a>apple</a>)
  println(res6) // It's an a: ArrayBuffer(apple)
  val res7 = proc2(<b>banana</b>)
  println(res7) // It's a b: ArrayBuffer(banana)
  val res8 = proc2(<c>cherry</c>)
  println(res8) // It's something else.
  val res9 = proc2(<a>a <em>red</em> apple</a>)
  println(res9) // It's an a: ArrayBuffer(a , <em>red</em>,  apple)
  val res10 = proc2(<a/>)
  println(res10) // It's an a: WrappedArray()

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// XMLを対象とするパターンマッチ (3)
object Sp28_08c {
  val catalog =
      <catalog>
        <cctherm>
          <description>hot dog #5</description>
          <yearMade>1952</yearMade>
          <dateObtained>March 14, 2006</dateObtained>
          <bookPrice>2199</bookPrice>
          <purchasePrice>500</purchasePrice>
          <condition>9</condition>
        </cctherm>
        <cctherm>
          <description>Sprite Boy</description>
          <yearMade>1964</yearMade>
          <dateObtained>April 28, 2003</dateObtained>
          <bookPrice>1695</bookPrice>
          <purchasePrice>595</purchasePrice>
          <condition>5</condition>
        </cctherm>
      </catalog>

  catalog match {
    case <catalog>{therms @ _*}</catalog> =>
      //for (therm <- therms) // <catalog></catalog>タグ内の要素間には、
      // 2個の<cctherm>ノードがあるが、3箇所の空白があるので、合計5個のノードが出力される。
      for (therm @ <cctherm>{_ *}</cctherm> <- therms)
        println("processing: " + (therm \ "description").text)
  }

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}