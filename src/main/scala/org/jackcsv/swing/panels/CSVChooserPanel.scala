package org.jackcsv.swing.panels

import java.awt.BorderLayout
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileFilter
import org.supercsv.prefs.CsvPreference
import scala.swing._

class CSVChooserPanel extends GridBagPanel {

  private val fileChooser = new JFileChooser {
    this.setControlButtonsAreShown(false)
    this.setFileSelectionMode(JFileChooser.FILES_ONLY)
    this.setFileFilter(new FileFilter {
      def getDescription: String = "*.csv"

      def accept(f: File): Boolean = f.getName.endsWith(".csv")
    })
  }

  private val preferenceCbx = new ComboBox[CsvPreference](CSVChooserPanel.csvPreferences)

  private val fileChooserPanel = new BorderPanel {
    peer.add(fileChooser, BorderLayout.CENTER)
  }

  minimumSize = new Dimension(fileChooser.getWidth, (fileChooser.getHeight + preferenceCbx.minimumSize.getHeight +
    20).toInt)

  add(new Label("CSV type:"), new Constraints {
    grid = (0, 0)
    weightx = 0
    weighty = 0
    insets = new Insets(5, 5, 5, 0)
  })

  add(preferenceCbx, new Constraints {
    grid = (1, 0)
    weightx = 1
    weighty = 0
    insets = new Insets(5, 0, 5, 5)
    fill = GridBagPanel.Fill.Horizontal
  })

  add(fileChooserPanel, new Constraints {
    grid = (0, 1)
    gridwidth = 2
    weightx = 1
    weighty = 1
    insets = new Insets(5, 5, 5, 5)
    fill = GridBagPanel.Fill.Both
  })

  protected def selectedFile = fileChooser.getSelectedFile

  protected def selectedPref = preferenceCbx.selection.item

}

object CSVChooserPanel {

  val csvPreferences = CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE ::
    CsvPreference.EXCEL_PREFERENCE ::
    CsvPreference.STANDARD_PREFERENCE ::
    CsvPreference.TAB_PREFERENCE :: Nil

}