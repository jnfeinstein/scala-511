/*
import org.five11._

object Main {
  implicit val token: Api.token = System.getenv("TOKEN")

  val api = new Api

  def main(args: Array[String]): Unit = {

    val caltrain = api.agencies.find{ _.name.toLowerCase == "caltrain" }.get

    caltrain.routes.map{ r: Route =>
      r.directions.map{ d: Direction =>
        d.stops.map{ s: Stop =>
          println(s.departures)
        }
      }
    }
  }
}
*/
