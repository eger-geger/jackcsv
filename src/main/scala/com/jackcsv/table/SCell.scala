package com.jackcsv.table

class SCell protected (private var _value: Any) {

  def +(other: SCell): SCell = new SCell(value) { this += other }

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

  implicit def anyToSCell(any:Any):SCell = SCell(any)

  def apply(value: Any): SCell = {
    value match {
      case cell: SCell => cell
      case null => NullSCell
      case o: Option[Any] => SCell(o.getOrElse(null))
      case v => new SCell(v)
    }
  }
}

object NullSCell extends SCell(null) {
  override def toString = "undefined"

  override def +=(other: SCell) {}

  override def +(other: SCell): SCell = other
}