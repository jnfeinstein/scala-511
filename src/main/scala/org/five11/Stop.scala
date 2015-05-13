package org.five11

import scala.xml.XML
import scalaj.http._

class Stop(val route: Route, val direction: Direction, raw: xml.Node) {
  val name = (raw \ "@name").text
  val code = (raw \ "@StopCode").text

  def this(route: Route, raw: xml.Node) = this(route, null, raw)

  def departures(implicit token: Api.token): Seq[Departure] = {
    val req = Departure.req.param("token", token).
      param("stopcode", code).asString

    val body = XML.loadString(req.body)

    val routeNode = (body \\ "AgencyList" \\ "Agency" \\ "RouteList" \\ "Route").
      find{ node => (node \ "@Code").text == route.code }

    if ( !routeNode.isDefined ) {
      return Seq()
    }

    val directionNode = if (direction == null) {
      routeNode
    } else {
      (routeNode.get \\ "RouteDirectionList" \\ "RouteDirection").
        find{ node => (node \ "@Code").text == direction.code }
    }

    if ( !directionNode.isDefined) {
      return Seq()
    }

    (directionNode.get \\ "StopList" \\ "Stop" \\
      "DepartureTimeList" \\ "DepartureTime").map{ new Departure(this, _) }
  }

  override def toString = name
}

object Stop {
  val part = "GetStopsForRoute.aspx"
  val req = Http(Api.url + part)
}
