package com.example.simuration

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/11/02
 * Time: 11:09
 * To change this template use File | Settings | File Templates.
 */

import scala.actors.Actor
import Actor._

object ParallelSimulation {
  // 現在の時刻を維持管理する「クロック」アクターを用意し、部品オブジェクト（シミュレーションに含まれる配線、ゲート等）
  // に先に進むタイミングを通知する。クロックは念入りに選ばれたタイミングでアクターにpingメッセージを送り、
  // 現在のタイムチックに対応するすべてのメッセージを受信し、処理したかどうかを確かめる。
  // クロックが部品オブジェクトに送るPingメッセージと、クロックを先に動かせる状態になったオブジェクトがクロックに
  // 送り返すPongメッセージを作る。
  case class Ping(time: Int)
  case class Pong(time: Int, from: Actor)

  // 新しい作業アイテムをスケジューリングするメソッドは、クロックから送られるメッセージに変更
  // WorkItemクラスと同様に、引数としてメッセージやターゲットアクターをとる。
  case class AfterDelay(delay: Int, msg: Any, target: Actor)

  // シミュレーションの開始・停止を要求するメソッド
  case object Start
  case object Stop

  // 現在の時刻を維持管理する「クロック」アクター
  class Clock extends Actor {
    // 起動時のクロックは、runningにfalseがセットされた状態になっている。シミュレーションの初期化が終わると、
    // クロックにはStartメッセージが送られ、runningがtrueになる。
    // 部品が全部接続されるまで、シミュレーションは凍結されているので、アクターメッセージ送信を使わず、
    // 通常のメソッド呼び出しでセットアップを行なっても安全である。
    private var running = false
    // 現在のタイムチック
    private var currentTime = 0
    private var agenda: List[WorkItem] = List()
    // クロックが管理するオブジェクトのリスト
    private var allSimulants: List[Actor] = List()
    // 現在のタイムチックの処理をまだ実行しているオブジェクトのリスト
    // 予測できない順序で項目が削除されていくので集合で定義している。
    // 実行を開始したシミュレーターは、busySimulantsが空にならなければタイムチックを先に進めない。
    // クロックを進める時には、busySimulantsにはallSimulantsの内容がセットされる。
    private var busySimulants: Set[Actor] = Set.empty

    // クロック自体は作成してしまえば、先回りして実行してもかわまわない。
    start()

    // シミュレーションのセットアップでは、新しい部品オブジェクトをクロックに追加するメソッドが必要
    def add(sim: Simulant) {
      allSimulants = sim :: allSimulants
    }

    // ターゲットアクターとアクターに送るべきメッセージによってアクションを表現する
    case class WorkItem(time: Int, msg: Any, target: Actor)

    // クロックの動作：メインループは、クロックを先に進めること、メッセージに応答することの2つの処理を実行する。
    // クロックを先に進めると、少なくとも1つのメッセージを受信しなければ、次のタイムチックにクロックを進めることができない。
    def act() {
      loop {
        if (running && busySimulants.isEmpty)
          advance() // タイムチックを進める処理
        reactToOneMessage() // クロックが受け取るメッセージの処理
      }
    }
    // タイムチックを進める処理
    // ①の条件：予定表が空で、シミュレーションがすでに開始されているなら、シミュレーションを終了しなければならない。
    // ②の条件：予定表が空でないとすると、currentTimeのすべての作業項目を実行しなければならない。
    // ③の条件：べての部品オブジェクトをbusySimulantsにコピーして、Pingを送らなければならない。クロックは、すべての
    // 部品オブジェクトがPingに対する応答を返してくるまで、タイムチックを先に進めない。
    def advance() {
      // ①の条件
      if (agenda.isEmpty && currentTime > 0) {
        println("** Agenda empty. Clock exiting at time "+ currentTime + ".")
        self ! Stop
        return
      }
      currentTime += 1
      println("Advancing to time " + currentTime)
      // ②の条件
      processCurrentEvents()
      // ③の条件
      for (sim <- allSimulants)
        sim ! Ping(currentTime)
      busySimulants = Set.empty ++ allSimulants
    }

    // 現在のイベント処理：予定表の先頭にあるイベントの中でtimeがcurrentTimeになっているものをすべて処理すること
    private def processCurrentEvents() {
      // このタイムチックで実行しなければならいない作業項目を選択し、todoNowに格納
      val todoNow = agenda.takeWhile(_.time <= currentTime)
      // dropを使ってagendaからtodoNow分の項目を削除する。
      agenda = agenda.drop(todoNow.length)
      // このタイムチックで実行する作業項目をループで処理してターゲットにメッセージを送る。
      // assert処理は、スケジューラーのロジックが狂っていないことを確かめる為のものである。
      for (WorkItem(time, msg, target) <- todoNow) {
        assert(time == currentTime)
        target ! msg
      }
    }

    // クロックが受け取るメッセージの処理
    def reactToOneMessage() {
      react {
        // 作業項目の待ち行列に新しい作業項目を追加する。
        case AfterDelay(delay, msg, target) =>
          val item = WorkItem(currentTime + delay, msg, target)
          agenda = insert(agenda, item)
        // busySimulatnsから送信元オブジェクトを取り除く
        case Pong(time, sim) =>
          assert(time == currentTime)
          assert(busySimulants contains sim)
          busySimulants -= sim
        // シミュレーションを開始する。
        case Start => running = true
        // クロックを止める。
        case Stop =>
          for (sim <- allSimulants)
            sim ! Stop
          exit()
      }
    }

    // 予定表が正しくソートされた状態になるようにパラメーターの作業項目を予定表に追加する。
    private def insert(ag: List[WorkItem], item: WorkItem): List[WorkItem] = {
      if (ag.isEmpty || item.time < ag.head.time) item :: ag    // 空白リストもしくは、先頭リストの時刻より小さい場合
      else ag.head :: insert (ag.tail, item)  // 先頭（再帰した場合は、tailの先頭）リストに、itemを追加（上記条件が適合した場合）する。
    }
  }

  // シミュレートされるオブジェクトの間には、種類が異なっても共通の振る舞いが現れるので、Simulantトレイトを定義する。
  trait Simulant extends Actor {
    val clock: Clock  // 部品オブジェクトの抽象メンバー（作成と同時に実行を開始するが、クロックがメッセージを送ってくるまで実際には何もできない）
    def handleSimMessage(msg: Any)  // 部品オブジェクトの抽象メンバー
    def simStarting() { }
    def act() {
      loop {
        react {
          // 部品オブジェクトは、Stopメッセージを受け取ると終了する。
          case Stop => exit()
          // timeが1のときに送られたきたPingに対しては、Pongを送り返す前にsimStartingを呼び出す。
          // simStartingを利用するように、サブクラスでは、シミュレーションが実行を開始したときに実行すべき動作を定義する。
          case Ping(time) =>
            if (time == 1) simStarting()
            clock ! Pong(time, self)
          case msg => handleSimMessage(msg)
        }
      }
    }
    start()
  }
}
