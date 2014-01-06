package com.jackcsv.io

import java.io.{FileWriter, File}
import org.supercsv.prefs.CsvPreference
import org.supercsv.io.CsvListWriter
import scala.collection.convert.WrapAsJava

trait CsvWriter {

  protected def rowsToWrite:Iterator[Iterator[String]]

  def writeStandardCsv(file:File) =
    CsvWriter.write(file, CsvPreference.STANDARD_PREFERENCE, rowsToWrite)

  def writeExcelCsv(file:File) =
    CsvWriter.write(file, CsvPreference.EXCEL_PREFERENCE, rowsToWrite)
}

object CsvWriter extends WrapAsJava {

  def write(file:File, preferences:CsvPreference, rows:Iterator[Iterator[String]]):Unit = {
    val writer = new CsvListWriter(new FileWriter(file), preferences)

    try {
      rows.foreach((seq)=> writer.write(seqAsJavaList(seq.toSeq)))
    } finally {
      writer.close()
    }
  }

}
