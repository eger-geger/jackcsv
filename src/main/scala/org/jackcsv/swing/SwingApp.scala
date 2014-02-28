package org.jackcsv.swing

import akka.actor.ActorSystem
import org.jackcsv.swing.panels._
import org.jackcsv.swing.utils.MenuBuilder._
import scala.swing._
import scala.swing.TabbedPane.Page
import org.jackcsv.swing.panels.WizardCanceled
import org.jackcsv.swing.panels.WizardFinished
import org.jackcsv.table.{NTable, STable}
import org.jackcsv.TableController

object SwingApp extends SimpleSwingApplication {

  AppFrame.title = "Swing Table Utility"
  AppFrame.minimumSize = new Dimension(400, 400)
  AppFrame.preferredSize = new Dimension(400, 400)

  override def top: Frame = AppFrame

}

object AppFrame extends MainFrame {

  private implicit val actorSystem = ActorSystem("UI")

  private object TabbedPane extends XTabbedPane {
    reactions += {
      case e @ (WizardCanceled(_) | WizardFinished(_)) =>
        pages.find(_.content == e.asInstanceOf[WizardPanelEvent].wizardPanel).foreach(pages -= _)
    }
  }

  contents = TabbedPane

  menuBar = new MenuBar {
    _contents += new Menu("Tables") {
      _contents += "Show" --> {
        TabbedPane.pages += new Page("[Show Table]", new WizardPanel{
          this += new STableListPanel

          controllers += {
            case p:STableListPanel =>
              require(p.selectedTable != null, "Table not selected")
              TabbedPane.pages += new Page(p.selectedTable.name, new TablePanel(p.selectedTable))
          }

          TabbedPane.listenTo(this)
        })
      }

      _contents += "Combine" --> {
        TabbedPane.pages += new Page("[Join Tables]", new WizardPanel{
          private var table:NTable = null

          private val tableListPanel = new NTableListPanel

          private val tableNamePanel = new TextFieldPanel {
            title = "Table Name:"
          }

          this += tableListPanel
          this += tableNamePanel

          reactions += {
            case ComponentLoaded(c:TextFieldPanel) =>
              c.content = table.name
          }

          controllers += {
            case p:NTableListPanel =>
              table = TableController.joinTables(p.tables)

            case p:TextFieldPanel =>
              TableController.renameTable(p.content, table)
          }

          TabbedPane.listenTo(this)
        })
      }

      _contents += new Menu("Import") {
        _contents += "...from CSV" --> {
          TabbedPane.pages += new Page("[Import Table]", new WizardPanel{
            var table:STable = null

            private val fileChooserPanel = new CSVChooserPanel(FileDialogMode.Open)

            private val tableNamePanel = new TextFieldPanel {
              title = "Table Name:"
            }

            this += fileChooserPanel
            this += tableNamePanel

            reactions += {
              case ComponentLoaded(c:TextFieldPanel) =>
                c.content = table.name
            }

            controllers += {
              case p:CSVChooserPanel =>
                table = TableController.importTable(p.selectedFile, p.selectedPref)

              case p:TextFieldPanel =>
                TableController.renameTable(p.content, table)
            }

            TabbedPane.listenTo(this)
          })
        }

        _contents += "...from text" --> {
          TabbedPane.pages += new Page("[Import Table]", new WizardPanel {
            var table:STable = null

            private val tableSourcePanel = new TextAreaPanel{
              title = "Input Table:"
            }

            private val tableNamePanel = new TextFieldPanel {
              title = "Table Name:"
            }

            this += tableSourcePanel
            this += tableNamePanel

            reactions += {
              case ComponentLoaded(c:TextFieldPanel) =>
                c.content = table.name
            }

            controllers += {
              case p:TextAreaPanel =>
                table = TableController.createTable(p.content)

              case p:TextFieldPanel =>
                TableController.renameTable(p.content, table)
            }

            TabbedPane.listenTo(this)
          })
        }
      }

      _contents += "Export to CSV" --> {
        TabbedPane.pages += new Page("[Export Table]", new WizardPanel{
          private var table:STable = null

          private val tableListPanel = new STableListPanel

          private val fileChooserPanel = new CSVChooserPanel(FileDialogMode.Save)

          this += tableListPanel
          this += fileChooserPanel

          controllers += {
            case c:STableListPanel =>
              require(tableListPanel.selectedTable != null, "Table not selected")
              table = tableListPanel.selectedTable

            case c:CSVChooserPanel =>
              TableController.exportTable(fileChooserPanel.selectedFile, fileChooserPanel.selectedPref, table)
          }

          TabbedPane.listenTo(this)
        })
      }
    }
  }
}
