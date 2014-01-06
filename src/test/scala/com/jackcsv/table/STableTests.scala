package com.jackcsv.table

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.prop.TableDrivenPropertyChecks

class STableTests extends FunSuite with TableDrivenPropertyChecks with ShouldMatchers {

  val tablesToAdd = Table(
    ("settings", "table"),
    (List(), new STable(List()))
  )

  test("should add tables"){

  }

}
