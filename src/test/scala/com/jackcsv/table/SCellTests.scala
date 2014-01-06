package com.jackcsv.table

import org.scalatest.FunSuite
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.matchers.ShouldMatchers

class SCellTests extends FunSuite with TableDrivenPropertyChecks with ShouldMatchers {

  val cellsToAdd = Table[SCell, SCell, SCell](
    ("cell1",   "cell2",     "cell3"),
    ("a",       "b",         "a, b"),
    (1f,        2f,          3f),
    ("a",       1f,          "a, 1.0"),
    (1f,        "a",         "1.0, a"),
    (NullSCell, "a",         "a"),
    ("a",       NullSCell,   "a"),
    (NullSCell, "a",         "a"),
    ("a",       "a",         "a"),
    ("a",       NullSCell,   "a")
  )

  test("should produce new cell") {
    forAll(cellsToAdd) {
      (cell1, cell2, cell3) => cell1 + cell2 shouldEqual cell3
    }
  }

  test("should change existing cell"){
    forAll(cellsToAdd){
      (cell1, cell2, cell3) => new SCell(cell1.value) {this += cell2} shouldEqual cell3
    }
  }

}
