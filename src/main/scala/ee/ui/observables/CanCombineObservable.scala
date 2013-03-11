package ee.ui.observables

import scala.language.higherKinds

trait CanCombineObservable[O1[X] <: Observable[X], O2[X] <: Observable[X], +That[X] <: Observable[X]] {
  def combine[R, T1 <: R, T2 <: R](observable1:O1[T1], observable2:O2[T2]):That[R]
}

