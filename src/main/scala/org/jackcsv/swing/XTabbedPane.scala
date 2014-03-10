package org.jackcsv.swing

import java.util.ResourceBundle
import javax.swing.JPopupMenu
import org.jackcsv.i10n.Localization
import scala.swing.event.SelectionChanged
import scala.swing.{Action, MenuItem, TabbedPane}

class XTabbedPane extends TabbedPane with Localization {

  private val removePageMenuItem = new MenuItem(Action(Localization.localized("x_tabbed_pane.remove_current_page")) {
    pages -= selection.page
  })

  selection.reactions += {
    case SelectionChanged(_) =>
      removePageMenuItem.peer.setText(
        if (selection.index > 0) {
          String.format(Localization.localized("x_tabbed_pane.remove_page"), selection.page.title)
        } else {
          Localization.localized("x_tabbed_pane.remove_current_page")
        }
      )
  }

  peer.setComponentPopupMenu(new JPopupMenu {add(removePageMenuItem.peer)})

  override def updateLocalizedStrings(bundle: ResourceBundle): Unit = {
    if(removePageMenuItem != null){
      removePageMenuItem.text = bundle.getString("x_tabbed_pane.remove_current_page")
    }

    repaint()
  }
}
