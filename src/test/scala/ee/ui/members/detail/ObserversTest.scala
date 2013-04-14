package ee.ui.members.detail

import org.specs2.mutable.Specification
import scala.language.reflectiveCalls

class ObserversTest extends Specification {

  xonly
  isolated
  
  val observers = new Observers[Int => Unit] {
    def notify1() = notify (_(1))
  }
  var result = 0
  def notify1() = observers.notify1()
  
  "Observers" should {
    
     "have the ability to unsubscribe an observer" in {
      val subscription = observers observe { result = _ }
      subscription.unsubscribe()
      notify1()

      result === 0
    }

    "have the ability to disable and enable an observer" in {
      val subscription = observers observe { result = _ }
      subscription.disable()
      notify1()
      result === 0
      subscription.enable()
      notify1()
      result === 1
    }
  }
  
}