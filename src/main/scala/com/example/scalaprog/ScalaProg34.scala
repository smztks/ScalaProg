package com.example.scalaprog

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/11/03
 * Time: 14:50
 * To change this template use File | Settings | File Templates.
 */

// 第34章 GUIプログラミング
object ScalaProg34

// 最初に作るSwingアプリケーション
object Sp34_01 {

  // 下記、インタプリターで実施する場合の方法。IntelliJ IDEAの場合はFirstSwingApp.scalaを使用
  // 以降は、FirstSwingApp.scalaを参照
  /*
  import scala.swing._

  // SimpleGUIApplicationよりも、SimpleSwingApplicationを推奨
  object FirstSwingApp extends SimpleSwingApplication {
    def top = new MainFrame {
      title = "First Swing App"
      contents = new Button {
        text = "Click me"
      }
    }
  }
   */

  // scalac FirstSwingApp.scala
  // scala FirstSwingApp

  println("end of: " + Thread.currentThread.getStackTrace()(1))
  println("abcd")
}