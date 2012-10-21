package ee.ui.properties

class PropertyChangeCollector(properties: PropertyChangeHandler*) {

  def applyChanges: Unit = properties foreach (_.applyChange)
}

object PropertyChangeCollector {
  implicit def propertyToPropertyChangeBuilder1[A](p: ReadOnlyProperty[A]): PropertyChangeBuilder1[A] =
    new PropertyChangeBuilder1(p)

  implicit def tupleToPropertyChangeBuilder2[A, B](properties: (ReadOnlyProperty[A], ReadOnlyProperty[B])): PropertyChangeBuilder2[A, B] =
    new PropertyChangeBuilder2(properties)
}

class PropertyChangeBuilder1[A](property: ReadOnlyProperty[A]) {
  def ~>(onNewValue: A => Unit): PropertyChangeHandler1[A] =
    new PropertyChangeHandler1(property, onNewValue)
}

class PropertyChangeBuilder2[A, B](properties: (ReadOnlyProperty[A], ReadOnlyProperty[B])) {
  val (p1, p2) = properties

  def ~>(onNewValue: (A, B) => Unit): PropertyChangeHandler2[A, B] =
    new PropertyChangeHandler2(p1, p2, onNewValue)
}

abstract class PropertyChangeHandler(properties: Iterable[ReadOnlyProperty[_]]) {
  def handleChanges: Unit

  var valueChanged = true

  properties foreach {
    _ forNewValue { v =>
      valueChanged = true
    }
  }

  def applyChange = {
    if (valueChanged) {
      handleChanges
      valueChanged = false
    }
  }
}

class PropertyChangeHandler1[A](property: ReadOnlyProperty[A], onNewValue: A => Unit)
  extends PropertyChangeHandler(Iterable(property)) {

  def handleChanges = onNewValue(property.value)
}

class PropertyChangeHandler2[A, B](p1: ReadOnlyProperty[A], p2: ReadOnlyProperty[B], onNewValue: (A, B) => Unit)
  extends PropertyChangeHandler(Iterable(p1, p2)) {

  def handleChanges = onNewValue(p1.value, p2.value)
}