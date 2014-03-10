package org.jackcsv.swing.panels

import java.awt.Insets
import scala.swing.{Label, TextField, GridBagPanel}
import org.jackcsv.i10n.Localization
import java.util.ResourceBundle

class TableNamePanel extends GridBagPanel {

  private val contentTxt = new TextField

  private val titleLabel = new Label with Localization {
    override def updateLocalizedStrings(bundle: ResourceBundle): Unit = {
      text = bundle.getString("table_name_panel.title")
    }
  }

  add(titleLabel, new Constraints {
    grid = (0, 0)
    weightx = 0
    weighty = 1
    insets = new Insets(5, 5, 5, 0)
  })

  add(contentTxt, new Constraints {
    grid = (1, 0)
    weighty = 1
    weightx = 1
    insets = new Insets(5, 0, 5, 5)
    fill = GridBagPanel.Fill.Horizontal
  })

  def tableName = contentTxt.text
  def tableName_=(value: String) = contentTxt.text = value

}