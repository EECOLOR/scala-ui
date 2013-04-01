package ee.ui.implementation

import ee.ui.application.Application

object EmptyExitHandler extends ExitHandler {
  def exit(application:Application):Unit = {}
}