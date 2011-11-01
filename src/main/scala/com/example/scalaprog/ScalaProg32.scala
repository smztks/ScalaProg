package com.example.scalaprog

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/10/31
 * Time: 17:16
 * To change this template use File | Settings | File Templates.
 */

// 第32章 アクターと並行プログラミング
object ScalaProg32

// アクターとメッセージ交換 (1)
object Sp32_02a {

  import scala.actors._

  object SillyActor extends Actor {
    def act() { // actという名称の関数が必須
      for (i <- 1 to 5) {
        println("I'm acting!")
        Thread.sleep(1000)
      }
    }
  }

  object SeriousActor extends Actor {
    def act() { // actという名称の関数が必須
      for (i <- 1 to 5) {
        println("To be or not to be.")
        Thread.sleep(1000)
      }
    }
  }

  SillyActor.start(); SeriousActor.start()
  // Scalaインタプリターで実施すると、コンソールを実行しながらも、「I'm acting!」が表示される。
}

// アクターとメッセージ交換 (2)
object Sp32_02b {

  import scala.actors.Actor._

  val seriousActor2 = actor {
    for (i <- 1 to  5) {
      println("That is the question.")
      Thread.sleep(1000)
    }
  }
  // Scalaインタプリターで定義した時点で、すぐ実行される。

  // recieveを呼び出すアクター
  val echoActor = actor {
    while (true) {
      receive {
        case msg => println("recived message: " + msg)
      }
    }
  }
  echoActor ! "hi there"
  // recived message: hi there
  echoActor ! 15
  // recived message: 15

  // Int型のメッセージだけを処理するアクター
  val intActor = actor {
    //while (true) {
      receive {
        case x: Int => println("Got an Int: " + x)
      }
    //}
  }
  intActor ! "hello"
  intActor ! 12
  intActor ! math.Pi
  intActor ! 10  // reciveの外側にwhileを入れないと、2度目以降の成功パターンは、呼ばれない。

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// アクターとメッセージ交換 (3)
object Sp32_02c {
  
  import scala.actors.Actor._
  
  val multiActor = actor {
    while (true) {
      receive {
        case x:Int => println("メッセージは整数の " + x)
        case x:Double => println("メッセージは小数の " + x)
        case x:String => println("メッセージは文字列の " + x)
        case x => println("メッセージはなにかしらの " + x)
      }
    }
  }
  multiActor ! 15
  multiActor ! "String Message"
  multiActor ! 0.12345
  multiActor ! None
  
  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// ネイティブスレッドをアクターとして扱う
object Sp32_03 {

  import scala.actors.Actor._

  self ! "hello"

  // メッセージが送信されなかったり遅延すると、下記処理のではシェルをブロックしてしまう。
  // self.receive { case x => x }
  // res11: Any = hello

  // 対話型シェルでreceiveを使った場合、receiveメソッドがメッセージを受け取るまで
  // シェルを占有してしまって操作が効かなくなってしまうのでタイムアウト時間を指定する。
  self.receiveWithin(1000) { case x => x }  // 1秒だけ待つ

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// スレッドの再利用によるパフォーマンスの向上 (1)
object Sp32_04a {
  // アクターは全てのactメソッドに出番を与えるために専用のJavaスレッドが与えられている
  // Javaのスレッドはかなりのメモリを消費する&スレッドを頻繁に切り替えるとプロセッサーサイクルををかなり消費する
  // 処理効率を上げるためにはスレッドの作成や切り替えは控えめにしたほうがよい

  // Scalaは、スレッドの節約を助けるため、通常のreciveメソッドの代わりに使えるreactというメソッドを提供している。
  // raactは制御を返さないので、reactの引数を受け取るメッセージハンドラーは、メッセージを処理するとともに、
  // アクターの残りの仕事を調整しなければならない。

  import scala.actors._
  import scala.actors.Actor._

  object NameResolver extends Actor {
    import java.net.{InetAddress, UnknownHostException }
    def act() {
      react {
        case (name: String, actor: Actor) =>
          val ip = show(getIp(name))
          // println("Debug IP address: " + ip)
          actor ! ip
          act()
        case "EXIT" =>
          println("Name resolver exiting.")
          // 終了
        case msg =>
          println("Unhandled message: " + msg)
          act()
      }
    }
    def getIp(name: String): Option[InetAddress] = {
      try {
        Some(InetAddress.getByName(name))
      } catch {
        case _:UnknownHostException => None
      }
    }
    def show(x: Option[java.net.InetAddress]) =
      x match {
        case Some(y) => y
        case None    => "Nothing!"
      }
  }
  NameResolver.start()

  NameResolver ! ("www.scala-lang.org", self)
  // Debug IP address: www.scala-lang.org/128.178.154.159
  self.receiveWithin(100) { case x => println(x) }
  // res2: Any = www.scala-lang.org/128.178.154.159

  NameResolver ! ("wwwwww.scala-lang.org", self)
  // Debug IP address: Nothing!
  self.receiveWithin(100) { case x => println(x) }
  // res4: Any = Nothing!

  NameResolver ! ("EXIT")

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// スレッドの再利用によるパフォーマンスの向上 (2) loop版&EXIT無し
object Sp32_04b {

  import scala.actors._
  import scala.actors.Actor._

  object NameResolver extends Actor {
    import java.net.{InetAddress, UnknownHostException }
    def act() {
      loop {
        react {
          case (name: String, actor: Actor) =>
            val ip = show(getIp(name))
            // println("Debug IP address: " + ip)
            actor ! ip
          case msg =>
            println("Unhandled message: " + msg)
        }
      }
    }
    def getIp(name: String): Option[InetAddress] = {
      try {
        Some(InetAddress.getByName(name))
      } catch {
        case _:UnknownHostException => None
      }
    }
    def show(x: Option[java.net.InetAddress]) =
      x match {
        case Some(y) => y
        case None    => "Nothing!"
      }
  }
  NameResolver.start()

  NameResolver ! ("www.scala-lang.org", self)
  // Debug IP address: www.scala-lang.org/128.178.154.159
  self.receiveWithin(100) { case x => println(x) }
  // res2: Any = www.scala-lang.org/128.178.154.159

  NameResolver ! ("wwwwww.scala-lang.org", self)
  // Debug IP address: Nothing!
  self.receiveWithin(100) { case x => println(x) }
  // res4: Any = Nothing!

 /*
  ■raactの仕組み ... reactは次のような流れで処理を進めていく
  1.startメソッドは、何らかのスレッドが最終的にそのアクターのactを呼び出すように調整する
  2.actメソッドがreactを呼び出すとアクターのメールボックスに部分関数が処理できるメッセージがあるかどうかをチェック
  3.処理できるメッセージを見つけるとreactはあとでメッセージを処理できるようにスケジューリングして例外を投げる
  4.メッセージが見つからないとアクターは「冷蔵庫」に入れられて、メールボックスに他のメッセージが送られてきたときに目を覚まして例外を投げる
  5.reactとactは例外を投げて異常終了
  6.actを呼び出したスレッドは例外をキャッチして他の仕事に進む

  */

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// アクターのコードスタイル
object Sp32_05 {
  println("this is a pen")
  
  println("end of: " + Thread.currentThread.getStackTrace()(1))
}