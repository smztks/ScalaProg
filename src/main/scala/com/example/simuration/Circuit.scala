package com.example.simuration

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/11/02
 * Time: 13:46
 * To change this template use File | Settings | File Templates.
 */

import scala.actors.Actor
import com.example.simuration.ParallelSimulation._

class Circuit {
  val clock = new Clock

  // シミュレーションメッセージ
  // 相互に送りたい情報の種類を表すメッセージ型が必要
  // ゲートは、出力線に状態変更を知らせる。配線は、状態変更が起きると、ゲートに入力を知らせなければならない。
  case class SetSignal(sig: Boolean)
  case class SignalChanged(wire: Wire, sig: Boolean)

  // ディレイ定数　... イベントの発生タイミング（ディレイ値）を指定
  val WireDelay = 1
  val InverterDelay = 2
  val OrGateDelay = 3
  val AndGateDelay = 3

  // 配線やゲート、その他のシミュレートされるべきオブジェクトは、Simulantトレイトをミックスインする。
  // Wire（配線）クラス：現在の信号の状態（High/Low）を持つ部品オブジェクトで、その状態を監視しているゲートのリストを抱えている。
  class Wire(name: String, init: Boolean) extends Simulant {
    def this(name: String) { this(name, false) }
    def this() { this("unnamed") }

    val clock = Circuit.this.clock
    clock.add(this)

    private var sigVal = init
    private var observers: List[Actor] = List()

    // シミュレーションメッセージにどのように応答するか規定するメソッドを必要とする。
    // 配線が受け取るメッセージは、信号を変更するSetSignalメッセージだけである。
    // この信号を受け取った配線は、パラメーターの信号が現在のものと異なる場合には、現在の状態を変更し、新しい信号を伝える。
    def handleSimMessage(msg: Any) {
      msg match {
        case SetSignal(s) =>
          if (s != sigVal) {
            sigVal = s
            signalObservers()
          }
      }
    }

    // 配線の信号の変化がそれを監視しているゲートのどのように伝えられるかを示す。監視ゲートに配線の初期状態を知らせる。
    def signalObservers() {
      for (obs <- observers)
        clock ! AfterDelay(
          WireDelay,
          SignalChanged(this, sigVal),
          obs
        )
    }
    // シミュレーション開始時に初期信号を送る
    override def simStarting() { signalObservers() }

    // 新しいゲートを接続するためのメソッド
    def addObserver(obs: Actor) {
      observers = obs :: observers
    }
    
    // toStringの改良
    override def toString = "Wire(" + name + ")"
  }

  // Notゲート用のダミー配線
  private object DummyWire extends Wire("dummy")

  // ゲート（インバータ・ANDゲート・ORゲート）クラス
  abstract class Gate(in1: Wire, in2: Wire, out: Wire) extends Simulant {
    // 入力に基づいて出力を計算する抽象メソッド
    def computeOutput(s1: Boolean, s2: Boolean): Boolean
    // ゲートの種類によって伝播に要するディレイが異なるので、抽象valを使う
    val delay: Int
    // GeteはSimulantをミックスインするので、使っているクロックがどれか指定しなければならない。
    // Gateは、Wireと同様に、所属するCircuitのクロックを指定しなければならない。
    val clock = Circuit.this.clock
    clock.add(this)
    // 通常のメソッド呼び出しを使ってゲートと2本の入力線を先に接続
    in1.addObserver(this)
    in2.addObserver(this)
    // 新しい出力は、2本の入力線の状態から計算しなければならないので、入力線の状態を保存する。
    var s1, s2 = false

    // ゲートが処理しなければならないメッセージは、片方の入力線の状態が変わったことを知らせるSingnalChangedメッセージだけ
    // このメッセージが届いたときに、2つのことをしなければならない。
    // ①配線の状態を管理するローカル変数を状態変更に合わせて更新する。
    // ②新しい出力を計算し、SetSignalメッセージで出力線にそれを送ること。
    def handleSimMessage(msg: Any) {
      msg match {
        case SignalChanged(w, sig) =>
          if (w == in1)
            s1 = sig
          if (w == in2)
            s2 = sig
          clock ! AfterDelay(delay, SetSignal(computeOutput(s1, s2)), out)
      }
    }
  }

  def orGate(in1: Wire, in2: Wire, output: Wire) =
    new Gate(in1, in2, output) {
      val delay = OrGateDelay
      def computeOutput(s1: Boolean, s2: Boolean) = s1 || s2
    }

  def andGate(in1: Wire, in2: Wire, output: Wire) =
    new Gate(in1, in2, output) {
      val delay = AndGateDelay
      def computeOutput(s1: Boolean, s2: Boolean) = s1 && s2
    }

  def inverter(input: Wire, output: Wire) =
    new Gate(input, DummyWire, output) {
      val delay = InverterDelay
      def computeOutput(s1: Boolean, ingnored: Boolean) = !s1
    }

  // その他のユーティリティメソッド
  def probe(wire: Wire) = new Simulant {
    val clock = Circuit.this.clock
    clock.add(this)
    wire.addObserver(this)
    def handleSimMessage(msg: Any) {
      msg match {
        case SignalChanged(w, s) =>
          println("signal " + w + " changed to " + s)
      }
    }
  }

  // シミュレーションの開始手段…クロックにStartメッセージを送るだけで実装できる。
  def start() { clock ! Start }
}