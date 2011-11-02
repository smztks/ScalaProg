package com.example.scalaprog

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/10/14
 * Time: 17:29
 * To change this template use File | Settings | File Templates.
 */

// 第18章 ステートフルオブジェクト
object ScalaProg18

// どのようなオブジェクトがステートフルなのか
object Sp18_01 {
  val cs = List('a', 'b', 'c')
  println(cs.head)
  println(cs.head)

  class BankAccount(private var bal :Int = 100) {
    //private var bal: Int = 0
    def balance: Int = bal
    def deposit(amount: Int) {
      require(amount > 0)
      bal += amount
    }
    def withdraw(amount: Int): Boolean =
      if (amount > bal) false
      else {
        bal -= amount
        true
      }
  }
  //val account = new BankAccount
  //account deposit 100
  val account = new BankAccount(100)
  //val account = new BankAccount()
  //SBT,Makeだと、デフォルト引数を使ったオブジェクト作成ができない。インタプリタでは問題なく実行できる ... 課題
  println(account.balance)
  println(account withdraw 80)
  println(account.balance)
  println(account withdraw 80)
  println(account.balance)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 再代入可能な変数とプロパティ
object Sp18_02 {
  class Time1{
    var hour = 12
    var minute = 0
  }
  val time1 = new Time1
  time1.hour_=(15)
  time1.minute_=(59)
  println(time1.hour + "|" + time1.minute)

  class Time2 {
    private[this] var h = 12
    private[this] var m = 0
    def hour: Int = h
    def hour_=(x: Int) {
      require((0 <= x && x < 24))
      h = x
    }
    def minute: Int = m
    def minute_=(x: Int) {
      require((0 <= x && x < 60))
      m = x
    }
  }
  val time2 = new Time2
  time2.hour_=(15)
  time2.minute_=(59)
  println(time2.hour + "|" + time2.minute)

  class Thermometer{
    // 摂氏の温度を格納します
    // 初期値"_"が指定されているので型ごとの初期値(Floatなら0)が入力されます
    var celsius: Float = _

    //// 華氏に関するセッター・ゲッターです
    // セッターでは摂氏を華氏に変換して値を返します
    def fahrenheit = celsius * 9 / 5 + 32
    // ゲッターでは華氏の値を摂氏に変換してcelsiusに格納します
    def fahrenheit_= (f:Float) {
      celsius = (f - 32) * 5/ 9
      println("set to celsius")
    }
    // 格納された温度を摂氏と華氏の両方で表示します
    override def toString = fahrenheit + "F/" + celsius + "C"
  }

  val thermo = new Thermometer
  println(thermo)

  println(thermo.celsius)
  thermo.celsius = 100
  println(thermo)     // ThermometerのtoStringにて、fahrenheit関数がコールされる。

  println(thermo.fahrenheit)
  thermo.fahrenheit = -40 // fahrenheit関数のコールにより、celsiusの値も変わる。
  println(thermo)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// ケーススタディ:離散イベントシミュレーション
// デジタル回路のための言語
// シミュレーションAPI
// デジタル回路のシミュレーション
object Sp18_03 {
  /*
  第1層：シミュレーションフレームワーク
  第2層：基本論理回路シミュレーションパッケージ
  第3層：ユーザー定義回路のライブラリー
  第4層：シミューレーション上の回路自体
   */

  import com.example.scalapkg._

  // イベントの発生タイミングを指定
  object MySimulation extends CircuitSimulation {
    def InverterDelay = 1
    def AndGateDelay = 3
    def OrGateDelay = 5
  }

  // 第4層：シミューレーション上の回路自体
  import MySimulation._
  // 配線の要素を作成
  println("WireProbe")
  val input1, input2, sum, carry = new Wire
  probe("[P]input1", input1) // WireクラスのactionsリストにaddAction（アクションを追加）
  probe("[P]input2", input2) // addActionの際にActionを一度実行して、afterDelayによりagenda（予定表）に格納
  probe("[P]sum", sum)        // Wireの信号(sigVal)が変化した場合に再コールされて、printlnされる。
  probe("[P]carry", carry)   // 再コールされる理由：setSignalが行われると、Wireに紐づいたactionsが実行されるので
                                // 半加算器の要素の生成＝Wireの生成と、addAction関数コール＝Actionの追加
  println("\nhalfAdder")
  halfAdder(input1, input2, sum, carry)


  println("\nSIMU-#01")
  // 配線の信号が変わるたびにも実行される（配線の信号が変わった際は配線が保持している全てのアクションが実行される）
  // 理由：WireのActionを実行することで、「output setSignal !inputSig」input値の再定義とafterDelayによる
  // agendaへの再投入が必要だから。
  input1 setSignal true // WireクラスのsetSignalを実行し、actionsリスト内のActionをすべて実行
  run()

  println("\nSIMU-#02")
  input2 setSignal true
  run()

  /*

  SIMU01
  input1 time: 0 | new-value = true
  *** simulation started, time = 0 ***
  sum 8 new-value = true

  sumが8秒かかった理由：
  input2が1であり、半加算器の2個目のAndゲートでinput1の値待ちが必要
  （論理積の場合、1の値が先にきた時点で二個目の値の判断が必要）な為、
  input1経路の最大値である、Or:5 + And:3のコストがかかった

  SIMU02
  input2 time: 8 | new-value = true
  *** simulation started, time = 8 ***
  carry 11 new-value = true
  carryが11-8=3秒かかった理由：半加算器の1個目のAnd:3のコストがかかった

  sum time: 15 | new-value = false
  sumが15-8=7秒かかった理由：
  input2が1（インバータで0に変換）であり、半加算器の2個目のinput1の値待ちが不要
  論理積の場合、0の値が先にきた時点で偽となる）な為、
  input2経路の最大値である、And:3 + Inv:1 + And:3のコストがかかった

   */

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}
