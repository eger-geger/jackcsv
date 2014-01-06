package com.jackcsv.table

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.prop.TableDrivenPropertyChecks

class SCellSeqTests extends FunSuite with ShouldMatchers with TableDrivenPropertyChecks  {

  val rowsToAdd = Table[SCellSeq, SCellSeq, SCellSeq](
    ("---row 1---",       "---row 2---",  "---row 3---"),
    ("a"::"b"::Nil,       "c"::"d"::Nil,  "a, c"::"b, d"::Nil),
    ("a"::Nil,            "b"::"c"::Nil,  "a, b"::"c"::Nil),
    (NullSCell::1::Nil,   2::3::Nil,      2::4f::Nil),
    (Nil,                 3::5::Nil,      3::5::Nil)
  )

  test("should produce new cell sequence") {
    forAll(rowsToAdd) {
      (row1, row2, row3) => row1 + row2 shouldEqual row3
    }
  }

  test("should alter existing cell sequence") {
    forAll(rowsToAdd){
      (row1, row2, row3) => new SCellSeq(row1) {this += row2} shouldEqual row3
    }
  }

}
