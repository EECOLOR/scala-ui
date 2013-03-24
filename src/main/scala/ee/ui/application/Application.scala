package ee.ui.application

import ee.ui.display.Window

abstract class Application {
  def start(window:Window):Unit
  
  def show(window:Window) = {}
  
  start(new Window)
}