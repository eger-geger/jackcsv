package org.jackcsv.table

import scala.collection.mutable.ListBuffer

class CellSeq(private val _cells: Traversable[Cell]) extends Seq[Cell] {

  val cells = new ListBuffer[Cell] ++= _cells

  def length: Int = cells.length

  def apply(idx: Int): Cell = cells.applyOrElse[Int, Cell](idx, i => EmptyCell)

  def iterator: Iterator[Cell] = cells.iterator

  def +(cellSeq: Seq[Cell]): CellSeq = new CellSeq(cells) { this += cellSeq }

  def +=(cellSeq: Seq[Cell]) {
    for (i <- 0 until cellSeq.size) {
      if (cells.isDefinedAt(i)) {
        cells.update(i, cells(i) + cellSeq(i))
      } else {
        cells += cellSeq(i)
      }
    }
  }

  def transform(pattern: Seq[(Int, Int)]): CellSeq = {
    val newSize = pattern.maxBy(_._2)._2 + 1

    val buffer = new ListBuffer[Cell] ++= List.fill(newSize)(EmptyCell)

    for (p <- pattern) {
      buffer.update(p._2, this(p._1))
    }

    new CellSeq(buffer)
  }

}

object CellSeq {

  implicit def apply(seq:Traversable[Any]):CellSeq = new CellSeq(seq.map(Cell(_)))

  def normalize(rows: Seq[Seq[Cell]])(keyIndex: Int): Seq[CellSeq] = {
    val buffer = new ListBuffer[CellSeq]

    val size = rows.map(_.size).max

    val emptyCells = List.fill(_:Int)(EmptyCell)

    for (r <- rows; row = r.toList ::: emptyCells(size - r.size)) {
      val sameKeyRow = buffer.find(_(keyIndex) equals row(keyIndex))

      if (sameKeyRow.isDefined) {
        sameKeyRow.get += row
      } else {
        buffer += CellSeq(row)
      }
    }

    buffer
  }

}
