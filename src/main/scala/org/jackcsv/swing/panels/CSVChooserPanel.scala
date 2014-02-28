package org.jackcsv.swing.panels

import java.io.File
import javax.swing.filechooser.FileFilter
import org.jackcsv.swing.panels.FileDialogMode.FileDialogMode
import org.supercsv.prefs.CsvPreference
import scala.swing._
import scala.swing.event.MouseClicked
import org.jackcsv.Logger

class CSVChooserPanel(fileDialogMode: FileDialogMode) extends GridBagPanel with Logger {

  private var _selectedFile: File = null

  private val fileChooser = new FileChooser {
    controlButtonsAreShown = true
    fileSelectionMode = FileChooser.SelectionMode.FilesOnly
    fileFilter = new FileFilter {
      def getDescription: String = "*.csv"

      def accept(f: File): Boolean = f.getName.endsWith(".csv")
    }
  }

  private val preferenceCbx = new ComboBox[CSVPreferenceWrapper](
    CSVPreferenceWrapper(CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE, "North Europe") ::
    CSVPreferenceWrapper(CsvPreference.EXCEL_PREFERENCE, "Excel") ::
    CSVPreferenceWrapper(CsvPreference.STANDARD_PREFERENCE, "Standard") ::
    CSVPreferenceWrapper(CsvPreference.TAB_PREFERENCE, "Tabs Delimited") :: Nil
  )

  private val selectedFileTxt = new TextField {
    this.listenTo(mouse.clicks)

    this.editable = false

    reactions += {
      case x:MouseClicked =>
        val result = fileDialogMode match {
          case FileDialogMode.Open =>
            fileChooser.showOpenDialog(CSVChooserPanel.this)

          case FileDialogMode.Save =>
            fileChooser.showSaveDialog(CSVChooserPanel.this)
        }

        result match {
          case FileChooser.Result.Approve =>
            _selectedFile = fileChooser.selectedFile

            if (_selectedFile != null) {
              this.text = _selectedFile.getAbsolutePath
            } else {
              this.text = ""
            }

          case _ =>
            fileChooser.selectedFile = _selectedFile
        }
    }
  }

  add(new Label("CSV type:"), new Constraints {
    grid = (0, 0)
    weightx = 0
    weighty = 0
    insets = new Insets(5, 5, 5, 0)
  })

  add(preferenceCbx, new Constraints {
    grid = (1, 0)
    weightx = 1
    weighty = 1
    insets = new Insets(5, 0, 5, 5)
    fill = GridBagPanel.Fill.Horizontal
  })

  add(new Label("Selected File:"), new Constraints{
    grid = (0, 1)
    gridwidth = 1
    weightx = 1
    weighty = 1
    insets = new Insets(5, 5, 5, 5)
  })

  add(selectedFileTxt, new Constraints {
    grid = (1, 1)
    gridwidth = 1
    weightx = 10
    weighty = 1
    insets = new Insets(5, 5, 5, 5)
    fill = GridBagPanel.Fill.Horizontal
  })

  def selectedFile = _selectedFile

  def selectedPref = preferenceCbx.selection.item.preference

}

object FileDialogMode extends Enumeration {
  type FileDialogMode = Value
  val Open, Save = Value
}

private case class CSVPreferenceWrapper(preference: CsvPreference, name: String) {

  override def toString = name

}