package org.five11

class Direction(
    val name: String,
    val code: String,
    val agency: Agency,
    getRoutes: => Seq[Route])
  extends Coded {
  lazy val routes = getRoutes
  override def toString = Array(name, code).mkString(":")
}
