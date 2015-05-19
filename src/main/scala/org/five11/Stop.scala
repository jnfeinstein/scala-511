package org.five11

class Stop(
    val name: String,
    val code: String,
    val agency: Agency,
    val routes: Seq[Route],
    val directions: Seq[Direction])
  (implicit api: Api)
  extends Coded {
  def departures = api.departures(this)
  override def toString = Array(name, code).mkString(":")
}
