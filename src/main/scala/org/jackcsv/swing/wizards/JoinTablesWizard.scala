package org.jackcsv.swing.wizards

import org.jackcsv.TableController
import org.jackcsv.swing.panels.{ComponentLoaded, TableNamePanel, NTableListPanel, WizardPanel}
import org.jackcsv.table.{STable, NTable}

class JoinTablesWizard(private val tables: Seq[STable], private val onFinish: NTable => Unit) extends WizardPanel {

  private var table: NTable = null

  private val tableListPanel = new NTableListPanel(tables)

  private val tableNamePanel = new TableNamePanel

  this += tableListPanel
  this += tableNamePanel

  reactions += {
    case ComponentLoaded(`tableNamePanel`) =>
      tableNamePanel.tableName = table.name
  }

  controllers += {
    case `tableListPanel` =>
      table = TableController.joinTables(tableListPanel.tables)

    case `tableNamePanel` =>
      TableController.renameTable(tableNamePanel.tableName, table)
      onFinish(table)

  }
}
