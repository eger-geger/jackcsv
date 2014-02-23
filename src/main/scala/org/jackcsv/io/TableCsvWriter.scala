package org.jackcsv.io

import org.jackcsv.table.STable

class TableCsvWriter(table: STable) extends CsvWriter {

  override def rowsToWrite: Iterator[Iterator[String]] =
    table.rows.map(row => row.map(_.value.toString).toIterator).toIterator

}
