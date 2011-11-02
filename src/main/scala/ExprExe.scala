/*************************************************
// 日本語表示可能なUTF-8
//P.284
 */


// 算術式整形ライブラリをインポートしマス
import com.example.scalapkg._

// Applicationトレイトを使って処理を見やすくします
object ExprExe {
  def main(args: Array[String]): Unit = {
    val f = new ExprFormatter

    //// 算術式を定義します
    // (1 / 2 ) * (x + 1)です
    val e1 = BinOp("*", BinOp("/", Number(1), Number(2)), BinOp("+", Var("x"), Number(1)))
    // (1 / 2 ) * (1.5 / x)です
    val e2 = BinOp("*", BinOp("/", Var("x"), Number(2)), BinOp("/", Number(1.5), Var("x")))
     // ((1 / 2 ) * (x + 1))  / ((1 / 2 ) * (1.5 / x))です
    val e3 = BinOp("/", e1, e2)

    // 各算術式を表示しますよー
    def show(e:Expr) = println(f.format(e) + "\n\n")
    for(val e <- Array(e1, e2, e3)) show(e)
  }
}

