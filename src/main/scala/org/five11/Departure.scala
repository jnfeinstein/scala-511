package org.five11

class Departure(
    val time: Int,
    val stop: Stop,
    val route: Route,
    val direction: Direction) {
  def this(time: Int, stop: Stop, route: Route) = this(time, stop, route, null)
  override def toString = Array(route, direction, stop, time).mkString(" / ")
}
