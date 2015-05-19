/*
import org.five11._

object Main {
  val token = System.getenv("TOKEN")

  val api = new Api(token)

  def main(args: Array[String]): Unit = {

    val caltrain = api.agencies.find{ _.name.toLowerCase == "caltrain" }.get

    caltrain.stops.map{ s: Stop =>
      s.departures.foreach{ d: Departure =>
        println( Array(d.route.name, d.direction.name, d.stop.name, d.time).mkString(" / ") )
      }
    }
  }
}
*/
