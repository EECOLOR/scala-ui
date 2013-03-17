package ee.ui.members.details

import scala.language.higherKinds

trait BindableVariable[T] extends Variable[T] {

  def <==[OS[~], S](source:OS[S])(implicit toBindingSource: OS[S] => BindingSource[OS, S], factory: BindingFactory[OS, Variable[T], S]) = 
    toBindingSource(source).create(this)

  def <==[OS[~], S](bindingSource: BindingSource[OS, S])(implicit factory: BindingFactory[OS, Variable[T], S]) =
    bindingSource.create(this)
    
    

}
