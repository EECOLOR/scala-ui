package ee.ui.bindings

import ee.ui.properties.Variable

trait BindableVariable[T] extends Variable[T] {

  def <==[S](source: S)(implicit binding: BindingFactory[S, Variable[T]]) =
    binding create (source, this)
}