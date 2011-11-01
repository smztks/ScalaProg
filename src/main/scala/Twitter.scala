
/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/10/28
 * Time: 22:06
 * To change this template use File | Settings | File Templates.
 */

object Twitter extends Application {

  import twitter4j.Status
  import twitter4j.TwitterStream
  import twitter4j.TwitterStreamFactory
  import twitter4j.UserStreamAdapter
  import twitter4j.conf.Configuration
  import twitter4j.conf.ConfigurationBuilder

  val tsf = new TwitterStreamFactory
  println(tsf)
  //TwitterStream
  
}