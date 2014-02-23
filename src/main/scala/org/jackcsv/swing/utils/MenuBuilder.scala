package org.jackcsv.swing.utils

import scala.swing.{Action, MenuItem}

class MenuBuilder(title:String) {

  def --> (action: => Unit) = new MenuItem(new Action(title){
      def apply(): Unit = action
    })
}

object MenuBuilder {

  implicit def apply(title:String) = new MenuBuilder(title)

}
