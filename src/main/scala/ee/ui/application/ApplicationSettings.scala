package ee.ui.application

import ee.ui.members.Property
import ee.ui.members.ReadOnlyEvent
import scala.concurrent.Future

class ApplicationSettings(applicationCreated:Future[Application]) {
  private val _implicitExit = Property(true)
  def implicitExit = _implicitExit
  def implicitExit_=(value:Boolean) = _implicitExit.value = value
  
}