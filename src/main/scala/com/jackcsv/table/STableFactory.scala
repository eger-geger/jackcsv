package com.jackcsv.table

import com.jackcsv.io.CsvReader
import java.io.File
import org.supercsv.prefs.CsvPreference


object STableFactory {

  def create(input: String): STable = input.split('\n').map(_.split('\t').toSeq).toSeq

  def create(file: File, csvPref:CsvPreference): STable = CsvReader.read(file, csvPref)

}
