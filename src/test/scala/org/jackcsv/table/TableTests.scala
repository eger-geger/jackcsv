package org.jackcsv.table

import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{Matchers, FunSuite}

class TableTests extends FunSuite with Matchers with TableDrivenPropertyChecks {

  val nul = null

  val table: STable = List(
    "a" :: "c" :: "g" :: Nil,
    "c" :: "d" :: "e" :: Nil,
    "k" :: "a" :: "c" :: Nil
  )

  test("should return column") {
    table column 0 shouldEqual CellSeq("a" :: "c" :: "k" :: Nil)
    table column 1 shouldEqual CellSeq("c" :: "d" :: "a" :: Nil)
    table column 2 shouldEqual CellSeq("g" :: "e" :: "c" :: Nil)
  }

  test("should return row") {
    table row 0 shouldEqual CellSeq("a" :: "c" :: "g" :: Nil)
    table row 1 shouldEqual CellSeq("c" :: "d" :: "e" :: Nil)
    table row 2 shouldEqual CellSeq("k" :: "a" :: "c" :: Nil)
  }

  test("should generate headers") {
    table.headers shouldEqual "column 1" :: "column 2" :: "column 3" :: Nil
  }

  test("should return table size") {
    table.columnCount shouldEqual 3
    table.rowCount shouldEqual 3
    table.size shouldEqual(3, 3)
  }

  test("should add tables") {
    NTable(table, 0) + NTable(table, 1) shouldEqual STable(List(
      "a" :: "c" :: "g" :: "k" :: "c" :: Nil,
      "c" :: "d" :: "e" :: "a" :: "g" :: Nil,
      "k" :: "a" :: "c" :: nul :: nul :: Nil,
      "d" :: nul :: nul :: "c" :: "e" :: Nil
    ))
  }

}
