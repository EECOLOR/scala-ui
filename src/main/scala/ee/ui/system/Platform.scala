package ee.ui.system

import ee.ui.members.Property
import ee.ui.application.details.Launcher
import ee.ui.display.Window
import ee.ui.members.details.Remove
import ee.ui.application.Application
import ee.ui.members.ReadOnlyProperty
import ee.ui.members.ReadOnlyEvent

trait Platform {
  protected val launcher:Launcher
  protected val application:ReadOnlyProperty[Option[Application]]
  
  val implicitExit = Property(true)
  def exit():Unit = application foreach launcher.exit
}

class DefaultPlatform(protected val launcher:Launcher, applicationCreated:ReadOnlyEvent[Application]) extends Platform {

  val application = Property[Option[Application]](None)
  
  application <== applicationCreated
  
  implicitExit.change collect {
    case true if (Window.windows.isEmpty) => exit() 
  }
  
  Window.change { e =>
    e match {
      case r:Remove[_] => if (implicitExit && Window.windows.isEmpty) exit()
      case _ => // ignore
    }
  }
}