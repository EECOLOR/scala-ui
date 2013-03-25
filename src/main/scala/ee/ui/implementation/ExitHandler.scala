package ee.ui.implementation

import ee.ui.application.Application

trait ExitHandler {
  def exit(application:Application):Unit
}