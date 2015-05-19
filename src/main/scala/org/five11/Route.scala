package org.five11

class Route(
    val name: String,
    val code: String,
    val agency: Agency)
  (implicit api: Api)
  extends Coded {
  lazy val directions = agency.directions.filter{ direction =>
    direction.routes.contains(this)
  }
  lazy val stops = agency.stops.filter{ stop =>
    stop.routes.contains(this)
  }
  override def toString = Array(name, code).mkString(":")
}
