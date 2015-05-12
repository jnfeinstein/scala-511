# scala-511
Scala interface for 511.org real time transit API

```scala
  import org.five11._
  // Specify your API token as an implicit value
  implicit val token: Api.token = "YOUR_API_TOKEN"
  // Create a new api
  val api = new Api
  // The API exposes agencies like CalTrain, BART, SF Muni...
  api.agencies.foreach{ a: Agency =>
    // Every agency has routes
    a.routes.foreach{ r: Route =>
      // Every route has stops
      r.stops.foreach{ s: Stop =>
        // Every stop has upcoming departures
        s.departures.foreach{ d: Departure =>
          println(r + "is departing from " + s + " in " + d + " minutes")
        }
      }
      // Some routes also have directions
      r.directions.foreach{ dir: Direction =>
        // Directions also have stops
        dir.stops.foreach{ s: Stop =>
          s.departures.foreach{ d: Departure =>
            println(r + "is departing from " + s + " in " + d + " minutes going " + dir)
          }
        }
      }
    }
  }
```