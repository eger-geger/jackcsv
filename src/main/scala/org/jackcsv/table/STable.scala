package org.jackcsv.table

import scala.collection.mutable
import scala.reflect.ClassTag
import org.jackcsv.NameGenerator

class STable(var name: String, val rows: Seq[Seq[Cell]]) {

  STable.managed += this

  def this(cells: Seq[Seq[Cell]]) = this(NameGenerator.generatedName[STable], cells)

  def apply(r: Int)(c: Int): Cell = rows(r)(c)

  def row(idx: Int): CellSeq = new CellSeq(rows(idx))

  def column(idx: Int): CellSeq = new CellSeq(0 until rowCount map (apply(_)(idx)))

  def columnCount = rows.maxBy(_.size).size

  def rowCount = rows.size

  def size = (rowCount, columnCount)

  def headers: Seq[String] = 1 to columnCount map (i => s"column $i")

  override def toString = s"$name:[${rows.map(_.mkString("\t", "|", "")).mkString("\n", "\n", "\n")}]"

  override def equals(obj: Any) = obj match {
    case t: STable => rows equals t.rows
    case _ => false
  }
}

object STable {

  private val managed = new mutable.HashSet[STable]

  implicit def apply(values: Traversable[Traversable[Any]]): STable =
    new STable(values.map(CellSeq.apply).toSeq)

  implicit def toCellArray[B >: Cell : ClassTag](table:STable) : Array[Array[B]] =
    table.rows.map(_.toArray[B]).toArray

  def tables = managed.toSet

}