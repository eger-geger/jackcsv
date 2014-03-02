package org.jackcsv.swing.wizards

import org.jackcsv.swing.panels.{ComponentLoaded, TextFieldPanel, TextAreaPanel, WizardPanel}
import org.jackcsv.table.STable
import org.jackcsv.TableController

class InputTableWizard(private val onFinish: STable => Unit) extends WizardPanel {

  private var table:STable = null

  private val tableSourcePanel = new TextAreaPanel{
    title = "Input Table:"
  }

  private val tableNamePanel = new TextFieldPanel {
    title = "Table Name:"
  }

  this += tableSourcePanel
  this += tableNamePanel

  reactions += {
    case ComponentLoaded(`tableNamePanel`) =>
      tableNamePanel.content = table.name
  }

  controllers += {
    case `tableSourcePanel` =>
      table = TableController.createTable(tableSourcePanel.content)

    case `tableNamePanel` =>
      TableController.renameTable(tableNamePanel.content, table)
      onFinish(table)
  }
}
