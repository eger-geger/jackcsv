package org.jackcsv.swing.panels

import scala.swing._
import scala.swing.event.Event

class WizardPanel extends GridBagPanel {

  private val infoLabel = new Label

  private val contentPanel = new BorderPanel {
    def replaceContentWith(component: Component) {
      _contents.clear()

      add(component, BorderPanel.Position.Center)

      revalidate()
    }
  }

  private val finishBtn = Button("Finish"){
    WizardPanel.this.publish(WizardFinished(WizardPanel.this))
  }

  private val cancelBtn = Button("Cancel"){
    WizardPanel.this.publish(WizardCanceled(WizardPanel.this))
  }

  private val nextBtn = Button("Next") {
    WizardPanel.this.publish(NextWizardStep(WizardPanel.this))
  }

  private val prevBtn = Button("Prev") {
    WizardPanel.this.publish(PrevWizardStep(WizardPanel.this))
  }

  add(contentPanel, new Constraints() {
    grid = (0, 0)
    weighty = 10
    gridwidth = 3
    fill = GridBagPanel.Fill.Both
    insets = WizardPanel.defaultInsets
  })

  add(infoLabel, new Constraints {
    grid = (1, 1)
    weighty = 0.5
    weightx = 2
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

  add(prevBtn, new Constraints {
    grid = (0, 1)
    weighty = 0.5
    weightx = 1
    insets = WizardPanel.defaultInsets
    fill = GridBagPanel.Fill.Horizontal
  })

  def enableFinish = finishBtn.enabled
  def enableFinish_=(value:Boolean) = finishBtn.enabled = value

  def enableNext = nextBtn.enabled
  def enableNext_=(value: Boolean) = nextBtn.enabled = value

  def enablePrev = prevBtn.enabled
  def enablePrev_=(value: Boolean) = prevBtn.enabled = value

  def content:Component = contentPanel.contents.headOption.orNull
  def content_=(value:Component) = contentPanel.replaceContentWith(value)

  def infoText = infoLabel.text
  def infoText_=(value:String) = infoLabel.text = value

}

object WizardPanel {

  val defaultInsets = new Insets(5, 5, 5, 5)

}

trait WizardPanelEvent extends Event {
  val wizardPanel:WizardPanel
}

case class WizardCanceled(wizardPanel:WizardPanel) extends WizardPanelEvent
case class WizardFinished(wizardPanel:WizardPanel) extends WizardPanelEvent
case class NextWizardStep(wizardPanel:WizardPanel) extends WizardPanelEvent
case class PrevWizardStep(wizardPanel:WizardPanel) extends WizardPanelEvent