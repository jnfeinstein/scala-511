package org.five11

trait Coded {
  val code: String

  override def equals(other: Any) = other match {
    case that: Stop => this.code == that.code
    case _ => false
  }

  override def hashCode = code.hashCode
}
