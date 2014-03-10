package org.jackcsv.table

import java.io.File
import org.jackcsv.io.CsvReader
import org.supercsv.prefs.CsvPreference


object TableFactory {

  @throws[TableCreationException]
  def create(input: String): STable =
    try {
      input.split('\n').map(_.split(',').toSeq).toSeq
    } catch {
      case th: Throwable => throw new TableCreationException(th)
    }

  @throws[TableCreationException]
  def create(file: File, csvPref: CsvPreference): STable =
    try{
      CsvReader.read(file, csvPref)
    } catch {
      case th:Throwable => throw new TableCreationException(th)
    }

}

class TableCreationException(cause: Throwable) extends Exception("Failed to create table", cause)