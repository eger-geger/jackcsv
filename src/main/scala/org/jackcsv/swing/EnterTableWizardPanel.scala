package com.jackcsv.swing

import com.jackcsv.swing.wizards.{WizardFinished, WizardStep, WizardPanel}
import scala.swing.{TextField, TextArea, BorderPanel}
import com.jackcsv.table.{STableFactory, STable}
import scala.swing.event.EditDone
import scala.swing.BorderPanel.Position

class EnterTableWizardPanel extends WizardPanel(new EnterTable(null, null)) {

  steps += new BorderPanel with WizardStep {
    val tableContentTxt = new TextArea() {
      reactions += {
        case EditDone(s) => model.content = s.text
      }
    }

    val tableNameTxt = new TextField() {
      reactions += {
        case EditDone(s) => model.name = s.text
      }
    }

    add(tableNameTxt, Position.North)
    add(tableContentTxt, Position.Center)

    override def title: String = "New Table"

    override def description: String = "Enter new table"

    override def isFinished: Boolean = !tableContentTxt.text.isEmpty
  }

  reactions += {
    case WizardFinished(model: EnterTable) => {
      val table = STableFactory.create(model.content)

      if (!model.name.isEmpty) {
        table.name = model.name
      }

      publish(new WizardFinished(table))
    }
  }

  update()

  def title: String = "New Table Wizard"
}

case class EnterTable(var name: String, var content: String)
