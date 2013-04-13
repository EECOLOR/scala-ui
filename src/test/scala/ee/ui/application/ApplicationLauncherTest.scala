package ee.ui.application

import org.specs2.mutable.Specification
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import utils.SubtypeTest
import ee.ui.implementation.EngineImplementationContract

object ApplicationLauncherTest extends Specification {
  
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
    
    "have an engine type which is a subtype of EngineImplementationContract" in {
      SubtypeTest[ApplicationLauncher#Engine <:< EngineImplementationContract]
    }
  }
}