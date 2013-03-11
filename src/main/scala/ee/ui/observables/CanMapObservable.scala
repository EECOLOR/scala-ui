package ee.ui.observables

import scala.language.higherKinds

trait CanMapObservable[O[X] <: Observable[X], +That[X] <: Observable[X]] {
	def map[T, R](observable:O[T])(f: Observer[R] => Observer[T]):That[R] 
}

