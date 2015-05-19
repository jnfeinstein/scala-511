package org.five11

class Agency(
    val name: String,
    val mode: String,
    val hasDirecton: Boolean)
  (implicit api: Api) {
  lazy val routes = api.routes(this)
  lazy val directions = api.directions(this)
  lazy val stops = api.stops(this)
  override def toString = name
}
