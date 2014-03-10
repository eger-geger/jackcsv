package org.jackcsv.swing.panels

import javax.swing.BorderFactory
import javax.swing.border.EtchedBorder
import org.jackcsv.ViewModel
import org.jackcsv.table.{NTable, STable}
import scala.collection.mutable.ListBuffer
import scala.swing.event.SelectionChanged
import scala.swing.{ComboBox, GridBagPanel, Insets, Dimension}

class NTablePanel(tables: Seq[STable]) extends GridBagPanel {

  private val columnModel = new ListBuffer[String]

  private val columnCbx = new ComboBox[String](columnModel)

  private val tableCbx = new ComboBox(ViewModel.empty :: ViewModel.fromSeq[STable](tables, _.name).toList ::: Nil)

  tableCbx.selection.reactions += {
    case SelectionChanged(`tableCbx`) =>
      columnModel.clear()
      selectedTable.foreach(columnModel ++= _.headers)
      columnCbx.repaint()
  }

  border = NTablePanel.defaultBorder
  preferredSize = NTablePanel.defaultSize
  minimumSize = NTablePanel.defaultSize

  add(tableCbx, new Constraints {
    grid = (0, 0)
    weightx = 1
    weighty = 1
    insets = NTablePanel.defaultInsets
    fill = GridBagPanel.Fill.Horizontal
    anchor = GridBagPanel.Anchor.Center
  })

  add(columnCbx, new Constraints {
    grid = (0, 1)
    weightx = 1
    weighty = 1
    insets = NTablePanel.defaultInsets
    fill = GridBagPanel.Fill.Horizontal
    anchor = GridBagPanel.Anchor.Center
  })

  def table: NTable = {
    for {
      t <- selectedTable
      i <- selectedColumn
    } return NTable(t, i)

    null
  }

  def table_=(value: NTable) = {
    tableCbx.selection.item = ViewModel(table)
    columnCbx.selection.index = value.keyIndex
  }

  private def selectedTable: Option[STable] =
    Option(tableCbx.selection.item.model)

  private def selectedColumn: Option[Int] = {
    val selectedIndex = columnCbx.selection.index

    if (selectedIndex > -1) {
      Some(selectedIndex)
    } else {
      None
    }
  }
}

object NTablePanel {

  val defaultSize = new Dimension(200, 100)

  val defaultBorder = BorderFactory.createEtchedBorder(EtchedBorder.RAISED)

  val defaultInsets = new Insets(10, 10, 10, 10)

}