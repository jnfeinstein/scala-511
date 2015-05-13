package org.five11

import scala.xml.XML

class Api {

  def agencies(implicit token: Api.token) = {
    val req = Agency.req.param("token", token).asString

    val body = XML.loadString(req.body)
    (body \\ "AgencyList" \\ "Agency").map{ new Agency(_) }
  }
}

object Api {
  type token = String

  val url = "http://services.my511.org/Transit2.0/"
}
