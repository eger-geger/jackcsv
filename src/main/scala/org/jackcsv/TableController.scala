package org.jackcsv

import java.io.File
import org.jackcsv.io.TableCsvWriter
import org.jackcsv.table.{NTable, TableFactory, STable}
import org.supercsv.prefs.CsvPreference

object TableController extends Validation with Localization {

  def createTable(source:String):STable = {
    require(source != null,   l("errors.table_source_empty"))
    require(!source.isEmpty,  l("errors.table_source_empty"))

    TableFactory.create(source)
  }

  def importTable(file:File, preferences:CsvPreference):STable = {
    require(file != null,         l("errors.file_not_selected"))
    require(!file.isDirectory,    l("errors.folder_selected"))
    require(file.exists(),        l("errors.file_not_selected"))
    require(preferences != null,  l("errors.csvpref_undefined"))

    TableFactory.create(file, preferences)
  }

  def joinTables(tables:Seq[NTable]):NTable = {
    require(tables.size >= 2, l("errors.need_more_tables"))

    tables.tail.foldLeft(tables.head)(_ + _)
  }

  def exportTable(file:File, preferences:CsvPreference, table:STable){
    require(file != null,         l("errors.file_not_selected"))
    require(!file.isDirectory,    l("errors.folder_selected"))
    require(preferences != null,  l("errors.csvpref_undefined"))
    require(table != null,        l("errors.table_not_selected"))

    new TableCsvWriter(table).writeStandardCsv(file)
  }

  def renameTable(name:String, table:STable){
    require(name != null,   l("errors.table_name_empty"))
    require(!name.isEmpty,  l("errors.table_name_empty"))
    require(table != null,  l("errors.table_not_selected"))

    table.name = name
  }

}
