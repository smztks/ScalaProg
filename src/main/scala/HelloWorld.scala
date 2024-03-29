
/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/10/11
 * Time: 12:55
 * To change this template use File | Settings | File Templates.
 */

import com.example.scalaprog._

object HelloWorld {
  def main(args: Array[String]) {
    // 第16章 リストの操作
    //Sp16_01         // リストリテラル
    //Sp16_02         // List型
    //Sp16_03         // リストの構築
    //Sp16_04_01      // リストに対する基本操作
    //Sp16_04_02      // 挿入ソート
    //Sp16_05_01      // リストパターン
    //Sp16_05_02
    //Sp16_06_01      // 2個のリストの連結
    //Sp16_06_02      // 分割統治原則
    //Sp16_06_03      // リストの長さを計算する:length
    //Sp16_06_04      // リストの末尾へのアクセス:initとlast
    //Sp16_06_05_01   // リストの反転:reverse
    //Sp16_06_05_02
    //Sp16_06_06      // プレフィックスとサフィックス:drop,take,splitAt
    //Sp16_06_07      // 要素の選択:applyとindices
    //Sp16_06_08      // リストのリストから単層のリストへ:flatten
    //Sp16_06_09      // リストのジッパー操作:zipとunzip
    //Sp16_06_10      // リストの表示:toStringとmkString
    //Sp16_06_11      // リストの変換:iterator,toArray,copyToArray
    //Sp16_06_12      // サンプル:マージソート
    //Sp16_07_01      // Listクラスの高階メソッド
    //Sp16_07_02      // リストのフィルタリング:filter,partition,find,takeWhile,dropWhile,span
    //Sp16_07_03      // リストを対象とする述語関数:forallとexists
    //Sp16_07_04      // リストの畳み込み: /:と:\
    //Sp16_07_05      // foldを使ったリストの反転
    //Sp16_07_06      // リストのソート:sortWith
    //Sp16_08_01      // 要素からリストを作る:List.apply
    //Sp16_08_02      // 数値の範囲を作る:List.range
    //Sp16_08_03      // 同じ値のリストを作る:List.fill
    //Sp16_08_04      // 関数の実行結果による表の作成:List.tabulate
    //Sp16_08_05      // 複数のリストの連結:List.concat
    //Sp16_09         // 複数のリストをまとめて処理する方法
    //Sp16_10         // Scalaの型推論アルゴリズムを理解する

    // 第17章 コレクション
    //Sp17_01_01      // リスト
    //Sp17_01_02      // 配列
    //Sp17_01_03      // リストバッファー
    //Sp17_01_04      // 配列バッファー
    //Sp17_01_05      // 文字列(StringOps)
    //Sp17_02         // 集合(Set)とマップ(Map)
    //Sp17_02_01      // 集合の使い方
    //Sp17_02_02      // マップの使い方
    //Sp17_02_03      // デフォルトの集合とマップ
    //Sp17_02_04      // ソートされた集合とマップ （イミュータブル版しかない、赤黒木アルゴリズムを使用）
    //Sp17_03         // ミュータブルとイミュータブルのどちらを使うべきか
    //Sp17_04         // コレクションの初期化
    //Sp17_04_01      // 配列やリストへの変換
    //Sp17_04_02      // 集合・マップのミュータブル版とイミュータブル版の相互変換
    //Sp17_05         // タプル

    // 第18章 ステートフルオブジェクト
    //Sp18_01         // どのようなオブジェクトがステートフルなのか
    //Sp18_02         // 再代入可能な変数とプロパティ
    //Sp18_03         // ケーススタディ:離散イベントシミュレーション
                      // デジタル回路のための言語
                      // シミュレーションAPI
                      // デジタル回路のシミュレーション

    // 第19章 型のパラメータ化
    //Sp19_01         // 関数型待ち行列
    //Sp19_02_01      // 情報隠蔽 非公開コンストラクターとファクトリーメソッド
    //Sp19_02_02      // 非公開クラスというもう一つの方法
    //Sp19_03         // 変位指定アノテーション
    //Sp19_03_01      // 変位指定と配列
    //Sp19_04         // 変位指定アノテーションのチェック
    //Sp19_05         // 下限限界(lower bounds)
    //Sp19_06         // 反変(contravariance)
    //Sp19_07         // オブジェクト非公開データ
    //Sp19_08         // 上限境界(upper bounds)

    // 第20章 抽象メンバー
    //Sp20_01         // 抽象メンバーの弾丸ツアー
    //Sp20_03         // 抽象val
    //Sp20_04         // 抽象var
    //Sp20_05         // 抽象valの初期化
    //Sp20_05_01      // 事前初期化済みフィールド
    //Sp20_05_02      // 遅延評価val ... lazy=遅延 ... valが初めて使われるまで評価をしない。
    //Sp20_06         // 抽象型
    //Sp20_07         // パス依存型
    //Sp20_08         // 構造的サブ型 refinement=改良
    //Sp20_09         // 列挙(enumeration)
    //Sp20_10         // ケーススタディ:通貨計算

    // 第21章 暗黙の型変換とパラメーター
    //Sp21_01         // 暗黙の型変換
    //Sp21_02         // implicitの規則
    //Sp21_03         // 要求された方への暗黙の型変換
    //Sp21_04         // レシーバの変換
    //Sp21_05_01      // 暗黙のパラメータ(1)
    //Sp21_05_02      // 暗黙のパラメータ(2) 複数のパラメーターを持つ暗黙のパラメーターリスト
    //Sp21_05_03      // 暗黙のパラメータ(3) 上限境界を使っている関数
    //Sp21_06         // 可視境界
    //Sp21_07         // 複数の型変換を適用できるとき
    //Sp21_08         // 暗黙の型変換のデバッグ

    // 第22章 リストの実装
    //Sp22_01         // Listクラスの原則
    //Sp22_01_01      // Nilオブジェクト
    //Sp22_01_02      // ::クラス
    //Sp22_01_03      // その他のメソッド
    //Sp22_01_04      // リストの構築
    //Sp22_02         // ListBufferクラス
    //Sp22_03         // Listクラスの実際の中身
    //Sp22_04         // 関数型の見かけ


    // 第23章 for式の再説
    //Sp23_00
    //Sp23_01         // for式
    //Sp23_02         // N女王問題
    //Sp23_03         // for式によるクエリー
    //Sp23_04         // for式の変換
    //Sp23_05         // 逆方向への変換
    //Sp23_06         // forの一般化
    //Sp23_98         // Maybeモナド
    //Sp23_99         // Listモナド

    // 第24章 ScalaコレクションAPI
    //Sp24_02         // コレクションの一貫性
    //Sp24_03         // Traversableトレイト ... Traversable=横断できる、通過できる
    //Sp24_04         // Iterbleトレイト
    //Sp24_05         // シーケンストレイトSeq,IndexedSeq,LinearSeq
    //Sp24_05_01      // バッファー
    //Sp24_06_a       // 集合
    //Sp24_06_b       // 集合(Set)
    //Sp24_06_01      // ソートされた集合 P.328参照
    //Sp24_07_a       // マップ immutable
    //Sp24_07_b       // マップ mutable
    //Sp24_07_c       // マップ mutable - getOrElseUpdate
    //Sp24_08_a       // 同期集合・マップ - 同期マップ
    //Sp24_08_b       // 同期集合・マップ - 同期集合
    //Sp24_09_01      // 具象イミュータブルコレクションクラス - リスト
    //Sp24_09_02      // ストリーム Stream
    //Sp24_09_03      // ベクター Vector
    //Sp24_09_04      // イミュータブルスタック LIFO（後入れ先出し）
    //Sp24_09_05      // イミュータブルキュー FIFO（先入れ先出し）
    //Sp24_09_06      // レンジ（Range:範囲）
    //Sp24_09_07      // ハッシュトライ(HashTrie)
    //Sp24_09_08      // 赤黒木 TreeSet,TreeMap P.328を参照
    //Sp24_09_09      // イミュータブルビットセット (BitSet)
    //Sp24_09_10      // リストマップ
    //Sp24_10_01      // 具象ミュータブルコレクションクラス - 配列バッファー (ArrayBuffer)
    //Sp24_10_02      // リストバッファー (ListBuffer)
    //Sp24_10_03      // 文字列ビルダー (StringBuilder)
    //Sp24_10_04      // （単方向）連結リスト
    //Sp24_10_05      // 双方向連結リスト (DoubleLinkedLIsts)
    //Sp24_10_06      // ミュータブルリスト (MutableList)
    //Sp24_10_07      // キュー (Queue)
    //Sp24_10_08      // 配列シーケンス (ArraySeq)
    //Sp24_10_09      // スタック (Stack)
    //Sp24_10_10      // 配列スタック (ArrayStack)
    //Sp24_10_11      // ハッシュテーブル (HashMap, HashSet)
    //Sp24_10_12      // 弱いハッシュマップ
    //Sp24_10_13      // 並列マップ (ConcurrentMap)
    //Sp24_10_14      // ミュータブルビットセット (BitSet)
    //Sp24_11_a       // 配列 (1)WrappedArray
    //Sp24_11_b       // 配列 (2)ClassManifest
    //Sp24_12         // 文字列
    //Sp24_13         // パフォーマンス特性
    //Sp24_14_a       // 等価性 (1)
    //Sp24_14_b       // 等価性 (2)
    //Sp24_15_a       // ビュー (1)
    //Sp24_15_b       // ビュー (2)パフォーマンス
    //Sp24_15_c       // ビュー (3)ミュータブシーケンス固有の利点
    //Sp24_16_a       // イテレーター (1)
    //Sp24_16_b       // イテレーター (2)
    //Sp24_16_01      // バッファー付きイテレーター
    //Sp24_17_a       // 0からコレクションを作る
    //Sp24_17_b       // シーケンスのファクトリーメソッド
    //Sp24_18         // JavaコレクションとScalaコレクションの相互互換
    //Sp24_19         // Scala 2.7かの移植

    // 第25章 Scalaコレクションのアーキテクチャ
    //Sp25_01         // ビルダー
    //Sp25_02         // 共通演算の括り出し
    //Sp25_03_01a     // 新しいコレクションの統合 - シーケンスの統合 RNA ver.1
    //Sp25_03_01b     // 新しいコレクションの統合 - シーケンスの統合 RNA ver.2
    //Sp25_03_01c     // 新しいコレクションの統合 - シーケンスの統合 RNA ver.Final
    //Sp25_03_02      // 新しい集合とマップの統合

    // 第26章 抽出子
    //Sp26_01         // サンプル：メールアドレスの抽出
    //Sp26_02         // 抽出子
    //Sp26_03         // 変数が1個以下のパターン
    //Sp26_04a        // 可変個の引数をとる抽出子(1)
    //Sp26_04b        // 可変個の引数をとる抽出子(2)
    //Sp26_05         // 抽出子とシーケンスパターン
    //Sp26_06         // 抽出子とケースクラス
    //Sp26_07_01      // 正規表現 - 正規表現の形成
    //Sp26_07_02      // 正規表現 - 正規表現による探索
    //Sp26_07_03      // 正規表現 - 正規表現による抽出

    // 第27章 アノテーション
    //Sp27_01         // アノテーションを使う理由
    //Sp27_02         // アノテーションの構文
    //Sp27_03_01      // 標準アノテーション (1)使うべきでない機能の指定
    //Sp27_03_02_07   // 標準アノテーション(2)その他

    // 第28章 XMLの操作
    //Sp28_01         // 準構造化データ
    //Sp28_02         // XMLの概要
    //Sp28_03         // XMLリテラル
    //Sp28_04         // シリアライゼーション
    //Sp28_05         // XMLの分解
    //Sp28_06         // デシリアライゼーション
    //Sp28_07         // ロードと保存
    //Sp28_08a        // XMLを対象とするパターンマッチ (1)
    //Sp28_08b        // XMLを対象とするパターンマッチ (2)
    //Sp28_08c        // XMLを対象とするパターンマッチ (3)

    // 第29章 オブジェクトを使ったモジュラープログラミング
    //Sp29_02         // 「レシピ」アプリケーション
    //Sp29_03         // 抽象化
    //Sp29_04         // モジュールのトレイトへの分割
    //Sp29_05         // 実行時リンク
    //Sp29_06         // モジュールインスタンスの管理

    // 第30章 オブジェクトの等価性
    //Sp30_01         // Scalaにおける等価性
    //Sp30_02         // 等価メソッドの開発
    //Sp30_02_01_a    // 落とし穴1：誤ったシグネチャーでequalsを定義する (1)
    //Sp30_02_01_b    // 落とし穴1：誤ったシグネチャーでequalsを定義する (2)
    //Sp30_02_02      // 落とし穴2：hashCodeに変更を加えずequalsだけを変更する
    //Sp30_02_03a     // 落とし穴3：ミュータブルなフィールドによってequalsを定義する (1)
    //Sp30_02_03b     // 落とし穴3：ミュータブルなフィールドによってequalsを定義する (2)
    //Sp30_02_04a     // 落とし穴4：数学における同値関係を表すものとしてequalsを定義できていない (1)対称律
    //Sp30_02_04b     // 落とし穴4：数学における同値関係を表すものとしてequalsを定義できていない (2)推移律 - 一般性を上げた対処方法
    //Sp30_02_04c     // 落とし穴4：数学における同値関係を表すものとしてequalsを定義できていない (3)推移律 - 厳密にする対処方法
    //Sp30_02_04d     // 落とし穴4：数学における同値関係を表すものとしてequalsを定義できていない (3)クラス階層の等価関係の定義
    //Sp30_03a        // パラメーター化された型の等価性の定義 (1)
    //Sp30_03b        // パラメーター化された型の等価性の定義 (2)
    //Sp30_04         // equalsとhashCodeのレシピ


    // 第31章 ScalaとJavaの統合
    //Sp31_01_03      // JavaからScalaを使うための注意点 - シングルトンオブジェクト
    //Sp31_02_02      // 投げられた例外
    //Sp31_02_03      // Javaアノテーション
    //Sp31_02_04      // 独自アノテーションの開発
    //Sp31_03a        // 存在型 (1)
    //Sp31_03b        // 存在型 (2)
    //Sp31_04a        // synchronizedの使い方 (1)
    //Sp31_04b        // synchronizedの使い方 (2)

    // 第32章 アクターと並行プログラミング
    //Sp32_02a        // アクターとメッセージ交換 (1)
    //Sp32_02b        // アクターとメッセージ交換 (2)
    //Sp32_02c        // アクターとメッセージ交換 (3)
    //Sp32_03         // ネイティブスレッドをアクターとして扱う
    //Sp32_04a        // スレッドの再利用によるパフォーマンスの向上 (1)
    //Sp32_04b        // スレッドの再利用によるパフォーマンスの向上 (2) loop版&EXIT無し
    //Sp32_05_01      // アクターのコードスタイル - アクターはブロックしてはならない
    //Sp32_05_04      // メッセージを自己完結的にする
    //Sp32_06         // 大規模なサンプル：並列拡散イベントシミュレーション

    // 第33章 パーサーコンビネーター
    //Sp33_01         // サンプル：算術式・パーサーの実行
    //Sp33_03         // 基本正規表現パーサー
    //Sp33_04         // 他のパーサーの使用例：JSON
    //Sp33_05         // パーサーの出力
    //Sp33_06_04      // thisの別名（別名記法）

    // 第34章 GUIプログラミング
    Sp34_01

  /*
    /* ScalaTest
    参考URL: http://d.hatena.ne.jp/seratch2/20110807/1312726957
    */
    import org.scalatest.FunSuite

    class StringFunSuite extends FunSuite {
      test("abc starts with a") {
        assert("abc".startsWith("a"))
      }
    }
    (new StringFunSuite).execute

    // http://d.hatena.ne.jp/a-hisame/20100715/1279183871
    import org.scalatest.Spec
    import org.scalatest.matchers.MustMatchers

    case class Sample(val n: Int) {
      def say = n
      def sqrt = Math.sqrt(n: Double)
    }
    class SampleTest extends Spec with MustMatchers {
      describe("Sampleは") {
        val s = Sample(10)
        it("数値を聞ける") {
          s.say must be (10)
        }
        it("平方根を聞ける") {
          s.sqrt must be (3.1622 plusOrMinus 0.001)
        }
      }
      describe("計算") {
        it("0除算は例外が返る") {
          evaluating{ 10 / 0 } must produce [ArithmeticException]
        }
      }
    }
    (new SampleTest).execute

    // http://blog.8-p.info/2010/5-sbt-scala
    object Hello {
      val greeting = "hello world"
      def main(args: Array[String]): Unit = {
        println(greeting)
      }
    }
    import org.scalatest.FunSuite

    class HelloSuite extends FunSuite {
      test("""traditional "hello world" from K&R""") {
        assert(Hello.greeting === "hello, world")
      }
    }
    (new HelloSuite).execute

  */
  }
}
