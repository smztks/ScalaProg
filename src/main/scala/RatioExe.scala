
/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/10/19
 * Time: 10:52
 * To change this template use File | Settings | File Templates.
 */

import com.example.scalapkg.Rational

object RatioExe {
  def main(args: Array[String]): Unit = {
    var r1 = new Rational(1,3)  // 1/3
    var r2 = new Rational(1,4)  // 1/4
    // Orderdトレイト + compareメソッドにより評価可能
    println(r1 < r2)
    println(r1 > r2)
    println(r1 >= r2)
    println(r1 <= r2)

    // Orderdトレイト不要
    println(r1 == r2)
    println(r1 != r2)
  }
}
