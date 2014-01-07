package com.jackcsv.table

import scala.collection.mutable.ListBuffer

class SCellSeq(private val _cells: Traversable[SCell]) extends Seq[SCell] {

  val cells = new ListBuffer[SCell] ++= _cells

  def length: Int = cells.length

  def apply(idx: Int): SCell = cells.applyOrElse[Int, SCell](idx, i => EmptyCell)

  def iterator: Iterator[SCell] = cells.iterator

  def +(cellSeq: Seq[SCell]): SCellSeq = new SCellSeq(cells) { this += cellSeq }

  def +=(cellSeq: Seq[SCell]) {
    for (i <- 0 until cellSeq.size) {
      if (cells.isDefinedAt(i)) {
        cells.update(i, cells(i) + cellSeq(i))
      } else {
        cells += cellSeq(i)
      }
    }
  }

  def transform(pattern: Seq[(Int, Int)]): SCellSeq = {
    val newSize = pattern.maxBy(_._2)._2 + 1

    val buffer = new ListBuffer[SCell] ++= List.fill(newSize)(EmptyCell)

    for (p <- pattern) {
      buffer.update(p._2, this(p._1))
    }

    new SCellSeq(buffer)
  }

}

object SCellSeq {

  implicit def apply(seq:Traversable[Any]):SCellSeq = new SCellSeq(seq.map(SCell(_)))

  def normalize(rows: Seq[Seq[SCell]])(keyIndex: Int): Seq[SCellSeq] = {
    val buffer = new ListBuffer[SCellSeq]

    val size = rows.map(_.size).max

    val emptyCells = List.fill(_:Int)(EmptyCell)

    for (r <- rows; row = r.toList ::: emptyCells(size - r.size)) {
      val sameKeyRow = buffer.find(_(keyIndex) equals row(keyIndex))

      if (sameKeyRow.isDefined) {
        sameKeyRow.get += row
      } else {
        buffer += SCellSeq(row)
      }
    }

    buffer
  }

}
