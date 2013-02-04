package ee.ui.properties

import ee.ui.observable.ObservableValue
import ee.ui.observable.Subscription
import ee.ui.observable.Observable
import scala.language.higherKinds
import ee.ui.observable.WrappedSubscription
import scala.language.implicitConversions

trait BindableValue[T] {

  def set(value: T): Unit

  def bindTo[S, R](observableValue: ObservableValue[S])(
    implicit factory: BindingFactory[ObservableValue[S], BindableValue[T], R]): R =
    factory create (observableValue, this)

  def <==[S, R](observableValue: ObservableValue[S])(
    implicit factory: BindingFactory[ObservableValue[S], BindableValue[T], R]): R =
    bindTo(observableValue)

}

trait BindingFactory[S, T, R] {
  def create(source: S, target: T): R
}

trait LowPriorityBindingFactory {
  implicit def incompleteBinding[S, T] = new BindingFactory[ObservableValue[S], BindableValue[T], IncompleteBinding[S, T]] {
    def create(source: ObservableValue[S], target: BindableValue[T]) = new IncompleteBinding(source, target)
  }
}

object BindingFactory extends LowPriorityBindingFactory {
  implicit def completeBinding[T] = new BindingFactory[ObservableValue[T], BindableValue[T], CompleteBinding[T]] {
    def create(source: ObservableValue[T], target: BindableValue[T]) = new CompleteBinding(source, target)
  }
}

trait Binding[S, T] {
  def source: ObservableValue[S]
  def target: BindableValue[T]
}

class CompleteBinding[T](val source: ObservableValue[T], val target: BindableValue[T]) extends Binding[T, T] {

  protected val subscription = source foreach target.set _

  def unbind(): Unit = subscription.unsubscribe()
}

class IncompleteBinding[S, T](val source: ObservableValue[S], val target: BindableValue[T]) extends Binding[S, T] {
	 def map[R, That](f: S => R)(
	     implicit factory:BindingFactory[ObservableValue[R], BindableValue[T], That]):That = {
	       
	   factory create (ObservableValue.mapped(source, f), target)
	 }
}