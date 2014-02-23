package org.jackcsv.swing.panels

import org.jackcsv.table.STable
import scala.swing.{ScrollPane, Table}

class TablePanel(val table:STable) extends ScrollPane(new Table(table, table.headers))
