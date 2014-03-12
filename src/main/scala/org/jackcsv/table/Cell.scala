package org.jackcsv.table

class Cell protected (private var _value: Any) {

  def +(other: Cell): Cell = { val c = Cell(value); c += other; c }

  def +=(other: Cell): Unit = {
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
      case cell:Cell => cell.value equals value
      case _ => false
    }
}

object Cell {

  implicit def apply(value: Any): Cell = {
    value match {
      case cell: Cell => cell
      case null => EmptyCell
      case o: Option[Any] => Cell(o.getOrElse(null))
      case v => new Cell(v)
    }
  }
}

object EmptyCell extends Cell(new Object) {

  override def toString = "\u2205"

  override def +=(other: Cell) {}

  override def +(other: Cell): Cell = other

}