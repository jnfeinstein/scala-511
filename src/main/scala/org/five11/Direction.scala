package org.five11

class Direction(
    val name: String,
    val code: String,
    val agency: Agency,
    getRoutes: => Seq[Route]) {
  lazy val routes = getRoutes
  override def toString = name
}
