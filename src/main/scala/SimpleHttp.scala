
/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/10/26
 * Time: 8:52
 * To change this template use File | Settings | File Templates.
 */

// cf. http://d.hatena.ne.jp/syttru/20090312/1236877047

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.BasicResponseHandler
import org.apache.http.impl.client.DefaultHttpClient
import java.io.{BufferedReader,InputStreamReader}

object SimpleHttp {
  def main(args: Array[String]): Unit = {
    class HttpClientTest {
        def testGet() = {
          val client = new DefaultHttpClient
          val get = new HttpGet("http://www.google.co.jp")

          val response = client.execute(get)
          val entity = response.getEntity

          val input = new BufferedReader(new InputStreamReader(entity.getContent))
          var line = ""
          while({line=input.readLine; line != null}) {
            println(line)
          }
          client.getConnectionManager.shutdown
        }
    }

    val hct = new HttpClientTest
    hct.testGet

    // Benchmark
    def testRun {
      println("testRun")
      Thread.sleep(100)
    }

    import scala.testing.Benchmark
    val time = (new Benchmark { multiplier = 1; def run = testRun } runBenchmark(10))
    println(time)
    // cf. http://kaitenn.blogspot.com/2010/11/scala-viewforcescalatestingbenchmark.html
    // http://www.ne.jp/asahi/hishidama/home/tech/scala/benchmark.html
  }
}
