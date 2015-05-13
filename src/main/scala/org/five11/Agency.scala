package org.five11

import scala.xml.XML
import scalaj.http._

class Agency(raw: xml.Node) {
  val name = (raw \ "@Name").text
  val hasDirecton = (raw \ "@HasDirecton").text
  val mode = (raw \ "@Mode").text

  def routes(implicit token: Api.token) = {
    val req = Route.req.param("token", token).
      param("agencyName", name).asString

    val body = XML.loadString(req.body)
    (body \\ "AgencyList" \\ "Agency" \\
      "RouteList" \\ "Route").map{ new Route(this, _) }.to[Vector]
  }

  override def toString = name
}

object Agency {
  val part = "GetAgencies.aspx"
  val req = Http(Api.url + part)
}
