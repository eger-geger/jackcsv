package org.jackcsv.swing.panels

import java.util.ResourceBundle
import org.jackcsv.{Logger, NavigableBuffer}
import org.jackcsv.i10n.{SwingLocalization, Localization}
import scala.collection.mutable.ListBuffer
import scala.swing._
import scala.swing.event.Event

class WizardPanel extends GridBagPanel with Localization with Logger {

  private val _components = new NavigableBuffer[Component]

  private val contentPanel = new BorderPanel {
    def content: Component = contents.applyOrElse(0, (_: Int) => null)

    def content_=(value: Component) {
      _contents.clear()

      add(value, BorderPanel.Position.Center)

      revalidate()

      WizardPanel.this.publish(ComponentLoaded(value))
    }
  }

  val controllers = new PartialFunction[Component, Unit] {

    private val _controllers = new ListBuffer[PartialFunction[Component, Unit]]

    override def apply(value: Component): Unit = {
      for (h <- _controllers if h isDefinedAt value) h apply value
    }

    override def isDefinedAt(x: Component): Boolean = _controllers.exists(_ isDefinedAt x)

    def +=(action: PartialFunction[Component, Unit]) = _controllers += action
  }

  private val finishBtn = SwingLocalization.createLocalizedButton("wizard_panel.finish") {
    try {
      _components.curr.foreach(controllers.apply)

      WizardPanel.this.publish(WizardFinished(WizardPanel.this))
    } catch {
      case th: Throwable =>
        th.getStackTrace.foreach(se => log(se.toString))
        WizardPanel.this.publish(ExceptionThrown(WizardPanel.this, th))
    }
  }

  private val cancelBtn = SwingLocalization.createLocalizedButton("wizard_panel.cancel") {
    WizardPanel.this.publish(WizardCanceled(WizardPanel.this))
  }

  private val nextBtn = SwingLocalization.createLocalizedButton("wizard_panel.next") {
    try {
      _components.curr.foreach(controllers.apply)
      _components.next.foreach(contentPanel.content = _)
    } catch {
      case th: Throwable =>
        WizardPanel.this.publish(ExceptionThrown(WizardPanel.this, th))
    }
  }

  private val prevBtn = SwingLocalization.createLocalizedButton("wizard_panel.prev") {
    _components.prev.foreach(contentPanel.content = _)
  }

  reactions += {
    case _: ComponentLoaded =>
      nextBtn.enabled = _components.hasNext
      prevBtn.enabled = _components.hasPrev
      finishBtn.enabled = !_components.hasNext

    case ExceptionThrown(wizardPanel, throwable) =>
      Dialog.showMessage(wizardPanel, throwable.getMessage, Localization.localized("wizard_panel.error_dialog.title"))
  }

  add(contentPanel, new Constraints() {
    grid = (0, 0)
    weighty = 10
    gridwidth = 4
    fill = GridBagPanel.Fill.Both
    insets = WizardPanel.defaultInsets
  })

  add(cancelBtn, new Constraints {
    grid = (0, 1)
    weighty = 0.5
    weightx = 1
    insets = WizardPanel.defaultInsets
    fill = GridBagPanel.Fill.Horizontal
  })

  add(prevBtn, new Constraints {
    grid = (1, 1)
    weighty = 0.5
    weightx = 1
    insets = WizardPanel.defaultInsets
    fill = GridBagPanel.Fill.Horizontal
  })

  add(nextBtn, new Constraints {
    grid = (2, 1)
    weighty = 0.5
    weightx = 1
    insets = WizardPanel.defaultInsets
    fill = GridBagPanel.Fill.Horizontal
  })

  add(finishBtn, new Constraints {
    grid = (3, 1)
    weighty = 0.5
    weightx = 1
    insets = WizardPanel.defaultInsets
    fill = GridBagPanel.Fill.Horizontal
  })

  def +=(value: Component) = {
    _components += value
    _components.curr.foreach(contentPanel.content = _)
  }

  def components: Seq[Component] = _components

  def components_=(value: Seq[Component]) = {
    _components.clear()
    _components ++= value
    _components.curr.foreach(contentPanel.content = _)
  }

  override def updateLocalizedStrings(bundle: ResourceBundle): Unit = this.repaint()
}

object WizardPanel {

  val defaultInsets = new Insets(5, 5, 5, 5)

}

trait WizardPanelEvent extends Event {
  val wizardPanel: WizardPanel
}

case class ExceptionThrown(wizardPanel: WizardPanel, throwable: Throwable) extends Event

case class ComponentLoaded(component: Component) extends Event

case class WizardCanceled(wizardPanel: WizardPanel) extends WizardPanelEvent

case class WizardFinished(wizardPanel: WizardPanel) extends WizardPanelEvent