package ee.ui.application

import org.specs2.mutable.Specification
import ee.ui.display.Window
import ee.ui.display.implementation.EmptyWindowImplementationHandler
import ee.ui.display.implementation.EngineImplementationContract
import scala.language.reflectiveCalls

class ApplicationLauncherTest extends Specification {
  xonly

  def callMain(applicationLauncher: ApplicationLauncher, args: Array[String] = Array.empty) =
    applicationLauncher.main(args)

  def simpleApplication:Application with StubApplicationLauncher#StubEngine = new StubApplicationLauncher().createApplication

  "ApplicationLauncher" should {
    "call launch when main is called" in {
      val expectedArgs = Array("test")
      var launchArgs = Array.empty[String]
      
      val applicationLauncher = new ApplicationLauncher {
        def createApplication() = ???
        def launch(createApplication: () => Application with Engine, args: Array[String]) =
          launchArgs = args
      }
      callMain(applicationLauncher, expectedArgs)

      launchArgs === expectedArgs
    }
    "call launch with a specific create application method" in {

      var createdApplication = simpleApplication

      val applicationLauncher = new StubApplicationLauncher {

        override def launch(createApplication: () => Application with Engine, args: Array[String]) =
          createdApplication = createApplication()
      }

      callMain(applicationLauncher)

      createdApplication === applicationLauncher.application
    }
    "have an event that fires when an application is created" in {

      val applicationLauncher = new StubApplicationLauncher {

        override def launch(createApplication: () => Application with Engine, args: Array[String]) =
          createApplication()
      }

      var createdApplication = simpleApplication

      applicationLauncher.applicationCreated {
        createdApplication = _
      }

      callMain(applicationLauncher)

      createdApplication === applicationLauncher.application
    }
    "start the application" in {
      val applicationLauncher = new StubApplicationLauncher {

        override def launch(createApplication: () => Application with Engine, args: Array[String]) = {
          val application = createApplication()
          application.start()
        }
      }
      callMain(applicationLauncher)

      applicationLauncher.application.isStarted === true
    }
    "have an engine type which is a subtype of EngineImplementationContract" in {
      def x(y:ApplicationLauncher#Engine) = y:EngineImplementationContract
      ok
    }
  }
}