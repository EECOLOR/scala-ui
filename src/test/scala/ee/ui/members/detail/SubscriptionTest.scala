package ee.ui.members.detail

import org.specs2.mutable.Specification
import utils.SignatureTest

class SubscriptionTest extends Specification {

  xonly

  "Subscription" should {

    "have an unsubscribe method" in {
      SignatureTest[Subscription, Unit](_.unsubscribe())
    }

    "have a disable method" in {
      SignatureTest[Subscription, Unit](_.disable())
    }

    "have an enable method" in {
      SignatureTest[Subscription, Unit](_.enable())
    }
  }
}