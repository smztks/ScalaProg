package com.example.scalapkg

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/11/03
 * Time: 12:00
 * To change this template use File | Settings | File Templates.
 */

import scala.util.parsing.combinator._

// リスト33.1 算術式のパーサー
class Arith extends JavaTokenParsers {
  def expr: Parser[Any] = term~rep("+"~term | "-"~term)
  def term: Parser[Any] = factor~rep("*"~factor | "/"~factor)
  def factor: Parser[Any] = floatingPointNumber | "("~expr~")"
}

// リスト33.4 簡単なJSONパーサー
class JSON1 extends  JavaTokenParsers {
  def obj   : Parser[Any] = "{"~repsep(member, ",")~"}"
  def arr   : Parser[Any] = "["~repsep(value, ",")~"]"
  def member: Parser[Any] = stringLiteral~":"~value
  def value : Parser[Any] = obj | arr | stringLiteral | floatingPointNumber | "null" | "true" | "false"
}

// リスト33.5 意味のある結果値を返す完全なJSONパーサー
class JSON2 extends  JavaTokenParsers {
  def obj   : Parser[Map[String, Any]] = "{"~> repsep(member, ",") <~"}" ^^ (Map() ++ _)
  def arr   : Parser[List[Any]] = "["~> repsep(value, ",") <~"]"
  def member: Parser[(String, Any)] = stringLiteral~":"~value ^^ { case name~":"~value => (name, value) }
  def value : Parser[Any] = (
    obj |
    arr |
    stringLiteral |
    floatingPointNumber ^^ (_.toDouble) |
    "null"  ^^ (x => null) |
    "true"  ^^ (x => true) |
    "false" ^^ (x => false)
  )
}

/*
  表33.1 パーサー・コンビネーター一覧
  
  "..."           リテラル
  "...".r         正規表現
  P^Q             逐次合成
  P <^ Q, P ^> Q  逐次合成、左／右の結果のみ保持
  P | Q           選択
  opt(P)          オプション
  rep(P)          繰り返し
  repsep(P, Q)    区切り子つきの繰り返し
  P ^^ f          解析結果の変換

*/