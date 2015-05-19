package org.five11

import scala.xml.XML
import scalaj.http._

class Api(token: String) {

  private val endpoints = new Endpoints(token)

  def agencies(): Seq[Agency] = {
    val req = endpoints.agencies.asString

    val body = XML.loadString(req.body)
    (body \\ "Agency").map{ agencyNode =>
      val name = (agencyNode \ "@Name").text
      val hasDirecton = (agencyNode \ "@HasDirecton").text == "True"
      val mode = (agencyNode \ "@Mode").text

      new Agency(name, mode, hasDirecton)(this)
    }
  }

  def routes(agency: Agency): Seq[Route] = {
    val req = endpoints.routesForAgency.param("agencyName", agency.name).asString

    val body = XML.loadString(req.body)
    (body \\ "Route").map{ routeNode =>
      val name = (routeNode \ "@Name").text
      val code = (routeNode \ "@Code").text

      new Route(name, code, agency)(this)
    }
  }

  def directions(agency: Agency): Seq[Direction] = {
    val req = endpoints.routesForAgency.param("agencyName", agency.name).asString

    val body = XML.loadString(req.body)

    (body \\ "Route").map{ routeNode =>
      (routeNode \\ "RouteDirection").map{ directionNode => (routeNode, directionNode) }
    }.
    flatten.
    groupBy{ case (routeNode, directionNode) => directionNode }.
    map{ case(directionNode, tuples) =>
      val name = (directionNode \ "@Name").text
      val code = (directionNode \ "@Code").text

      val routeCodes = tuples.map{ case (routeNode, directionNode) =>
        (routeNode \ "@Code").text
      }

      def getRoutes = agency.routes.filter{ route =>
        routeCodes.contains(route.code)
      }

      new Direction(name, code, agency, getRoutes)
    }.
    to[Seq]
  }

  def stops(agency: Agency): Seq[Stop] = {
    agency.routes.map{ route =>
      if (route.directions.isEmpty) {
        val idf = routeIDF(route)
        val req = endpoints.stopsForRoute.param("routeIDF", idf).asString

        val body = XML.loadString(req.body)
        (body \\ "Stop").map{ stopNode => (route, null, stopNode) }
      } else {
        route.directions.map{ direction =>
          val idf = routeIDF(route, direction)
          val req = endpoints.stopsForRoute.param("routeIDF", idf).asString

          val body = XML.loadString(req.body)
          (body \\ "Stop").map{ stopNode => (route, direction, stopNode) }
        }.flatten
      }
    }.
    flatten.
    groupBy{ case (route, direction, stopNode) => stopNode }.
    map{ case (stopNode, tuples) =>
      val name = (stopNode \ "@name").text
      val code = (stopNode \ "@StopCode").text

      val routes = tuples.map{ case (route, direction, stopNode) =>
        route
      }.distinct

      val directions = tuples.map{ case (route, direction, stopNode) =>
        direction
      }.distinct

      new Stop(name, code, agency, routes, directions)(this)
    }.
    to[Seq]
  }

  def departures(stop: Stop): Seq[Departure] = {
    val req = endpoints.departuresForStop.param("stopcode", stop.code).asString

    val body = XML.loadString(req.body)
    (body \\ "Route").map{ routeNode =>
      val route = stop.routes.find{ (routeNode \ "@Code").text == _.code }

      if (!route.isDefined) {
        Seq()
      } else if (route.get.directions.isEmpty) {
        (routeNode \\ "DepartureTime").map{ departureNode =>
          new Departure(departureNode.text.toInt, stop, route.get)
        }
      } else {
        (routeNode \\ "RouteDirection").map{ directionNode =>
          val direction = route.get.directions.find{ (directionNode \ "@Code").text == _.code }

          if (!direction.isDefined) {
            Seq()
          } else {
            (directionNode \\ "DepartureTime").map{ departureNode =>
              new Departure(departureNode.text.toInt, stop, route.get, direction.get)
            }
          }
        }.flatten
      }
    }.
    flatten
  }

  private def routeIDF(route: Route, direction: Direction): String = {
    Array(route.agency.name, route.code, direction.code).mkString("~")
  }

  private def routeIDF(route: Route): String = {
    Array(route.agency.name, route.code).mkString("~")
  }
}

sealed class Endpoints(token: String) {
  private val url = "http://services.my511.org/Transit2.0/"
  val agencies = Http(url + "GetAgencies.aspx").param("token", token)
  val routesForAgency = Http(url + "GetRoutesForAgency.aspx").param("token", token)
  val stopsForRoute = Http(url + "GetStopsForRoutes.aspx").param("token", token)
  val departuresForStop = Http(url + "GetNextDeparturesByStopCode.aspx").param("token", token)
}
