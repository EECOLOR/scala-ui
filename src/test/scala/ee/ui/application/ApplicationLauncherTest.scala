package ee.ui.application

import org.specs2.mutable.Specification
import ee.ui.display.Window
import ee.ui.display.implementation.EmptyWindowImplementationHandler

class ApplicationLauncherTest extends Specification {
  xonly

  def callMain(applicationLauncher: ApplicationLauncher, args: Array[String] = Array.empty) =
    applicationLauncher.main(args)

  def simpleApplication:Application = new StubApplication

  "ApplicationLauncher" should {
    "call launch when main is called" in {
      val expectedArgs = Array("test")
      var launchArgs = Array.empty[String]
      
      val applicationLauncher = new ApplicationLauncher {
        def createApplication() = ???
        def launch(createApplication: () => Application, args: Array[String]) =
          launchArgs = args
      }
      callMain(applicationLauncher, expectedArgs)

      launchArgs === expectedArgs
    }
    "call launch with a specific create application method" in {

      val expectedApplication = simpleApplication
      var createdApplication = simpleApplication

      val applicationLauncher = new ApplicationLauncher {
        def createApplication() = expectedApplication
        def launch(createApplication: () => Application, args: Array[String]) =
          createdApplication = createApplication()
      }

      callMain(applicationLauncher)

      createdApplication === expectedApplication
    }
    "have an event that fires when an application is created" in {
      val expectedApplication = simpleApplication

      val applicationLauncher = new ApplicationLauncher {
        def createApplication() = expectedApplication
        def launch(createApplication: () => Application, args: Array[String]) =
          createApplication()
      }

      var createdApplication = simpleApplication

      applicationLauncher.applicationCreated {
        createdApplication = _
      }

      callMain(applicationLauncher)

      createdApplication === expectedApplication
    }
  }
}