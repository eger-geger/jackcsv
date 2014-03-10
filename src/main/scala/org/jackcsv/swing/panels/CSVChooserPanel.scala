package org.jackcsv.swing.panels

import java.io.File
import java.util.ResourceBundle
import javax.swing.filechooser.FileFilter
import org.jackcsv.ViewModel
import org.jackcsv.i10n.Localization
import org.jackcsv.swing.panels.FileDialogMode.FileDialogMode
import org.supercsv.prefs.CsvPreference
import scala.swing._
import scala.swing.event.MouseClicked

class CSVChooserPanel(fileDialogMode: FileDialogMode) extends GridBagPanel {

  private var _selectedFile: File = null

  private val fileChooser = new FileChooser with Localization {
    controlButtonsAreShown = true

    fileSelectionMode = FileChooser.SelectionMode.FilesOnly

    fileFilter = new FileFilter {
      def getDescription: String = "*.csv"

      def accept(f: File): Boolean = f.getName.endsWith(".csv")
    }

    override def updateLocalizedStrings(bundle: ResourceBundle): Unit = {

    }
  }

  private val preferenceCbx = new ComboBox[ViewModel[CsvPreference]](
    ViewModel(CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE, Localization.localized("csv_chooser_panel.csv_type.excel.title")) ::
      ViewModel(CsvPreference.STANDARD_PREFERENCE, Localization.localized("csv_chooser_panel.csv_type.standard.title")) ::
      ViewModel(CsvPreference.TAB_PREFERENCE, Localization.localized("csv_chooser_panel.csv_type.tabs.title")) :: Nil
  ) with Localization {
    override def updateLocalizedStrings(bundle: ResourceBundle): Unit = repaint()
  }

  private val selectedFileTxt = new TextField {
    this.listenTo(mouse.clicks)

    this.editable = false

    reactions += {
      case x: MouseClicked =>
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

  private val csvTypeLabel = new Label with Localization {
    override def updateLocalizedStrings(bundle: ResourceBundle): Unit = {
      text = bundle.getString("csv_chooser_panel.csv_type_label")
    }
  }

  private val selectedFileLabel = new Label with Localization {
    override def updateLocalizedStrings(bundle: ResourceBundle): Unit = {
      text = bundle.getString("csv_chooser_panel.selected_file_label")
    }
  }

  add(csvTypeLabel, new Constraints {
    grid = (0, 0)
    weightx = 1
    weighty = 1
    insets = new Insets(5, 5, 5, 0)
  })

  add(preferenceCbx, new Constraints {
    grid = (1, 0)
    weightx = 10
    weighty = 1
    insets = new Insets(5, 0, 5, 5)
    fill = GridBagPanel.Fill.Horizontal
  })

  add(selectedFileLabel, new Constraints {
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

  def selectedPref = preferenceCbx.selection.item.model

}

object FileDialogMode extends Enumeration {
  type FileDialogMode = Value
  val Open, Save = Value
}