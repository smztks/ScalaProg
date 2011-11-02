package com.example.scalapkg

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/10/16
 * Time: 18:10
 * To change this template use File | Settings | File Templates.
 */

// 第18章 ステートフルオブジェクトのシミュレーションクラス

// 第1層：シミュレーションフレームワーク
abstract class Simuration {
  // 空パラメータをとり、Unitを返す手続き型（副作用メソッド）にActionという別名を定義している
  // Actionは型メンバーと呼ばれる、型メンバーについて20章
  type Action = () => Unit

  // ケースクラス:newキーワードを必要とせず、オブジェクトを作れる ... P.261
  // 指定されたタイミングで実行する作業項目(work item)
  // WorkItemクラスはコンストラクターパラメータのtimeとactionに対するアクセサが手に入る
  // WorkItemクラスはSimurationクラス内の入れ子になっている。詳細は、20.7節で説明
  case class WorkItem(time: Int, action: Action)

  // シミュレーション上の時間を計測する為の変数
  private var curtime = 0

  // currentTimeメソッドで現在時を返すアクセサ(accessor=アクセス機構)
  def currentTime: Int = curtime

  // agenda=予定表、作業項目はシミュレーション上の時間によってソートされる
  private var agenda: List[WorkItem] = List() // WorkItemのリスト型、初期値として空リストを代入

  // 予定表(agenda)に作業項目(WorkItem)を追加する唯一のメソッド
  private def insert(ag: List[WorkItem], item: WorkItem): List[WorkItem] = {
    if (ag.isEmpty || item.time < ag.head.time) item :: ag    // 空白リストもしくは、先頭リストの時刻より小さい場合
    else ag.head :: insert (ag.tail, item)  // 先頭（再帰した場合は、tailの先頭）リストに、itemを追加（上記条件が適合した場合）する。
  }

  // 予定表にアクション（blockが該当）を挿入する block:名前渡しパラメータ ... P.179
  // 名前渡しパラメータの場合、メソッドに渡された時点では評価せず（計算を行わない）、実際に展開された時点で評価される。
  // 例えば、afterDelay(delay){count += 1}のような関数コールを行った場合、第二引数の「count += 1」は、作業項目に
  // 格納されているアクションをシミュレーションワークフレームが呼び出すまでは、countはインクリメントされない。
  def afterDelay(delay: Int)(block: => Unit) {  // 後に名前渡しパラメータのblockを実行しても戻り値はUnitである。
    val item = WorkItem(currentTime + delay, () => block)   //　第二パラメータは名前渡しパラメータのままitemに格納
    agenda = insert(agenda, item)
    // insertの第一パラメータのagendaは、どこの値？-> insertに渡すagendaは既存の値、insertの戻り値をagendaに再代入
  }

  private def next() {
    // @uncheckedアノテーション ... P.276 -> その後に記述されるパターンに対して徹底的なチェックは行われない。
    // 空リストが存在しないエラーを防ぐためにuncheckedアノテーションを付与
    // 「case _ :その他のケース」等の網羅的ケースの記載が不要となる。
    (agenda: @unchecked) match {
      case item :: rest =>  // 先頭のitemと、残りのrestに分解
        agenda = rest
        curtime = item.time   // currentTimeは、agendaの項目単位でが実行された時点で、更新される。
        item.action()
    }
  }

  def run() {
    // afterDelay(0)=予定表の先頭にコメント出力メソッドを追加
    afterDelay(0) {
      println("### simulation started, time=" + currentTime + " ###")
    }
    //予定表内のアクション実行開始
    // next()を呼び出せるのは、agendaがカラリストでない場合なので、next関数ないの空リスト対応ケースは記載せず、
    // uncheckedアノテーションを付与し、例外エラーの発生を抑える。
    while (!agenda.isEmpty) next()
  }
}

// 第2層：基本論理回路（半加算器を構成するインバータ・ANDゲート・ORゲート、探索器）のシミュレーションパッケージ
abstract class BasicCircuitSimulation extends Simuration {

  def InverterDelay: Int
  def AndGateDelay: Int
  def OrGateDelay: Int

  // 配線を表現するクラス
  class Wire {
    private var sigVal = false
    private var actions: List[Action] = List()
    def getSignal = sigVal    // 配線上の現在の信号を返す
    def setSignal(s: Boolean) =  // 配線の信号をsigにする
      if (s != sigVal) {
        sigVal = s
        // actionsリストの個々の要素に「_ ()」関数を適用する。
        // 「_ ()」は、f => f ()の略記法 ... 8.5節 = actions.foreach(f => f())
        actions foreach (_ ())
      }
    def addAction(a: Action) = {  // 配線のアクションに指定されたプロシージャpを付属させる。
      actions = a :: actions
      a()
    }
    def getWireID = {
      toString.split("[$]+")(1)
    }
  }

  // "="がある場合、最終的な関数の値が返されるが、戻り値を要求しない関数の為、不要？ ... http://www.javareading.com/bof/scala6.html
  def inverter(input: Wire, output: Wire) = {
    // インバーターのアクションを定義
    // 呼び出されると、入力信号を取得した後、出力信号を反転する別のアクションを予定表に追加する。
    def inverterAction() {
      val inputSig = input.getSignal
      // afterDelayの第二パラメータ{}は、名前渡しパラメータ
      afterDelay(InverterDelay) { // InverterDelayの値は？ ... CircuitSimulationクラスを呼び出したオブジェクトにて定義
        // println("### inverterAction - afterDelay ###")
        // output Wireに対し、WireクラスのsetSignalを使って、inputSigの逆の値を追加
        output setSignal !inputSig
      }
    }
    // input Wireに対し、WireクラスのaddActionを使って、inverterActionを追加
    input addAction inverterAction
    println("[A]inverterAction in: " + input.getWireID + "=" + input.getSignal + " | out: " + output.getWireID)
  }

  // "="がある場合、最終的な関数の値が返されるが、戻り値を要求しない関数の為、不要？
  def andGate(a1: Wire, a2: Wire, output: Wire) = {
    def andAction() = {
      val a1Sig = a1.getSignal
      val a2Sig = a2.getSignal
      // afterDelayの第二パラメータ{}は、名前渡しパラメータ
      afterDelay(AndGateDelay) { // AndGateDelayの値は？ ... CircuitSimulationクラスを呼び出したオブジェクトにて定義
        // println("### andAction - afterDelay ###")
        // output Wireに対し、WireクラスのsetSignalを使って、a1Sigとa2Sigの論理積を追加
        output setSignal (a1Sig & a2Sig)
      }
    }
    a1 addAction andAction
    a2 addAction andAction
    println("[A]andAction in: " + a1.getWireID + "=" + a1.getSignal + ", "  + a2.getWireID + "=" + a2.getSignal + " | out: " + output.getWireID)
  }

  def orGate(o1: Wire, o2: Wire, output: Wire) {  // "="がない場合、Unit型固定になる。
    def orAction() {
      val o1Sig = o1.getSignal
      val o2Sig = o2.getSignal
      // afterDelayの第二パラメータ{}は、名前渡しパラメータ
      afterDelay(OrGateDelay) { // OrGateDelayの値は？ ... CircuitSimulationクラスを呼び出したオブジェクトにて定義
        // println("### orAction - afterDelay ###")
        // output Wireに対し、WireクラスのsetSignalを使って、o1Sigとo2Sigの論理和を追加
        output setSignal (o1Sig | o2Sig)
      }
    }
    o1 addAction orAction
    o2 addAction orAction
    println("[A]orAction in: " +  o1.getWireID + "=" + o1.getSignal + ", "  + o2.getWireID + "=" + o2.getSignal + " | out: " + output.getWireID)
  }

  // Wireにプローブ（探測器）を追加する
  def probe(name: String, wire: Wire) {  // "="がない場合、Unit型固定になる。
    def probeAction() {
      println(name + " time=" + currentTime + " | new-value=" + wire.getSignal)
    }
    wire addAction probeAction
  }
}

// 第3層：ユーザー定義回路のライブラリー
abstract class CircuitSimulation extends BasicCircuitSimulation {

    def halfAdder(a: Wire, b: Wire, s: Wire, c: Wire) {
      val d, e = new Wire
      orGate(a, b, d)
      andGate(a, b, c)
      inverter(c, e)
      andGate(d, e, s)
    }

    def fullAdder(a: Wire, b: Wire, cin: Wire, sum: Wire, cont: Wire) {
      val s, c1, c2 = new Wire
      halfAdder(a, cin, s, c1)
      halfAdder(b, s, sum, c2)
      orGate(c1, c2, cont)

      /* 本来の綺麗な配列？
      halfAdder(a, b, s, c1)
      halfAdder(cin, s, sum, c2)
      orGate(c1, c2, count)
       */
    }
}