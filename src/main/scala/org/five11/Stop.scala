package org.five11

import scala.xml.XML
import scalaj.http._

class Stop(val route: Route, val direction: Direction, raw: xml.Node) {
  val name = (raw \ "@name").text
  val code = (raw \ "@StopCode").text

  def this(route: Route, raw: xml.Node) = this(route, null, raw)

  def departures(implicit token: Api.token) = {
    val req = Departure.req.param("token", token).
      param("agencyName", route.agency.name).
      param("stopName", name).asString

    val body = XML.loadString(req.body)
    (body \\ "AgencyList" \\ "Agency" \\ "RouteList" \\ "Route" \\
      "StopList" \\ "Stop" \\
        "DepartureTimeList" \\ "DepartureTime").map{ new Departure(this, _) }.to[Vector]
  }

  override def toString = name
}

object Stop {
  val part = "GetStopsForRoute.aspx"
  val req = Http(Api.url + part)
}
