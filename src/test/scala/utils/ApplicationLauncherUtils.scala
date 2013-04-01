package utils

import ee.ui.application.ApplicationLauncher
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Promise
import ee.ui.application.Application

trait ApplicationLauncherUtils extends ThreadUtils {
  def launch(applicationLauncher: ApplicationLauncher) =
    applicationLauncher.main(Array.empty)

  def start(applicationLauncher: ApplicationLauncher, timeout: Duration = 1.seconds) = {
    val applicationPromise = Promise[Application]

    inThread {
      launch(applicationLauncher)
    } onFailure PartialFunction(applicationPromise.failure)
    
    applicationPromise.completeWith(applicationLauncher.application)

    waitFor(applicationPromise.future, s"Application did not start within $timeout", timeout)
  }

  def startAndExitApplication(applicationLauncher: ApplicationLauncher, timeout: Duration = 1.seconds) =
    start(applicationLauncher, timeout).exit()
}