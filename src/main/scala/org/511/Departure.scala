package org.five11

import scalaj.http._

class Departure(val stop: Stop, raw: xml.Node) {
  val time = raw.text.toInt

  def this(raw: xml.Node) = this(null, raw)

  override def toString = time.toString
}

object Departure {
  val part = "GetNextDeparturesByStopName.aspx"
  val req = Http(Api.url + part)
}
