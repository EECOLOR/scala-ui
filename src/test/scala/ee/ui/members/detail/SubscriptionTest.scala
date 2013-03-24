package ee.ui.members.detail

import org.specs2.mutable.Specification

class SubscriptionTest extends Specification {
  
  xonly
  
  "Subscription" should {
    "have an unsubscribe method" in {
      val s:Subscription = new Subscription {
        def unsubscribe() = {}
      }
      s.unsubscribe()
    }
  }
}