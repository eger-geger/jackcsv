package org.jackcsv.table

import scala.collection.mutable.ListBuffer

class NTable(private val _name: String, private val _rows: Seq[Seq[Cell]], val keyIndex: Int)
  extends STable(_name, NTable.normalize(_rows, keyIndex)) {

  def this(simple: STable, keyIndex: Int) = this(simple.name, simple.rows, keyIndex)

  def +(other: NTable): NTable = {
    val rows = new ListBuffer[CellSeq]
    rows ++= this.rows.map(new CellSeq(_))
    rows ++= other.compatible(this)

    new NTable(name, rows, keyIndex)
  }

  def compatible(other: NTable): Seq[CellSeq] = {
    val leftShift = other.columnCount
    val rightShift = leftShift - 1
    val lastIndex = columnCount - 1
    val firstIndex = 0

    val pattern = new ListBuffer[(Int, Int)]
    pattern += Tuple2(keyIndex, other.keyIndex)
    pattern ++= firstIndex until keyIndex by 1 map (i => (i, i + leftShift))
    pattern ++= lastIndex until keyIndex by -1 map (i => (i, i + rightShift))

    rows.map(CellSeq(_).transform(pattern))
  }
}

object NTable {

  implicit def apply(tuple: (STable, Int)): NTable =
    new NTable(tuple._1, tuple._2)

  private def normalize(rows: Seq[Seq[Cell]], keyIndex: Int): Seq[Seq[Cell]] = {
    val size = rows.map(_.size).max
    val emptyCells = List.fill(_: Int)(EmptyCell)
    val buffer = new ListBuffer[CellSeq]

    for (r <- rows; row = r.toList ::: emptyCells(size - r.size)) {
      val sameKeyRow = buffer.find(_(keyIndex) match {
        case EmptyCell => false

        case cell:Cell =>
          cell equals row(keyIndex)

        case _ => false
      })

      if (sameKeyRow.isDefined) {
        sameKeyRow.get += row
      } else {
        buffer += CellSeq(row)
      }
    }

    buffer
  }
}