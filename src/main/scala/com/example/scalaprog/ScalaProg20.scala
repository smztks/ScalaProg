package com.example.scalaprog

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/10/19
 * Time: 12:08
 * To change this template use File | Settings | File Templates.
 */

// 第20章 抽象メンバー
object ScalaProg20

// 抽象メンバーの弾丸ツアー
object Sp20_01 {
  trait Abstract { // Abstract=具体的な
    type T
    def transform(x: T): T
    val initial: T
    var current: T
  }
  class Concrete extends Abstract { // Concrete=具体的な
    type T = String
    def transform(x: String) = x + x
    //def transform(x: String): String = x + x
    val initial = "hi"
    var current = initial
  }

  // Scalaの抽象型とは、定義を指定せずにtypeキーワードによってクラスやトレイトの
  // メンバーとして宣言された型のことである。
  // AbstractトレイトのT型のように、何らかのクラスやトレイトのメンバーのこと

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 抽象val
object Sp20_03 {
  trait Abstract { // Abstract=具体的な
    val initial: String
  }
  class Concrete extends Abstract { // Concrete=具体的な
    val initial = "hi"
  }
  println((new Concrete) initial)

  abstract class Fruit {
    val v: String
    def m: String
  }
  abstract class Apple extends Fruit {
    val v: String
    val m: String // OK defをvalでオーバーライトすることは認められる。
  }
  abstract class BadApple extends Fruit {
    // メソッドは、呼出されるたびに異なる値を返す可能性がある為、
    //def v: String // NG valをdevでオーバーライトすることはできない。
    def m: String
  }

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 抽象var
object Sp20_04 {
  trait AbstractTime1 {
    var hour: Int
    var minute: Int
  }

  trait AbstractTime2 {
    def hour: Int
    def hour_=(x: Int)
    def minute: Int
    def minute_=(x: Int)
  }

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 抽象valの初期化
object Sp20_05 {
  // クラスパラメータはクラスコンストラクターに渡される前に評価される（パラメータが名前渡しの場合を除く）
  // サブクラスでのval定義の実装は、スーパークラスが初期化された後に初めて評価される。

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 事前初期化済みフィールド
object Sp20_05_01 {
  import com.example.scalapkg._

  // 先にフィールド初期化セッションを実行した後、コンストラクターの呼び出しを行なう。
  val x = 2
  new {
    val numerArg = 1 * x
    val denomArg = 2 * x
    val a = 10
    var b = 20
  } with RationalTrait1

  // 事前初期化済みフィールドはオブジェクトや名前付きサブクラスでも使える
  object twoThirds extends {
    val numerArg = 1 * x
    val denomArg = 2 * x
    val a = 10
    var b = 20
  } with RationalTrait1
  println(twoThirds)

  val rc1 = new RationalClass(1, 3)
  val rc2 = new RationalClass(1, 3)
  rc1.outputArg
  rc2.outputArg
  println(rc1 + rc2)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 遅延評価val ... lazy=遅延 ... valが初めて使われるまで評価をしない。
object Sp20_05_02 {
  object Demo1 {
    val x = { println("initializing x"); "done" }
  }
  Demo1
  println("Demo1 Ref END")
  println(Demo1.x)

  object Demo2 {
    lazy val y = { println("initializing y"); "done" }
  }
  Demo2
  println("Demo2 Ref END")
  println(Demo2.y)
  println(Demo2.y)

  object Demo3 {
    lazy val z = { println("initializing z"); "done" }
  }
  println(Demo3.z)
  println(Demo3.z)

  import com.example.scalapkg._

  val x = 2
  val rt = new RationalTrait2 {
    val numerArg = 1 * x
    val denomArg = 2 * x
  }
  println(rt)

  // lazyによる遅延評価は副作用がある場合や副作用に依存する場合は危険
  // 初期化の実行順が意味を持たない関数型オブジェクトに適している。... 命令型で書かれているコードには不適切

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 抽象型
object Sp20_06 {
  /*
  class Food
  abstract class Animal {
    def eat(food: Food)
  }
  class Grass extends Food
  class Cow extends Animal {  // error: class Cow needs to be abstract, since method eat in class Animal of type (food: Food)Unit is not defined
    override def eat(food: Grass) {} // error: method eat overrides nothing
  }

  class Fish extends Food
  val bessy: Animal = new Cow
  bessy eat (new Fish) // bessyという牛が魚も食べることができてしまう。
   */

  class Food
  abstract class Animal {
    type SuitableFood <: Food // Foodという上限境界を指定し、SuitableFoodはFoodのサブ型であればOKという形式にする。
    def eat(food: SuitableFood)
  }
  class Grass extends Food
  class Cow extends Animal {
    type SuitableFood = Grass
    override def eat(food: Grass) {}
  }

  class Fish extends Food
  val bessy:Animal = new Cow
  //bessy eat (new Fish) // type mismatch; ... bessyという牛が魚は食べられない。
  //bessy eat (new Grass) // bessyの抽象度をAnimalにあげるとこれもエラーとなる。

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// パス依存型
object Sp20_07 {
  class Food
  abstract class Animal {
    type SuitableFood <: Food // Foodという上限境界を指定し、SuitableFoodはFoodのサブ型であればOKという形式にする。
    def eat(food: SuitableFood)
  }

  class Grass extends Food
  class Cow extends Animal {
    type SuitableFood = Grass
    override def eat(food: Grass) {}
  }

  class DogFood extends Food
  class Dog extends Animal {
    type SuitableFood = DogFood
    override def eat(food: DogFood) {}
  }

  val bessy = new Cow
  val lassie = new Dog
  // lassie eat (new bessy.SuitableFood)  // 牛(bessy)にあった餌を食べようとしてエラー
  lassie eat (new lassie.SuitableFood)  // OK

  val bootsie = new Dog
  lassie eat (new bootsie.SuitableFood)  // 同じDogクラスSuitableFoodなのでOK


  class Outer {
    class Inner
  }
  val o1 = new Outer
  val o2 = new Outer
  new o1.Inner  // 何らかの形で外部クラスインスタンスを指定しなければ、内部クラスのインスタンスを作ることはできない。
  new o1.Inner

  //new Outer#Inner // error: Outer is not a legal prefix for a constructor


  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 構造的サブ型 refinement=改良
object Sp20_08 {
  class Food
  abstract class Animal {
    type SuitableFood <: Food // Foodという上限境界を指定し、SuitableFoodはFoodのサブ型であればOKという形式にする。
    def eat(food: SuitableFood)
  }

  class Grass extends Food
  class Cow extends Animal {
    type SuitableFood = Grass
    override def eat(food: Grass) {}
  }

  class Pasture {
    var animals: List[Animal { type SuitableFood = Grass }] = Nil
  }
  val grazer = new Pasture
  val bessy1, bessy2 = new Cow
  grazer.animals = List(bessy1, bessy2)


  import java.io.{File, PrintWriter}
  import java.util.Date
  import java.net.ServerSocket
  /*
  def withPrintWriter(file: File, op: PrintWriter => Unit) {
    val writer = new PrintWriter(file)
    try {
      op(writer)
    } finally {
      writer.close()
    }
  }
  withPrintWriter(
    new File("date.txt"),
    writer => writer.println(new java.util.Date)
  )
   */

  using(new PrintWriter("date.txt")) { writer =>
    writer.println(new Date)
  }
  using(new ServerSocket(9999)) { serverSocket =>
    println("listening on port 9999....")
    // socketのクリーンアップ
    using(serverSocket.accept()) { socket =>
      socket.getOutputStream().write("hello, world\n".getBytes)
    }
  }
  def using[T <: {def close(): Unit }, S](obj: T)(operation: T => S) = {
    val result = operation(obj)
    obj.close()
    result
  }

  // telnet localhost 9999

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 列挙(enumeration) ... 定数を表現する専用の型
object Sp20_09 {
  object Color extends Enumeration {
    //val Red = Value
    //val Green = Value
    //val Blue = Value
    // 「extends Enumeration」をミックスインすることで、Valueという名前の内部クラスを値として代入できる。
    val Red, Green, Blue = Value
  }

  // インポートする場合
  import Color._

  // Colorがパス、Value（Red,Green,Blueを示す）が依存型
  println(Color.Red)

  // Enumerationは、Valueという名前の内部クラスを定義しており、同名のパラメーターなしメソッドValueが
  // そのクラスの新しいインスタンスを返す。
  //object Direction extends Enumeration {
  //  val North, East, South, West = Value
  //}

  object Direction extends Enumeration {
    val North = Value("North!")
    val East = Value("East!")
    val South = Value("South!")
    val West = Value("West!")
  }

  //Direction.foreach(d => print(d + " "))
  for (d <- Direction.values) print(d + "|")
  println()
  for (d <- Direction.values) print(d.id + "|")
  println()
  println(Direction(0) + "|" + Direction(1) + "|" + Direction(2) + "|" + Direction(3))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// ケーススタディ:通貨計算
object Sp20_10 {

  import com.example.scalapkg._

  val doller100 = US.make(100)
  val doller10 = US.make(10)
  val doller5 = US.make(5)
  println (doller100)
  println (doller10 + doller5)
  println (doller10 * 5)

  val euro150 = Europe.make(150)
  val euro15 = Europe.make(15)
  val euro7 = Europe.make(7)
  println (euro150)
  println (euro15 + euro7)
  println (euro7 * 5)

  val japan200 = Japan.make(200)
  val japan20 = Japan.make(20)
  val japan15 = Japan.make(15)
  println (japan200)
  println (japan20 + japan15)
  println (japan15 * 5)

  println("Japan.Yen: " + Japan.Yen + "|" + "US.Dollar: " + US.Dollar)
  val JfromU = Japan.Yen from US.Dollar * 100 // 100ドルを円に換算
  println(JfromU)
  val EfromJ = Europe.Euro from JfromU
  println(EfromJ)
  val UfromE = US.Dollar from EfromJ
  println(UfromE)

  println((US.Dollar * 100 + US.Cent * 5 + UfromE) / 2 * (US.Dollar * 2))

  //println(US.Dollar + Europe.Euro) // Error: type mismatch;

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}
