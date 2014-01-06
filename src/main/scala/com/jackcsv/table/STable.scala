package com.jackcsv.table

import scala.collection.mutable.ListBuffer

class STable(var name: String, protected val _rows: Seq[Seq[SCell]]) {

  STable.managed += this

  def this(cells: Seq[Seq[SCell]]) = this(NameGenerator.generatedName[STable], cells)

  def apply(r: Int)(c: Int): SCell = rows(r)(c)

  def row(idx: Int): SCellSeq = new SCellSeq(rows(idx))

  def column(idx: Int): SCellSeq = new SCellSeq(0 until rowCount map (apply(_)(idx)))

  def columnCount = rows.maxBy(_.size).size

  def rowCount = rows.size

  def size = (rowCount, columnCount)

  def headers: Seq[String] = 1 to columnCount map (i => s"column $i")

  def rows: Seq[SCellSeq] = _rows map (new SCellSeq(_))

  override def toString = name
}

object STable {

  private val managed = new ListBuffer[STable]

  implicit def convert(values: Traversable[Traversable[Any]]): STable = new STable(values.map(SCellSeq.convert).toSeq)

  def tables = managed.toSeq

}

trait PrimaryKey {
  self: STable =>

  val keyIndex: Int

  private var normalRows = SCellSeq.normalize(_rows)(keyIndex)

  def +(other: STable with PrimaryKey): STable with PrimaryKey =
    new STable(rows) with PrimaryKey {
      this += other
      val keyIndex = self.keyIndex
    }

  def +=(other: STable with PrimaryKey): Unit = {
    normalRows = SCellSeq.normalize(rows ++ other.compatibleRows(this))(keyIndex)
  }

  def compatibleRows(other: STable with PrimaryKey): Seq[SCellSeq] = {
    val leftShift = other.columnCount
    val rightShift = leftShift - 1
    val lastIndex = columnCount - 1
    val firstIndex = 0

    val pattern = new ListBuffer[(Int, Int)]
    pattern += Tuple2(keyIndex, other.keyIndex)
    pattern ++= firstIndex until keyIndex map (i => (i, i + leftShift))
    pattern ++= lastIndex until keyIndex map (i => (i, i + rightShift))

    rows.map(_.transform(pattern))
  }

  override def rows = normalRows

}