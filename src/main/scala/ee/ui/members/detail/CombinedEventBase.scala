package ee.ui.members.detail

import ee.ui.members.ReadOnlyEvent

abstract class CombinedEventBase[A <: C, B <: C, C](a: ReadOnlyEvent[A], b: ReadOnlyEvent[B]) extends ReadOnlyEvent[C] {

  def observe(observer: C => Unit): Subscription = {
    val aSubscription = a observe observer
    val bSubscription = b observe observer

    new Subscription {
      def disable(): Unit = {
        aSubscription.disable()
        bSubscription.disable()
      }

      def enable(): Unit = {
        aSubscription.enable()
        bSubscription.enable()
      }

      def unsubscribe(): Unit = {
        aSubscription.unsubscribe()
        bSubscription.unsubscribe()
      }
    }
  }

}