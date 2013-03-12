package ee.ui.members.details

import scala.language.higherKinds
import ee.ui.events.Observer

trait CanMapObservable[O[X] <: Observable[X], +That[X] <: Observable[X]] {
	def map[T, R](observable:O[T])(f: Observer[R] => Observer[T]):That[R] 
}

