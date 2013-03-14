package ee.ui.application

import ee.ui.display.implementation.WindowImplementationHandler
import ee.ui.system.Platform

trait EngineDependencies {
  protected implicit def windowImplementationHandler:WindowImplementationHandler
  protected implicit def platform:Platform
}