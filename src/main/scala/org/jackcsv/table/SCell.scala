package com.jackcsv.table

class SCell protected (private var _value: Any) {

  def +(other: SCell): SCell = { val c = SCell(value); c += other; c }

  def +=(other: SCell): Unit = {
    _value = (value, other.value) match {
      case (v, null) => v
      case (null, v) => v
      case (v1, v2) if v1.equals(v2) => v1
      case (v1: Number, v2: Number) => v1.floatValue() + v2.floatValue()
      case (v1, v2) => s"$v1, $v2"
    }
  }

  def value = _value

  override def toString = value.toString

  override def equals(obj: scala.Any): Boolean =
    obj match {
      case cell:SCell => cell.value equals value
      case _ => false
    }
}

object SCell {

  implicit def apply(value: Any): SCell = {
    value match {
      case cell: SCell => cell
      case null => EmptyCell
      case o: Option[Any] => SCell(o.getOrElse(null))
      case v => new SCell(v)
    }
  }
}

object EmptyCell extends SCell(null) {

  override def toString = "\u2205"

  override def +=(other: SCell) {}

  override def +(other: SCell): SCell = other

}