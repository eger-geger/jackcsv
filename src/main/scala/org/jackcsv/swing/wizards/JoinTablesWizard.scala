package org.jackcsv.swing.wizards

import org.jackcsv.TableController
import org.jackcsv.swing.panels.{ComponentLoaded, TextFieldPanel, NTableListPanel, WizardPanel}
import org.jackcsv.table.NTable

class JoinTablesWizard(onFinish: NTable => Unit) extends WizardPanel {

  private var table: NTable = null

  private val tableListPanel = new NTableListPanel

  private val tableNamePanel = new TextFieldPanel {
    title = "Table Name:"
  }

  this += tableListPanel
  this += tableNamePanel

  reactions += {
    case ComponentLoaded(`tableNamePanel`) =>
      tableNamePanel.content = table.name
  }

  controllers += {
    case `tableListPanel` =>
      table = TableController.joinTables(tableListPanel.tables)

    case `tableNamePanel` =>
      TableController.renameTable(tableNamePanel.content, table)
      onFinish(table)

  }
}
