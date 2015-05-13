package org.five11

import scala.xml.XML
import scalaj.http._

class Direction(val route: Route, raw: xml.Node) {
  val name = (raw \ "@Name").text
  val code = (raw \ "@Code").text

  def stops(implicit token: Api.token) = {
    val routeIdf = Array(route.agency.name, route.code, code).mkString("~")
    val req = Stop.req.param("token", token).param("routeIDF", routeIdf).asString

    val body = XML.loadString(req.body)
    (body \\ "AgencyList" \\ "Agency" \\ "RouteList" \\ "Route" \\
      "StopList" \\ "Stop").map{ new Stop(route, this, _) }
  }

  override def toString = name
}
