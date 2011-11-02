
/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/11/02
 * Time: 19:27
 * To change this template use File | Settings | File Templates.
 */

import akka.actor.Actor

class MyActor extends Actor {
  def receive = {
    case "test" => println("received test")
    case _ =>      println("received unknown message")
  }
}

object MyAkka {
  def main(args: Array[String]): Unit = {
    val myActor = Actor.actorOf[MyActor]
    myActor.start()
    myActor ! "test"
  }
}
