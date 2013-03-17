package ee.ui.members.details

import scala.language.higherKinds
import ee.ui.events.Observer
import ee.ui.members.ReadOnlyEvent

trait CanTypeObservable[O[~] <: Observable[~], That[~] <: Observable[~]] {
  def typed[T](observable: Observable[T]): That[T]
}

