package com.jackcsv.table

import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{Matchers, FunSuite}

class STableTests extends FunSuite with Matchers with TableDrivenPropertyChecks {

  val nul = null

  val table: STable = List(
    "a" :: "c" :: "g" :: Nil,
    "c" :: "d" :: "e" :: Nil,
    "k" :: "a" :: "c" :: Nil
  )

  test("should return column") {
    table column 0 shouldEqual SCellSeq("a" :: "c" :: "k" :: Nil)
    table column 1 shouldEqual SCellSeq("c" :: "d" :: "a" :: Nil)
    table column 2 shouldEqual SCellSeq("g" :: "e" :: "c" :: Nil)
  }

  test("should return row") {
    table row 0 shouldEqual SCellSeq("a" :: "c" :: "g" :: Nil)
    table row 1 shouldEqual SCellSeq("c" :: "d" :: "e" :: Nil)
    table row 2 shouldEqual SCellSeq("k" :: "a" :: "c" :: Nil)
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
    val t0 = new STable(table.rows) with PrimaryKey {val keyIndex = 0}
    val t1 = new STable(table.rows) with PrimaryKey {val keyIndex = 1}

    t0 + t1 shouldEqual STable(List(
      "a" :: "c" :: "g" :: "k" :: "c" :: Nil,
      "c" :: "d" :: "e" :: "a" :: "g" :: Nil,
      "k" :: "a" :: "c" :: nul :: nul :: Nil,
      "d" :: nul :: nul :: "c" :: "e" :: Nil
    ))

    val t2 = new STable(table.rows) with PrimaryKey {val keyIndex = 2; this += t1}

    t2 shouldEqual STable(List(
      "a" :: "c" :: "g" :: nul :: nul :: Nil,
      "c" :: "d" :: "e" :: nul :: nul :: Nil,
      "k" :: "a" :: "c" :: "a" :: "g" :: Nil,
      nul :: nul :: "d" :: "c" :: "e" :: Nil,
      nul :: nul :: "a" :: "k" :: "c" :: Nil
    ))
  }

}
