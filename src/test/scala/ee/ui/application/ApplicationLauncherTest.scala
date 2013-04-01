package ee.ui.application

import org.specs2.mutable.Specification
import ee.ui.display.Window
import ee.ui.implementation.EngineImplementationContract
import scala.language.reflectiveCalls
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.util.Success
import scala.util.Failure
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import utils.TestUtils
import utils.TypeTest
import utils.SubtypeTest

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

      var actualApplication = simpleApplication

      val applicationLauncher = new StubApplicationLauncher {

        override def launch(createApplication: () => Application with Engine, args: Array[String]) =
          actualApplication = createApplication()
      }

      callMain(applicationLauncher)

      actualApplication === applicationLauncher.createdApplication
    }
    "have a future that completes when an application is created" in {

      val applicationLauncher = new StubApplicationLauncher {

        override def launch(createApplication: () => Application with Engine, args: Array[String]) =
          createApplication()
      }

      val applicationFuture:Future[Application] = applicationLauncher.application

      callMain(applicationLauncher)

      Await.result(applicationFuture, Duration.Inf) === applicationLauncher.createdApplication
    }
    "start the application" in {
      var applicationStarted = false
      
      val applicationLauncher = new StubApplicationLauncher {

        override def createApplication() = new TestApplication with Engine {
          override def start(window:Window):Unit = applicationStarted = true 
        }
        
        override def launch(createApplication: () => Application with Engine, args: Array[String]) = {
          val application = createApplication()
          application.start()
        }
      }
      callMain(applicationLauncher)

      applicationStarted === true
    }
    "have an engine type which is a subtype of EngineImplementationContract" in {
      SubtypeTest[ApplicationLauncher#Engine <:< EngineImplementationContract]
    }
  }
}