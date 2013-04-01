package utils

import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import java.util.concurrent.CountDownLatch

trait ThreadUtils {
  def waitFor[T](f: Future[T], message:String, timeout: Duration = 500.milliseconds):T =
    try {
      waitFor(f, timeout)
    } catch {
      case e:TimeoutException => throw new RuntimeException("Timeout: " + message, e)
    }
    
  def waitFor[T](f: Future[T], timeout: Duration = 500.milliseconds):T =
    Await.result(f, timeout)

  def inThread[T](code: => T, timeout: Duration = 1.second): Future[T] = {
    val codeExecution = future(code)
    future {
      // ensures the code executes within the given timeout
      waitFor(codeExecution, timeout)
    }
  }

  def blockingThread[T](timeout: Duration)(code: => T): T =
    waitFor(inThread(code, timeout), Duration.Inf)

  def blockingThread[T](code: => T): T =
    blockingThread(500.milliseconds)(code)

  case class WaitingBoolean(initialValue: Boolean) {
    private var _value = initialValue

    val valueChanged = new CountDownLatch(1)

    def set(value: Boolean) = {
      _value = value
      valueChanged.countDown()
    }

    def get(): Boolean = {
      blockingThread {
        valueChanged.await
      }

      _value
    }

  }
}