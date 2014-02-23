package org.jackcsv.swing

import org.jackcsv.swing.utils.MenuBuilder
import MenuBuilder._
import javax.swing.JPopupMenu
import org.jackcsv.Logger
import scala.swing.TabbedPane.Page
import scala.swing.event.SelectionChanged
import scala.swing.{Component, TabbedPane}

class XTabbedPane extends TabbedPane with Logger {

  private val removePageMenuItem = "Remove Current Page" --> {
    pages -= selection.page
  }

  selection.reactions += {
    case SelectionChanged(_) if selection.index > 0 =>
      removePageMenuItem.peer.setText(s"Remove [${selection.page.title}] Page")
    case SelectionChanged(_) =>
      removePageMenuItem.peer.setText("Remove Current Page")
  }

  peer.setComponentPopupMenu(new JPopupMenu {add(removePageMenuItem.peer)})

}
