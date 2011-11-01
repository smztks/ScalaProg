package com.example.scalaprog

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/10/31
 * Time: 14:36
 * To change this template use File | Settings | File Templates.
 */

// 第31章 ScalaとJavaの統合
object ScalaProg31

// JavaからScalaを使うための注意点
// シングルトンオブジェクト
object Sp31_01_03 {

  // "C:\Program Files\Java\jdk1.6.0_25\bin\javap" -classpath C:\scala\project\ScalaProgramming\target\scala-2.8.1.final\classes HelloWorld
  /*
  Compiled from "HelloWorld.scala"
  public final class HelloWorld extends java.lang.Object{
      public static final void main(java.lang.String[]);
      public static final void scala$Application$_setter_$executionStart_$eq(long);
      public static final long executionStart();
  }
   */
  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// アノテーション
// 投げられた例外
object Sp31_02_02 {

  import com.example.scalapkg._
  val reader = new Reader("README")
  println(reader.readline)

  // readメソッドには、IOExceptionを投げるかもしれないというJavaのthrows節が伴っている。

  // "C:\Program Files\Java\jdk1.6.0_25\bin\javap" -classpath C:\scala\project\ScalaProgramming\target\scala-2.8.1.final\classes\com\example\scalapkg Reader
  /*
    Compiled from "Reader.scala"
    public class com.example.scalapkg.Reader extends java.lang.Object implements scala.ScalaObject{
        public int read()       throws java.io.IOException;
        public java.lang.String readline();
        public com.example.scalapkg.Reader(java.lang.String);
    }
   */

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// Javaアノテーション
object Sp31_02_03 {
  // C:\scala\project\Learning\SetTest.scala
  /*
  import org.junit.Test
  import org.junit.Assert.assertEquals

  class SetTest {

    @Test
    def testMultiAdd {
      val set = Set() + 1 + 2 + 3 + 1 + 2 + 3
      assertEquals(3, set.size) // Set（集合）だから、個数は3個のはず
    }
  }
  */
  //scalac SetTest.scala -cp C:\scala\libraries\junit\junit-4.10.jar

  /*

  C:\scala\project\Learning>scala -cp C:\scala\libraries\junit\junit-4.10.jar org.junit.runner.JUnitCore SetTest
  scala -cp C:\scala\libraries\junit\junit-4.10.jar;. org.junit.runner.JUnitCore SetTest
  JUnit version 4.10
  .
  Time: 0.109

  OK (1 test)
   */

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 独自アノテーションの開発
object Sp31_02_04 {
  /*
  --------------------------------------------------
  import java.lang.annotation.*;
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  public @interface Ignore {}
  --------------------------------------------------
  "C:\Program Files\Java\jdk1.6.0_25\bin\javac" C:\scala\project\Learning\Ignore.java

  --------------------------------------------------
    object Tests {
      @Ignore
      def testData = List(0, 1, -1, 5, -5)

      def test1 {
        assert(testData == (testData.head :: testData.tail))
      }

      def test2 {
        assert(testData.contains(testData.head))
      }
    }
  --------------------------------------------------
  scalac Tests.scala
  
  --------------------------------------------------
  object FindTests extends Application {
    for {
      method <- Tests.getClass.getMethods
      if method.getName.startsWith("test")
      if method.getAnnotation(classOf[Ignore]) == null
    } {
      println("found a test method: " + method)
    }
  }


  --------------------------------------------------
  scalac FindTests.scala

  scala FindTests
  --------------------------------------------------
  found a test method: public void Tests$.test1()
  found a test method: public void Tests$.test2()
  --------------------------------------------------

   */
  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 存在型 (1)
object Sp31_03a {
  /*
--------------------------------------------------
import java.util.Collection;
import java.util.Vector;

// This is a Java class with wildcards
public class Wild {
  Collection<?> contents() {
    Collection<String> stuff = new Vector<String>();
    stuff.add("a");
    stuff.add("b");
    stuff.add("see");
    return stuff;
  }
}
--------------------------------------------------
"C:\Program Files\Java\jdk1.6.0_25\bin\javac" C:\scala\project\Learning\Wild.java

scala> val contents = (new Wild).contents
--------------------------------------------------
java.lang.IllegalAccessError: tried to access method Wild.contents()Ljava/util/Collection; from class
--------------------------------------------------

   */
  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 存在型 (2)
object Sp31_03b {

  // ①メソッドに存在型を渡すときには、forSome節からメソッドに型パラメーターを移す。
  // メソッド本体の中では、型パラメーターを使ってforSome節内の型を参照できる。
  // ②メソッドから存在型を返す代わりに、forSome節内の1つ1つの型の抽象メンバーを持つオブジェクトを返す。
  // （抽象メンバーについては、第20章を参照のこと）
  import scala.collection.mutable.Set
  import java.util.Collection

  abstract class SetAndType {
    type Elem
    val set: Set[Elem]
  }
  def javaset2ScalaSet[T](jset: Collection[T]): SetAndType = {
    val sset =Set.empty[T] // Tと呼べるようになった
    val iter = jset.iterator
    while (iter.hasNext)
      sset += iter.next()
    return new SetAndType {
      type Elem = T
      val set = sset
    }
  }
  // 存在型を抽象メンバーに変換する」とのことなので、最初っから抽象メンバー使った方がよい。
  // そんなわけで普段はScalaコードで存在型を使わない。

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// synchronizedの使い方 (1)
object Sp31_04a {
  /*
  var counter = 0
  synchronized {
    counter = counter + 1
  }
   */

  // http://d.hatena.ne.jp/unageanu/20080724/1216903291
  def synchronized[R](obj: AnyRef)(proc: => R): R = {
    obj.synchronized {
      proc
    }
  }

  val obj = new java.lang.Object
  synchronized(obj) {
    println("aaaa")
  }

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// synchronizedの使い方 (2)
object Sp31_04b {

  // http://d.hatena.ne.jp/unageanu/20080618

  import scala.concurrent.ops._
  import java.lang.Thread._

  // 同期化用オブジェクト
  val monitor = new Object

  // 並列実行する処理
  // monitor.synchronizedにより、複数スレッドで並列に実行されることはない。
  def proc(name: String) = monitor.synchronized(
    0 to 3 foreach(i => println(i + ":" + name + " : meow!"))
  )

  // 3スレッドで並列実行
  spawn(proc("mii"))
  spawn(proc("tora"))
  spawn(proc("shiro"))

  sleep(1000) // ほんとはちゃんと待たないといけない。

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}