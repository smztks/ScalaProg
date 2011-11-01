package com.example.scalapkg

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/10/31
 * Time: 12:26
 * To change this template use File | Settings | File Templates.
 */

// equalsやhashCodeの実装は抽象クラスの実装ごとに追加する

// 二分木を抽象クラスとして定義します
trait Tree[+T] {
  def elem: T
  def left: Tree[T]
  def right: Tree[T]
}
// 木が無い場合のオブジェクトです
object EmptyTree extends Tree[Nothing] {
  def elem = throw new NoSuchElementException("EmptyTree.elem")
  def left = throw new NoSuchElementException("EmptyTree.left")
  def right = throw new NoSuchElementException("EmptyTree.right")
}
// 部分木クラスを定義します
class Branch1[+T](
  val elem: T,
  val left: Tree[T],
  val right: Tree[T]
) extends Tree[T] {
  override def equals(other: Any) =
    other match {
      case that: Branch1[T] => this.elem == that.elem &&
                               this.left == that.left &&
                               this.right == that.right
      case _ => false
    }
}

// [t]もしくは、[_]を使うことで、任意の型のBranchで成功する為、unchecked　warning回避できる
class Branch2[+T](
  val elem: T,
  val left: Tree[T],
  val right: Tree[T]
) extends Tree[T] {
  override def equals(other: Any) =
    other match {
      case that: Branch1[_] => this.elem == that.elem &&  // [t]もしくは、[_]で警告回避
                               this.left == that.left &&
                               this.right == that.right
      case _ => false
    }
  def canEqual(other: Any) = other.isInstanceOf[Branch2[_]]
  /*
  def canEqual(other:Any) = other mtach {
    case that:Branch[_] => true
    case _ => false
  }
   */
  override def hashCode: Int = {
    println("hashCode: " +
      41 * (
        41 * (
          41 + elem.hashCode
          ) + left.hashCode
        ) + right.hashCode
    )
    41 * (
      41 * (
        41 + elem.hashCode
        ) + left.hashCode
      ) + right.hashCode
  }
}