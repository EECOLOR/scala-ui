package ee.ui.properties

class PropertyChangeCollector(properties: PropertyChangeHandler[_]*) {
	
  def applyChanges = properties foreach (_.applyChange)
}

class PropertyChangeBuilder[A](property: ReadOnlyProperty[A]) {
  def ~>(onNewValue: A => Unit): PropertyChangeHandler[A] =
    new PropertyChangeHandler(property, onNewValue)
}

object PropertyChangeCollector {
  implicit def propertyToPropertyChangeBuilder[A](p: ReadOnlyProperty[A]): PropertyChangeBuilder[A] =
    new PropertyChangeBuilder(p)
}

class PropertyChangeHandler[A](property: ReadOnlyProperty[A], onNewValue: A => Unit) {
  var change: Option[A] = None

  property forNewValue { v =>
    change = Some(v)
  }

  def applyChange = {
    change foreach onNewValue
    change = None
  }
}