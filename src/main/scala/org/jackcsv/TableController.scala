package org.jackcsv

import java.io.File
import org.jackcsv.i10n.Localization
import org.jackcsv.table.{NTable, TableFactory, STable}
import org.supercsv.prefs.CsvPreference
import org.jackcsv.io.CsvWriter

object TableController extends Validation {

  def createTable(source: String): STable = {
    require(source != null, Localization.localized("errors.table_source_empty"))
    require(!source.isEmpty, Localization.localized("errors.table_source_empty"))

    TableFactory.create(source)
  }

  def importTable(file: File, preferences: CsvPreference): STable = {
    require(file != null, Localization.localized("errors.file_not_selected"))
    require(!file.isDirectory, Localization.localized("errors.folder_selected"))
    require(file.exists(), Localization.localized("errors.file_not_selected"))
    require(preferences != null, Localization.localized("errors.csvpref_undefined"))

    TableFactory.create(file, preferences)
  }

  def joinTables(tables: Seq[NTable]): NTable = {
    require(tables.size >= 2, Localization.localized("errors.need_more_tables"))

    tables.tail.foldLeft(tables.head)(_ + _)
  }

  def exportTable(file: File, preferences: CsvPreference, table: STable) {
    require(file != null, Localization.localized("errors.file_not_selected"))
    require(!file.isDirectory, Localization.localized("errors.folder_selected"))
    require(preferences != null, Localization.localized("errors.csvpref_undefined"))
    require(table != null, Localization.localized("errors.table_not_selected"))

    CsvWriter.write(file, preferences, table.rows.map(_.map(_.value.toString)))
  }

  def renameTable(name: String, table: STable) {
    require(name != null, Localization.localized("errors.table_name_empty"))
    require(!name.isEmpty, Localization.localized("errors.table_name_empty"))
    require(table != null, Localization.localized("errors.table_not_selected"))

    table.name = name
  }

}
