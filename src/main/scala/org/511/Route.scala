package org.five11

import scala.xml.XML
import scalaj.http._

class Route(val agency: Agency, raw: xml.Node) {
  val name = (raw \ "@Name").text
  val code = (raw \ "@Code").text
  val directions = (raw \\ "RouteDirectionList" \\ "RouteDirection").map{ new Direction(this, _) }

  def stops(implicit token: Api.token) = {
    if (directions.isEmpty) {
      val routeIdf = Array(agency.name, code).mkString("~")
      val req = Stop.req.param("token", token).param("routeIDF", routeIdf).asString

      val body = XML.loadString(req.body)
      (body \\ "AgencyList" \\ "Agency" \\ "RouteList" \\ "Route" \\ "StopList" \\ "Stop").map( new Stop(this, _) )
    } else {
      directions.map{ _.stops }.flatten
    }
  }

  override def toString = name
}

object Route {
  val part = "GetRoutesForAgency.aspx"
  val req = Http(Api.url + part)
}
