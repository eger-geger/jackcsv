package org.jackcsv.swing.wizards

import org.jackcsv.TableController
import org.jackcsv.swing.panels.{ComponentLoaded, TableNamePanel, TableSourcePanel, WizardPanel}
import org.jackcsv.table.STable

class InputTableWizard(private val onFinish: STable => Unit) extends WizardPanel {

  private var table: STable = null

  private val tableSourcePanel = new TableSourcePanel

  private val tableNamePanel = new TableNamePanel

  this += tableSourcePanel
  this += tableNamePanel

  reactions += {
    case ComponentLoaded(`tableNamePanel`) =>
      tableNamePanel.tableName = table.name
  }

  controllers += {
    case `tableSourcePanel` =>
      table = TableController.createTable(tableSourcePanel.content)

    case `tableNamePanel` =>
      TableController.renameTable(tableNamePanel.tableName, table)
      onFinish(table)
  }
}
