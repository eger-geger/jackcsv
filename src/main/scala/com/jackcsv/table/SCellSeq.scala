package com.jackcsv.table

import scala.collection.mutable.ListBuffer

class SCellSeq(private val _cells: Traversable[SCell]) extends Seq[SCell] {

  val cells = new ListBuffer[SCell] ++= _cells

  def length: Int = cells.length

  def apply(idx: Int): SCell = cells.applyOrElse[Int, SCell](idx, i => NullSCell)

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
    val cellBuffer = new ListBuffer[SCell] ++= List.fill(pattern.maxBy(_._2)._2)(NullSCell)

    for (p <- pattern) {
      cellBuffer.update(p._2, this(p._1))
    }

    new SCellSeq(cellBuffer)
  }

}

object SCellSeq {

  implicit def convert(seq:Traversable[Any]):SCellSeq = new SCellSeq(seq.map(SCell(_)))

  def normalize(rows: Seq[Seq[SCell]])(keyIndex: Int): Seq[SCellSeq] = {
    val buffer = new ListBuffer[SCellSeq]

    for (row <- rows) {
      val sameKeyRow = buffer.find(_(keyIndex) equals row(keyIndex))

      if (sameKeyRow.isDefined) {
        sameKeyRow.get += row
      } else {
        buffer += new SCellSeq(row)
      }
    }

    buffer
  }

}
