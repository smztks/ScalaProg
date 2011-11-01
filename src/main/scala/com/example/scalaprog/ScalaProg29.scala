package com.example.scalaprog

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/10/28
 * Time: 17:19
 * To change this template use File | Settings | File Templates.
 */

// 第29章 オブジェクトを使ったモジュラープログラミング
object ScalaProg29

// 「レシピ」アプリケーション
object Sp29_02 {

  import com.example.recipe._

  // for SimpleDatabase1, SimpleBrowser1
  val apple = SimpleDatabase1.foodNamed("Apple1a!").get
  println(apple)
  
  val recipe = SimpleBrowser1.recipesUsing(apple)
  println(recipe)

  val allCategories = SimpleDatabase1.allCategories
  println(allCategories)

  val category = SimpleDatabase1.FoodCategory("fruitsMix",
    List(Apple1a, Orange1a, Apple1b, Orange1b))
  SimpleBrowser1.displayCategory(category)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}
// 抽象化
object Sp29_03 {

  import com.example.recipe._

  // for SimpleDatabase2, SimpleBrowser2
  val apple = SimpleDatabase2.foodNamed("Apple1a!").get
  println(apple)

  val recipe1 = SimpleBrowser2.recipesUsing(apple)
  println(recipe1)

  val allCategories1 = SimpleDatabase2.allCategories
  println(allCategories1)

  val category1 = SimpleDatabase2.FoodCategory("fruitsMix",
    List(Apple1a, Orange1a, Apple1b, Orange1b))
  SimpleBrowser2.displayCategory(category1)


  // for StudentDatabase1, SimpleBrowser1
  val frozenfood = StudentDatabase1.foodNamed("FrozenFood1!").get
  println(frozenfood)

  val recipe2 = StudentBrowser1.recipesUsing(frozenfood)
  println(recipe2)

  val allCategories2 = StudentDatabase1.allCategories
  println(allCategories2)

  val category2 = StudentDatabase1.FoodCategory("fronzenMix",
    List(StudentDatabase1.FronzenFood1a, StudentDatabase1.FronzenFood1b))
  StudentBrowser1.displayCategory(category2)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// モジュールのトレイトへの分割
object Sp29_04 {

  import com.example.recipe._

  // for SimpleDatabase3, SimpleBrowser3
  val apple = SimpleDatabase3.foodNamed("Apple2a!").get
  println(apple)

  val recipe1 = SimpleBrowser3.recipesUsing(apple)
  println(recipe1)

  val allCategories1 = SimpleDatabase3.allCategories
  println(allCategories1)

  val category1 = SimpleDatabase3.FoodCategory(
    "fruitsMix",
    List(
      SimpleDatabase3.Apple2a,
      SimpleDatabase3.Orange2a,
      SimpleDatabase3.Apple2b,
      SimpleDatabase3.Orange2b,
      SimpleDatabase3.Pear2a,
      SimpleDatabase3.Pear2b
    )
  )
  SimpleBrowser3.displayCategory(category1)

  // for StudentDatabase2, Browser2
  val frozenfood = StudentDatabase2.foodNamed("FrozenFood1!").get
  println(frozenfood)

  val recipe2 = StudentBrowser2.recipesUsing(frozenfood)
  println(recipe2)

  val allCategories2 = StudentDatabase2.allCategories
  println(allCategories2)

  val category2 = StudentDatabase2.FoodCategory(
    "fronzenMix",
    List(
      StudentDatabase2.FronzenFood2a,
      StudentDatabase2.FronzenFood2b
    )
  )
  StudentBrowser2.displayCategory(category2)

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// 実行時リンク
object Sp29_05 {

  import com.example.recipe._

  def gotFoods(args: Array[String]) {
    val db: Database2 =
      if(args(0) == "students")
        StudentDatabase2
      else
        SimpleDatabase3
    object browser extends Browser2 {
      val database = db
    }
    println("Database: " + args(0))
    val food = SimpleDatabase3.foodNamed(args(1)).get // student+Apple1!のパラメーターでは動作しない。
    for (recipe <- browser.recipesUsing(food))
      println("Recipe: " + recipe)
  }

  gotFoods(Array[String]("students", "Apple2a!"))  // 見つからない ...
  gotFoods(Array[String]("else", "Apple2a!"))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

// モジュールインスタンスの管理
object Sp29_06 {

  import com.example.recipe._

  val category = StudentDatabase2.allCategories.head
  StudentBrowser2.displayCategory(category)
  // CategoryName: edible1 Foods: List(FrozenFood1!)

  // SimpleBrowser3.displayCategory(category)
  // Error: type mismatch;

  def gotFoods(args: Array[String]) {
    val db: Database2 =
      if(args(0) == "students")
        StudentDatabase2
      else
        SimpleDatabase3
    object browser extends Browser2 {
      //val database = db
      val database: db.type = db
    }
    println("Database: " + args(0))
    for(category <- db.allCategories)
      browser.displayCategory(category)
      // Error: type mismatch; ... val database: db.type = dbのシングルトン型定義で回避
    val food = db.foodNamed(args(1)).get // student+Apple1!のパラメーターでは動作しない。
    for (recipe <- browser.recipesUsing(food))
      println("Recipe: " + recipe)
  }

  gotFoods(Array[String]("students", "FrozenFood1!"))
  //gotFoods(Array[String]("students", "Apple1!"))  // 「db.foodNamed」とした場合、見つからずエラーとなる。
  gotFoods(Array[String]("else", "Apple2a!"))

  println("end of: " + Thread.currentThread.getStackTrace()(1))
}

