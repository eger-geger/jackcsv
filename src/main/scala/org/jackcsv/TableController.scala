package org.jackcsv

import java.io.File
import org.jackcsv.io.TableCsvWriter
import org.jackcsv.table.{NTable, TableFactory, STable}
import org.supercsv.prefs.CsvPreference

object TableController {

  def createTable(source:String):STable = {
    require(source != null, "Table source is null")
    require(!source.isEmpty, "Table source is empty")

    TableFactory.create(source)
  }

  def importTable(file:File, preferences:CsvPreference):STable = {
    require(file != null, "File is undefined")
    require(!file.isDirectory, "File cannot be folder")
    require(file.exists(), "File does not exists")
    require(preferences != null, "Preferences are undefined")

    TableFactory.create(file, preferences)
  }

  def joinTables(tables:Seq[NTable]):STable = {
    require(tables.size >= 2, "Need at least 2 tables")

    tables.foldLeft(tables(0))((t1, t2) => t1 + t2)
  }

  def exportTable(file:File, preferences:CsvPreference, table:STable){
    require(file != null, "File is undefined")
    require(!file.isDirectory, "File cannot be folder")
    require(file.exists(), "File does not exists")
    require(preferences != null, "Preferences are undefined")
    require(table != null, "Table is undefined")

    new TableCsvWriter(table).writeStandardCsv(file)
  }

  def renameTable(name:String, table:STable){
    require(name != null, "Name is undefined")
    require(!name.isEmpty, "Name is empty")
    require(table != null, "Table is undefined")

    table.name = name
  }

}
