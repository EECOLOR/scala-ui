package ee.ui.members

import org.specs2.mutable.Specification
import utils.SubtypeTest
import ee.ui.members.detail.Observers

object SignalTest extends Specification {

  xonly

  "Signal" should {

    "extend the correct types" in {
      SubtypeTest[Signal <:< ReadOnlySignal with Observers[() => Unit]]
    }

    "be created easily" in {
      Signal()
      ok
    }
    
    "have the ability to fire" in {
      val signal = Signal()
      var fired = false

      signal.observe {
        fired = true
      }
      signal.fire()

      fired
    }
  }
}