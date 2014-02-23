package org.jackcsv.table

import org.scalatest.{BeforeAndAfterAll, FunSuite}
import org.scalatest.matchers.ShouldMatchers._

import java.nio.file.{Path, Files}
import java.nio.charset.Charset
import org.supercsv.prefs.CsvPreference

class STableFactoryTests extends FunSuite with BeforeAndAfterAll {

  private def writeTempFile(strings: String): Path = {
    val tmpPath = Files.createTempFile("test", ".csv")
    val bw = Files.newBufferedWriter(tmpPath, Charset.defaultCharset())

    try {
      bw.write(strings)
    } finally {
      bw.close()
    }

    return tmpPath
  }

  private var standardCsv: Path = null
  private var excelCsv: Path = null

  override def beforeAll {
    val csvLine = "one,two,three"

    standardCsv = writeTempFile(List(csvLine, csvLine, csvLine).mkString("\r\n"))
    excelCsv = writeTempFile(List(csvLine, csvLine, csvLine).mkString("\n"))
  }

  override def afterAll {
    Files.deleteIfExists(standardCsv)
    Files.deleteIfExists(excelCsv)
  }

  test("should create table from values") {
    val row = "one" :: "two" :: "three" :: Nil
    val table:STable = row :: row :: row :: Nil

    table.size shouldEqual(3, 3)
    table.column(1).size shouldEqual 3
    table.row(1).size shouldEqual 3
    table.name should not be null
  }

  test("should create table from tabbed strings") {
    val strings = "one \t two \t three"
    val table = TableFactory.create(strings + '\n' + strings + '\n' + strings)

    table.size shouldEqual(3, 3)
    table.column(1).size shouldEqual 3
    table.row(1).size shouldEqual 3
    table.name should not be null
  }

  test("should create table from standard csv file") {
    val table = TableFactory.create(standardCsv.toFile, CsvPreference.STANDARD_PREFERENCE)

    table.size shouldEqual(3, 3)
    table.column(1).size shouldEqual 3
    table.row(1).size shouldEqual 3
    table.name should not be null
  }

  test("should create table from excel csv file") {
    val table = TableFactory.create(excelCsv.toFile, CsvPreference.EXCEL_PREFERENCE)

    table.size shouldEqual(3, 3)
    table.column(1).size shouldEqual 3
    table.row(1).size shouldEqual 3
    table.name should not be null
  }
}
