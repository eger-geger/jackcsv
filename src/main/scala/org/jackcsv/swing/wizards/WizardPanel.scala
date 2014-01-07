package com.jackcsv.swing.wizards

import scala.swing._
import scala.collection.mutable.ArrayBuffer
import scala.swing.BorderPanel.Position
import scala.swing.event.Event

abstract class WizardPanel[A](val model:A) extends BorderPanel {

  private val descLabel = new Label

  val steps = new ArrayBuffer[WizardStep] with WizardStepNavigator

  this.add(new BorderPanel {
    add(Button("<<") {
      steps.movePrev()
      update()
    }, Position.West)

    add(Button(">>") {
      if (steps.current.isFinished) {
        steps.moveNext()
        update()
      }
    }, Position.East)

    add(descLabel, Position.Center)
  }, Position.South)

  protected def update() {
    if (steps.isCanceled) {
      publish(WizardCanceled(model))
      return
    }

    if (steps.isFinished) {
      publish(WizardFinished(model))
      return
    }

    descLabel.text = steps.current.description
    add(steps.current.component, Position.Center)
  }

  def title:String

}

case class WizardCanceled(value: Any) extends Event

case class WizardFinished(value: Any) extends Event