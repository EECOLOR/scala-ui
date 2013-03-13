package ee.ui.application.details

import ee.ui.layout.LayoutEngine
import ee.ui.layout.DefaultLayoutEngine
import ee.ui.application.Application

trait ApplicationDependencies {
    val implementationContract:ImplementationContract
    
    val layoutEngine:LayoutEngine = DefaultLayoutEngine
    
    val createPulseHandler:(Application) => PulseHandler = { application =>
      new DefaultPulseHandler(application, implementationContract.displayImplementationHandler, layoutEngine)
    }
}
