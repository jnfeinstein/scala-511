# scala-511
Scala interface for 511.org real time transit API

```scala
  import org.five11._
  // Specify your API token as an implicit value
  implicit val token: Api.token = "YOUR_API_TOKEN"
  // Create a new api
  val api = new Api
  // The API exposes agencies (CALTRAIN, BART, SF-MUNI, ...)
  api.agencies.foreach{ a: Agency =>
    // Every agency has routes (LOCAL, BABY_BULLET, ...)
    a.routes.foreach{ r: Route =>
      // Every route has stops (Millbrae Caltrain Station, San Bruno Caltrain Station, ...)
      r.stops.foreach{ s: Stop =>
        // Every stop has upcoming departures (10, 35, 64, ...)
        s.departures.foreach{ d: Departure =>
          println(r + " " + a + " is departing from " + s + " in " + d + " minutes")
          /* LOCAL CALTRAIN is departing from Millbrae Caltrain Station in 10 minutes */
        }
      }
      // Some routes also have directions (TO SAN FRANCISCO, TO SAN JOSE)
      r.directions.foreach{ dir: Direction =>
        // Directions also have stops
        dir.stops.foreach{ s: Stop =>
          s.departures.foreach{ d: Departure =>
            println(r + " " + a + " is departing from " + s + " in " + d + " minutes " + dir)
            /* LOCAL CALTRAIN is departing from Millbrae Caltrain Station in 10 minutes TO SAN FRANCISCO */
          }
        }
      }
    }
  }
```