package com.example.recipe

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/10/29
 * Time: 12:28
 * To change this template use File | Settings | File Templates.
 */

// リスト29.1 単純なFoodエンティティクラス
abstract class Food(val name: String) {
  override def toString = name
}

// リスト29.2 単純なRecipeエンティティクラス
class Recipe(
  val name: String, // 名前
  val ingredients: List[Food],  // 材料リスト
  val instructions: String  // 作り方
) {
  override def toString = name
}

// リスト29.3 テスト用のFoodとReciepeのサンプル
// Food
@deprecated object Apple1a extends Food("Apple1a!")
@deprecated object Orange1a extends Food("Orange1a!")
@deprecated object Cream1a extends Food("Cream1a!")
@deprecated object Sugar1a extends Food("Sugar1a!")

@deprecated object Apple1b extends Food("Apple1b!")
@deprecated object Orange1b extends Food("Orange1b!")
@deprecated object Cream1b extends Food("Cream1b!")
@deprecated object Sugar1b extends Food("Sugar1b!")

//Recipe
@deprecated object FruitSalad1a extends Recipe(
  "fruit salad1a",
  List(Apple1a, Orange1a, Cream1a, Sugar1a),
  "Stir it all together.1a"
)
@deprecated object FruitSalad1b extends Recipe(
  "fruit salad1b",
  List(Apple1a, Orange1a, Cream1b, Sugar1b),
  "Stir it all together.1b"
)
@deprecated object FruitSalad1c extends Recipe(
  "fruit salad1c",
  List(Apple1b, Orange1b, Cream1a, Sugar1a),
  "Stir it all together.1c"
)


/* ##########「レシピ」アプリケーション Ver.1 ########## */

// リスト29.4 モック実装のデータベースモジュール
@deprecated("use `SimpleDatabase2' instead of `SimpleDatabase1'")
object SimpleDatabase1 {
  def allFoods = List(Apple1a, Orange1a, Cream1a, Sugar1a, Apple1b, Orange1b, Cream1b, Sugar1b)
  def foodNamed(name: String): Option[Food] = allFoods.find(_.name == name)
  def allRecipes: List[Recipe] = List(FruitSalad1a, FruitSalad1b, FruitSalad1c)
  // リスト29.5 カテゴリーを追加した後のデータベースモジュール
  case class FoodCategory(name: String, foods:List[Food]){
    override def toString = "CategoryName: " + name + " Foods: " +  foods
  }
  private var categories = List(
    FoodCategory("fruits1a", List(Apple1a, Orange1a)),
    FoodCategory("fruits1b", List(Apple1b, Orange1b)),
    FoodCategory("misc1a", List(Cream1a, Sugar1a)),
    FoodCategory("misc1b", List(Cream1b, Sugar1b))
  )
  def allCategories = categories
}

// リスト29.4 モック実装のブラウザーモジュール
@deprecated("use `SimpleBrowser2' instead of `SimpleBrowser1'")
object SimpleBrowser1 {
  def recipesUsing(food: Food) =
    SimpleDatabase1.allRecipes.filter(_.ingredients.contains(food))
  // リスト29.5 カテゴリーを追加した後のブラウザーモジュール
  def displayCategory(category: SimpleDatabase1.FoodCategory) {
    println(category)
  }
}


/* ##########「レシピ」アプリケーション Ver.2 ########## */

// リスト29.7 抽象データベースのvalを持つデータベースクラス
@deprecated("use `Database2' instead of `Database1'")
abstract class Database1 {
  def allFoods: List[Food]
  def allRecipes: List[Recipe]
  // 具象クラスから移動
  def foodNamed(name: String) = allFoods.find(_.name == name)
  // 具象クラスから移動
  case class FoodCategory(name: String, foods: List[Food]){
    override def toString = "CategoryName: " + name + " Foods: " +  foods
  }
  def allCategories: List[FoodCategory]
}

// リスト29.6 抽象データベースのvalを持つブラウザークラス
@deprecated("use `Browser2' instead of `Browser1'")
abstract class Browser1 {
  val database: Database1
  // 具象クラスから移動 & 抽象データベースを使用
  def recipesUsing(food: Food) =
    database.allRecipes.filter(_.ingredients.contains(food))
  // 具象クラスから移動 & 抽象データベースを使用
  def displayCategory(category: database.FoodCategory) {
    println(category)
  }
}

// リスト29.8 DatabaseのサブクラスとしてのSimpleDatbaseオブジェクト
@deprecated("use `SimpleDatabase3' instead of `SimpleDatabase2'")
object SimpleDatabase2 extends Database1 {
  def allFoods = List(Apple1a, Orange1a, Cream1a, Sugar1a, Apple1b, Orange1b, Cream1b, Sugar1b)
  def allRecipes: List[Recipe] = List(FruitSalad1a, FruitSalad1b, FruitSalad1c)
  private var categories = List(
    FoodCategory("fruits1a", List(Apple1a, Orange1a)),
    FoodCategory("fruits1b", List(Apple1b, Orange1b)),
    FoodCategory("misc1a", List(Cream1a, Sugar1a)),
    FoodCategory("misc1a", List(Cream1b, Sugar1b))
  )
  def allCategories = categories
}

// リスト29.9 BrowserのサブクラスとしてSimpleBrowserオブジェクト
@deprecated("use `SimpleBrowser3' instead of `SimpleBrowser2'")
object SimpleBrowser2 extends Browser1 {
  val database = SimpleDatabase2
}


/* ##########「レシピ」アプリケーション Ver.2' ########## */

// リスト29.10 第2のモックデータベースとブラウザー
@deprecated("use `StudentDatabase2' instead of `StudentDatabase1'")
object StudentDatabase1 extends Database1 {
  object FronzenFood1a extends Food("FrozenFood1a!")
  object FronzenFood1b extends Food("FrozenFood1b!")
  object HeatItUp1 extends Recipe(
    "heat it up 1a",
    List(FronzenFood1a),
    "Microwave the 'food' for 10 minutes.1a"
  )
  object HeatItUp2 extends Recipe(
    "heat it up 1b",
    List(FronzenFood1b),
    "Microwave the 'food' for 10 minutes.1b"
  )
  def allFoods = List(FronzenFood1a, FronzenFood1b)
  def allRecipes = List(HeatItUp1, HeatItUp2)
  def allCategories = List(
    FoodCategory("edible1a", List(FronzenFood1a)),
    FoodCategory("edible1b", List(FronzenFood1b))
  )
}

@deprecated("use `StudentBrowser2' instead of `StudentBrowser1'")
object StudentBrowser1 extends Browser1 {
  val database = StudentDatabase1
}


/* ##########「レシピ」アプリケーション Ver.3 ########## */

// リスト29.11 カテゴリーのトレイト
trait FoodCategories {
  // Database抽象クラスから移動
  case class FoodCategory(name: String, foods: List[Food]){
    override def toString = "CategoryName: " + name + " Foods: " +  foods
  }
  // Database抽象クラスから移動
  def allCategories: List[FoodCategory]
}

// リスト29.12 FoodCategoriesトレイトをミックインするDatabaseクラス
abstract class Database2 extends FoodCategories {
  def allFoods: List[Food]
  def allRecipes: List[Recipe]
  // 具象クラスから移動
  def foodNamed(name: String) = allFoods.find(_.name == name)
}

// リスト29.14 SimpleFoodsトレイト
trait SimpleFoods {
  this: Database2 =>  // 自分型を使いFoodCategoryをコンパイラーに知らせる（オリジナル仕様）
  object Apple2a extends Food("Apple2a!")
  object Orange2a extends Food("Orange2a!")
  object Cream2a extends Food("Cream2a!")
  object Sugar2a extends Food("Sugar2a!")
  object Apple2b extends Food("Apple2b!")
  object Orange2b extends Food("Orange2b!")
  object Sugar2b extends Food("Sugar2b!")
  object Cream2b extends Food("Cream2b!")
  object Pear2a extends Food("Pear2a!")
  object Pear2b extends Food("Pear2b!")
  def allFoods = List(Apple2a, Orange2a, Cream2a, Sugar2a, Apple2b, Orange2b, Cream2b, Sugar2b, Pear2a, Pear2b)
  // オリジナル仕様
  private var categories = List(
    FoodCategory("fruits2a", List(Apple2a, Orange2a)),
    FoodCategory("fruits2b", List(Apple2b, Orange2b)),
    FoodCategory("misc2a", List(Cream2a, Sugar2a)),
    FoodCategory("misc2b", List(Cream2b, Sugar2b)),
    FoodCategory("all_fruits", List(Apple2a, Orange2a, Apple2b, Orange2b, Pear2a, Pear2b))
  )
  def allCategories = categories
  //def allCategories = Nil
}

// リスト29.15 自分型を指定したSimpleRecipesトレイト
trait SimpleRecipes {
  this: SimpleFoods =>  // 自分型(self type)とはクラス内でthisが使われるときにthisの型として想定されているもの
  object FruitSalad2a extends Recipe(
    "fruit salad2a",
    List(Apple2a, Orange2a, Cream2a, Sugar2a),
    "Stir it all together.2a"
  )
  object FruitSalad2b extends Recipe(
    "fruit salad2b",
    List(Apple2a, Orange2a, Cream2b, Sugar2b),
    "Stir it all together.2b"
  )
  object FruitSalad2c extends Recipe(
    "fruit salad2c",
    List(Apple2b, Orange2b, Cream2a, Sugar2a),
    "Stir it all together.2c"
  )
  object FruitSalad2d extends Recipe(
    "fruit salad4",
    List(Apple2a, Orange2a, Cream2a, Sugar2a, Apple2b, Orange2b, Cream2b, Sugar2b, Pear2a, Pear2b),
    "Stir it all together.2d"
  )
  def allRecipes = List(FruitSalad2a, FruitSalad2b, FruitSalad2c, FruitSalad2d)
}

// リスト29.13 ミックスインだけで組み立てられているSimpleDatabaseオブジェクト
object SimpleDatabase3 extends Database2 with SimpleFoods with SimpleRecipes {
  /*
  def allFoods = List(Apple1a, Orange1a, Cream1a, Sugar1a, Apple1b, Orange1b, Cream1b, Sugar1b)
  def allRecipes: List[Recipe] = List(FruitSalad1a, FruitSalad1b, FruitSalad1c)
  private var categories = List(
    FoodCategory("fruits1", List(Apple1a, Orange1a)),
    FoodCategory("fruits2", List(Apple1b, Orange1b)),
    FoodCategory("misc1", List(Cream1a, Sugar1a)),
    FoodCategory("misc2", List(Cream1b, Sugar1b))
  )
  def allCategories = categories
  */
}

abstract class Browser2 {
  val database: Database2
  // 具象クラスから移動 & 抽象データベースを使用
  def recipesUsing(food: Food) =
    database.allRecipes.filter(_.ingredients.contains(food))
  // 具象クラスから移動 & 抽象データベースを使用
  def displayCategory(category: database.FoodCategory) {
    println(category)
  }
}

object SimpleBrowser3 extends Browser2 {
  val database = SimpleDatabase3
}

/* ##########「レシピ」アプリケーション Ver.3' ########## */

trait StudentFoods {
  this: Database2 =>  // 自分型を使いFoodCategoryをコンパイラーに知らせる（オリジナル仕様）
  object FronzenFood2a extends Food("FrozenFood2a!")
  object FronzenFood2b extends Food("FrozenFood2b!")
  def allFoods = List(FronzenFood2a, FronzenFood2b)
  // オリジナル仕様
  private var categories = List(
    FoodCategory("edible2a", List(FronzenFood2a)),
    FoodCategory("edible2b", List(FronzenFood2b))
  )
  def allCategories = categories
  //def allCategories = Nil
}

trait StudentRecipes {
  this: StudentFoods =>  // 自分型(self type)とはクラス内でthisが使われるときにthisの型として想定されているもの
  object HeatItUp1 extends Recipe(
    "heat it up 2a",
    List(FronzenFood2a),
    "Microwave the 'food' for 10 minutes.2a"
  )
  object HeatItUp2 extends Recipe(
    "heat it up 2b",
    List(FronzenFood2b),
    "Microwave the 'food' for 10 minutes.2b"
  )
  def allRecipes = List(HeatItUp1, HeatItUp2)
}

object StudentDatabase2 extends Database2 with StudentFoods with StudentRecipes

object StudentBrowser2 extends Browser2 {
  val database = StudentDatabase2
}
