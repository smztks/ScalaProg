
/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/11/03
 * Time: 15:40
 * To change this template use File | Settings | File Templates.
 */

//import scala.swing._
//import scala.swing.event._
import swing._
import event._

object FirstSwingApp extends SimpleSwingApplication {

  // 34.1 最初に作るSwingアプリケーション
  def top_34_1 = new MainFrame {
    title = "First Swing App"
    contents = new Button {
      text = "Click me"
    }
  }

  // 34.2 パネルとレイアウト
  def top_34_2 = new MainFrame {
    title = "Second Swing App"
    val button = new Button {
      text = "Click me"
    }
    val label = new  Label {
      text = "No button clicks registered"
    }
    contents = new BoxPanel(Orientation.Vertical) {
      contents += button
      contents += label
      border = Swing.EmptyBorder(30, 30, 10, 30)
    }
  }

  // 34.3 イベント処理
  def top_34_3 = new MainFrame {
    title = "Second Swing App"
    val button1 = new Button {
      text = "Click me1"
    }
    val button2 = new Button {
      text = "Click me2"
    }
    val label1 = new  Label {
      text = "No button clicks registered1"
    }
    val label2 = new  Label {
      text = "No button clicks registered2"
    }
    contents = new BoxPanel(Orientation.Vertical) {
    //contents = new FlowPanel {
      contents += button1
      contents += button2
      contents += label1
      contents += label2
      border = Swing.EmptyBorder(10, 30, 10, 90)
    }
    listenTo(button1)
    listenTo(button2)
    var nClicks1, nClicks2 = 0
    reactions += {
      case ButtonClicked(b) =>
        (b.text: @unchecked) match {
          case "Click me1" =>
            nClicks1 += 1
            label1.text = "Number of button clicks: " + nClicks1
          case "Click me2" =>
            nClicks2 += 1
            label2.text = "Number of button clicks: " + nClicks2
        }
    }
  }

  // 34.3 イベント処理（改良版）
  def top = new MainFrame {
    title = "Second Swing App"
    object button1 extends Button { text = "Click me1" }
    object button2 extends Button { text = "Click me2" }
    object label1 extends Label { text = "No button clicks registered1" }
    object label2 extends Label { text = "No button clicks registered2" }

    contents = new BoxPanel(Orientation.Vertical) {
    //contents = new FlowPanel {
      contents += button1 += button2 += label1 += label2
      border = Swing.EmptyBorder(10, 30, 10, 90)
    }
    listenTo(button1, button2)
    var nClicks1, nClicks2 = 0
    reactions += {
      case ButtonClicked(`button1`) =>
          nClicks1 += 1
          label1.text = "Number of button clicks: " + nClicks1
      case ButtonClicked(`button2`) =>
          nClicks2 += 1
          label2.text = "Number of button clicks: " + nClicks2
    }
  }

  // 34.4 サンプル：摂氏・華氏換算プログラム
  def top_34_4 = new MainFrame {
    title = "Celsius/Fahrenheit Converter"
    object celsius extends TextField { columns = 5 }
    object fahrenheit extends TextField { columns = 5 }
    contents = new FlowPanel {
      contents += celsius
      contents += new Label(" Celsius  =  ")
      contents += fahrenheit
      contents += new Label(" Fahrenheit")
      border = Swing.EmptyBorder(15, 10, 10, 10)
    }
    listenTo(celsius, fahrenheit)
    reactions += {
      case EditDone(`fahrenheit`) =>
        val f = fahrenheit.text.toInt
        val c = (f - 32) * 5 / 9
        celsius.text = c.toString
      case EditDone(`celsius`) =>
        val c = celsius.text.toInt
        val f = c * 9 / 5 + 32
        fahrenheit.text = f.toString
    }
  }
  
  println("end of: " + Thread.currentThread.getStackTrace()(1))
}
