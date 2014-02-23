package org.jackcsv.io

import org.supercsv.io.{ICsvListReader, CsvListReader}
import java.io.{File, FileReader}
import org.supercsv.prefs.CsvPreference

import scala.collection.JavaConversions._

object CsvReader {

  private def read(reader:ICsvListReader):List[List[String]] = {
    val row = reader.read()

    if(row == null) Nil else row.toList :: read(reader)
  }

  def read(file:File, preferences:CsvPreference):List[List[String]] =
    read(new CsvListReader(new FileReader(file), preferences))
}
