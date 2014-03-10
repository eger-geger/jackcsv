package org.jackcsv.io

import java.io.{FileWriter, File}
import org.supercsv.prefs.CsvPreference
import org.supercsv.io.CsvListWriter
import scala.collection.convert.WrapAsJava

object CsvWriter extends WrapAsJava {

  def write(file:File, preferences:CsvPreference, rows:Iterable[Iterable[String]]):Unit = {
    val writer = new CsvListWriter(new FileWriter(file), preferences)

    try {
      rows.foreach((seq)=> writer.write(seqAsJavaList(seq.toSeq)))
    } finally {
      writer.close()
    }
  }

}
