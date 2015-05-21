package org.five11

class Direction(
    val name: String,
    val code: String,
    val agency: Agency,
    getRoutes: => Seq[Route])
  extends Coded {

  lazy val routes = getRoutes

  lazy val stops = agency.stops.filter{ stop =>
    stop.directions.contains(this)
  }

  override def toString = Array(name, code).mkString(":")
}
