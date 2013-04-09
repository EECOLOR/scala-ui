package ee.ui.members

import ee.ui.members.detail.Subscription
import ee.ui.system.AccessRestriction

trait ReadOnlySignal { self =>
  def observe(observer: => Unit): Subscription
  def apply(observer: => Unit): Subscription = observe(observer)

  protected def fire(): Unit

  def |(other: ReadOnlySignal): ReadOnlySignal =
    new ReadOnlySignal {

      def observe(observer: => Unit): Subscription = {
        val subscription1 = self observe observer
        val subscription2 = other observe observer

        new Subscription {
          def unsubscribe() = {
            subscription1.unsubscribe()
            subscription2.unsubscribe()
          }
          def enable() = {
            subscription1.enable()
            subscription2.enable()
          }
          def disable() = {
            subscription1.disable()
            subscription2.disable()
          }
        }
      }

      protected def fire(): Unit =
        throw new UnsupportedOperationException("The fire method is not supported on a combined instance")

    }
}

object ReadOnlySignal {
  def apply(): ReadOnlySignal = Signal()

  def fire(signal: ReadOnlySignal)(implicit ev: AccessRestriction) = signal.fire()
}