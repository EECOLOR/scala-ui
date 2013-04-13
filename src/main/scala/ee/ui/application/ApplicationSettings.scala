package ee.ui.application

import ee.ui.members.Property

class ApplicationSettings {
  
  private val _implicitExit = Property(true)
  def implicitExit = _implicitExit
  def implicitExit_=(value:Boolean) = _implicitExit.value = value
  
}