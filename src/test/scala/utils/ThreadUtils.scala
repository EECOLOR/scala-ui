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
      waitFor(codeExecution, s"inThread with timeout of $timeout", timeout)
    }
  }

  def blockingThread[T](timeout: Duration, message:String)(code: => T): T =
    waitFor(inThread(code, Duration.Inf), message, timeout)

  def blockingThread[T](code: => T): T =
    blockingThread(500.milliseconds, "default blocking thread")(code)

  case class WaitingBoolean(initialValue: Boolean, timeout:Duration = 500.milliseconds) {
    private var _value = initialValue

    val valueChanged = new CountDownLatch(1)

    def set(value: Boolean) = {
      _value = value
      valueChanged.countDown()
    }

    def get(): Boolean = {
      blockingThread(timeout, s"Waiting for value change of $this with a timeout of $timeout") {
        valueChanged.await
      }

      _value
    }

  }
}