package com.jackcsv.swing

import com.jackcsv.swing.wizards.{WizardFinished, WizardStep, WizardPanel}
import com.jackcsv.table.STable
import scala.swing.{ListView, GridBagPanel, ComboBox}
import scala.collection.mutable
import scala.swing.event.SelectionChanged
import scala.collection.mutable.ArrayBuffer

class MergeTablesWizardPanel extends WizardPanel(new MergeTables) {

  steps += new GridBagPanel with WizardStep {
    model.settings ++= Tuple2(null, -1) :: Tuple2(null, -1) :: Nil

    addTableControls(1)
    addTableControls(2)

    private def addTableControls(index: Int) {
      val columnsCtrl = new ListView[String]()
      val tableCtrl = new ComboBox(STable.tables)

      tableCtrl.reactions += {
        case SelectionChanged(_) => {
          val selectedTable = tableCtrl.selection.item
          model.settings.update(index, (selectedTable, 0))
          columnsCtrl.listData = selectedTable.headers
        }
      }

      columnsCtrl.reactions += {
        case SelectionChanged(src: ListView[_]) if src.selection.indices.nonEmpty => {
          val selectedTable = model.settings(index)._1
          val selectedColumnIndex = columnsCtrl.selection.indices.head
          model.settings.update(index, (selectedTable, selectedColumnIndex))
        }
      }

    }

    def isFinished: Boolean = {
      model.settings.size == 2 && model.settings.forall(v => v._1 != null && v._2 > -1)
    }

    def description: String = "Select Table and Columns"

    def title: String = "Select Table and Columns"
  }

  reactions += {
    case WizardFinished(merge: MergeTables) => ???
  }

  def title: String = "Merge Tables Wizard"
}

case class MergeTables(settings: mutable.Buffer[(STable, Int)]) {
  def this() = this(new ArrayBuffer)
}