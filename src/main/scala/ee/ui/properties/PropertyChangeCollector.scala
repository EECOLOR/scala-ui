package ee.ui.properties

import language.implicitConversions

class PropertyChangeCollector(properties: PropertyChangeHandler*) {

  def applyChanges: Unit = properties foreach (_.applyChanges)
}

abstract class PropertyChangeHandler(properties: Iterable[ReadOnlyProperty[_]]) {
  def handleChanges: Unit

  var valueChanged = true

  properties foreach {
    _ forNewValue { v =>
      valueChanged = true
    }
  }

  def applyChanges = {
    if (valueChanged) {
      handleChanges
      valueChanged = false
    }
  }
}

object PropertyChangeCollector {
  implicit def propertyToPropertyChangeBuilder1[A](p: ReadOnlyProperty[A]) =
    new PropertyChangeBuilder1(p)

  implicit def tupleToPropertyChangeBuilder2[A, B](properties: (ReadOnlyProperty[A], ReadOnlyProperty[B])) =
    new PropertyChangeBuilder2(properties)

  implicit def tupleToPropertyChangeBuilder3[A, B, C](properties: (ReadOnlyProperty[A], ReadOnlyProperty[B], ReadOnlyProperty[C])) =
    new PropertyChangeBuilder3(properties)

  implicit def tupleToPropertyChangeBuilder4[A, B, C, D](properties: (ReadOnlyProperty[A], ReadOnlyProperty[B], ReadOnlyProperty[C], ReadOnlyProperty[D])) =
    new PropertyChangeBuilder4(properties)

  implicit def tupleToPropertyChangeBuilder5[A, B, C, D, E](properties: (ReadOnlyProperty[A], ReadOnlyProperty[B], ReadOnlyProperty[C], ReadOnlyProperty[D], ReadOnlyProperty[E])) =
    new PropertyChangeBuilder5(properties)

  implicit def tupleToPropertyChangeBuilder6[A, B, C, D, E, F](properties: (ReadOnlyProperty[A], ReadOnlyProperty[B], ReadOnlyProperty[C], ReadOnlyProperty[D], ReadOnlyProperty[E], ReadOnlyProperty[F])) =
    new PropertyChangeBuilder6(properties)

  implicit def tupleToPropertyChangeBuilder7[A, B, C, D, E, F, G](properties: (ReadOnlyProperty[A], ReadOnlyProperty[B], ReadOnlyProperty[C], ReadOnlyProperty[D], ReadOnlyProperty[E], ReadOnlyProperty[F], ReadOnlyProperty[G])) =
    new PropertyChangeBuilder7(properties)

  implicit def tupleToPropertyChangeBuilder8[A, B, C, D, E, F, G, H](properties: (ReadOnlyProperty[A], ReadOnlyProperty[B], ReadOnlyProperty[C], ReadOnlyProperty[D], ReadOnlyProperty[E], ReadOnlyProperty[F], ReadOnlyProperty[G], ReadOnlyProperty[H])) =
    new PropertyChangeBuilder8(properties)

  implicit def tupleToPropertyChangeBuilder9[A, B, C, D, E, F, G, H, I](properties: (ReadOnlyProperty[A], ReadOnlyProperty[B], ReadOnlyProperty[C], ReadOnlyProperty[D], ReadOnlyProperty[E], ReadOnlyProperty[F], ReadOnlyProperty[G], ReadOnlyProperty[H], ReadOnlyProperty[I])) =
    new PropertyChangeBuilder9(properties)
  
  implicit def tupleToPropertyChangeBuilder10[A, B, C, D, E, F, G, H, I, J](properties: (ReadOnlyProperty[A], ReadOnlyProperty[B], ReadOnlyProperty[C], ReadOnlyProperty[D], ReadOnlyProperty[E], ReadOnlyProperty[F], ReadOnlyProperty[G], ReadOnlyProperty[H], ReadOnlyProperty[I], ReadOnlyProperty[J])) =
  new PropertyChangeBuilder10(properties)
}

class PropertyChangeBuilder1[A](property: ReadOnlyProperty[A]) {
  def ~>(onNewValue: A => Unit) =
    new PropertyChangeHandler1(property, onNewValue)
}

class PropertyChangeBuilder2[A, B](properties: (ReadOnlyProperty[A], ReadOnlyProperty[B])) {
  val (p1, p2) = properties

  def ~>(onNewValue: (A, B) => Unit) =
    new PropertyChangeHandler2(p1, p2, onNewValue)
}

class PropertyChangeBuilder3[A, B, C](properties: (ReadOnlyProperty[A], ReadOnlyProperty[B], ReadOnlyProperty[C])) {
  val (p1, p2, p3) = properties

  def ~>(onNewValue: (A, B, C) => Unit) =
    new PropertyChangeHandler3(p1, p2, p3, onNewValue)
}

class PropertyChangeBuilder4[A, B, C, D](properties: (ReadOnlyProperty[A], ReadOnlyProperty[B], ReadOnlyProperty[C], ReadOnlyProperty[D])) {
  val (p1, p2, p3, p4) = properties

  def ~>(onNewValue: (A, B, C, D) => Unit) =
    new PropertyChangeHandler4(p1, p2, p3, p4, onNewValue)
}

class PropertyChangeBuilder5[A, B, C, D, E](properties: (ReadOnlyProperty[A], ReadOnlyProperty[B], ReadOnlyProperty[C], ReadOnlyProperty[D], ReadOnlyProperty[E])) {
  val (p1, p2, p3, p4, p5) = properties

  def ~>(onNewValue: (A, B, C, D, E) => Unit) =
    new PropertyChangeHandler5(p1, p2, p3, p4, p5, onNewValue)
}

class PropertyChangeBuilder6[A, B, C, D, E, F](properties: (ReadOnlyProperty[A], ReadOnlyProperty[B], ReadOnlyProperty[C], ReadOnlyProperty[D], ReadOnlyProperty[E], ReadOnlyProperty[F])) {
  val (p1, p2, p3, p4, p5, p6) = properties

  def ~>(onNewValue: (A, B, C, D, E, F) => Unit) =
    new PropertyChangeHandler6(p1, p2, p3, p4, p5, p6, onNewValue)
}

class PropertyChangeBuilder7[A, B, C, D, E, F, G](properties: (ReadOnlyProperty[A], ReadOnlyProperty[B], ReadOnlyProperty[C], ReadOnlyProperty[D], ReadOnlyProperty[E], ReadOnlyProperty[F], ReadOnlyProperty[G])) {
  val (p1, p2, p3, p4, p5, p6, p7) = properties

  def ~>(onNewValue: (A, B, C, D, E, F, G) => Unit) =
    new PropertyChangeHandler7(p1, p2, p3, p4, p5, p6, p7, onNewValue)
}

class PropertyChangeBuilder8[A, B, C, D, E, F, G, H](properties: (ReadOnlyProperty[A], ReadOnlyProperty[B], ReadOnlyProperty[C], ReadOnlyProperty[D], ReadOnlyProperty[E], ReadOnlyProperty[F], ReadOnlyProperty[G], ReadOnlyProperty[H])) {
  val (p1, p2, p3, p4, p5, p6, p7, p8) = properties

  def ~>(onNewValue: (A, B, C, D, E, F, G, H) => Unit) =
    new PropertyChangeHandler8(p1, p2, p3, p4, p5, p6, p7, p8, onNewValue)
}

class PropertyChangeBuilder9[A, B, C, D, E, F, G, H, I](properties: (ReadOnlyProperty[A], ReadOnlyProperty[B], ReadOnlyProperty[C], ReadOnlyProperty[D], ReadOnlyProperty[E], ReadOnlyProperty[F], ReadOnlyProperty[G], ReadOnlyProperty[H], ReadOnlyProperty[I])) {
  val (p1, p2, p3, p4, p5, p6, p7, p8, p9) = properties

  def ~>(onNewValue: (A, B, C, D, E, F, G, H, I) => Unit) =
    new PropertyChangeHandler9(p1, p2, p3, p4, p5, p6, p7, p8, p9, onNewValue)
}

class PropertyChangeBuilder10[A, B, C, D, E, F, G, H, I, J](properties: (ReadOnlyProperty[A], ReadOnlyProperty[B], ReadOnlyProperty[C], ReadOnlyProperty[D], ReadOnlyProperty[E], ReadOnlyProperty[F], ReadOnlyProperty[G], ReadOnlyProperty[H], ReadOnlyProperty[I], ReadOnlyProperty[J])) {
	val (p1, p2, p3, p4, p5, p6, p7, p8, p9, p10) = properties
			
			def ~>(onNewValue: (A, B, C, D, E, F, G, H, I, J) => Unit) =
			new PropertyChangeHandler10(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, onNewValue)
}

class PropertyChangeHandler1[A](
  p1: ReadOnlyProperty[A],
  onNewValue: A => Unit)
  extends PropertyChangeHandler(Iterable(p1)) {

  def handleChanges = onNewValue(p1.value)
}

class PropertyChangeHandler2[A, B](
  p1: ReadOnlyProperty[A],
  p2: ReadOnlyProperty[B],
  onNewValue: (A, B) => Unit)
  extends PropertyChangeHandler(Iterable(p1, p2)) {

  def handleChanges = onNewValue.tupled(p1.value, p2.value)
}

class PropertyChangeHandler3[A, B, C](
  p1: ReadOnlyProperty[A],
  p2: ReadOnlyProperty[B],
  p3: ReadOnlyProperty[C],
  onNewValue: (A, B, C) => Unit)
  extends PropertyChangeHandler(Iterable(p1, p2, p3)) {

  def handleChanges = onNewValue(p1.value, p2.value, p3.value)
}

class PropertyChangeHandler4[A, B, C, D](
  p1: ReadOnlyProperty[A],
  p2: ReadOnlyProperty[B],
  p3: ReadOnlyProperty[C],
  p4: ReadOnlyProperty[D],
  onNewValue: (A, B, C, D) => Unit)
  extends PropertyChangeHandler(Iterable(p1, p2, p3, p4)) {

  def handleChanges = onNewValue(p1.value, p2.value, p3.value, p4.value)
}

class PropertyChangeHandler5[A, B, C, D, E](
  p1: ReadOnlyProperty[A],
  p2: ReadOnlyProperty[B],
  p3: ReadOnlyProperty[C],
  p4: ReadOnlyProperty[D],
  p5: ReadOnlyProperty[E],
  onNewValue: (A, B, C, D, E) => Unit)
  extends PropertyChangeHandler(Iterable(p1, p2, p3, p4, p5)) {

  def handleChanges = onNewValue(p1.value, p2.value, p3.value, p4.value, p5.value)
}

class PropertyChangeHandler6[A, B, C, D, E, F](
  p1: ReadOnlyProperty[A],
  p2: ReadOnlyProperty[B],
  p3: ReadOnlyProperty[C],
  p4: ReadOnlyProperty[D],
  p5: ReadOnlyProperty[E],
  p6: ReadOnlyProperty[F],
  onNewValue: (A, B, C, D, E, F) => Unit)
  extends PropertyChangeHandler(Iterable(p1, p2, p3, p4, p5, p6)) {

  def handleChanges = onNewValue(p1.value, p2.value, p3.value, p4.value, p5.value, p6.value)
}

class PropertyChangeHandler7[A, B, C, D, E, F, G](
  p1: ReadOnlyProperty[A],
  p2: ReadOnlyProperty[B],
  p3: ReadOnlyProperty[C],
  p4: ReadOnlyProperty[D],
  p5: ReadOnlyProperty[E],
  p6: ReadOnlyProperty[F],
  p7: ReadOnlyProperty[G],
  onNewValue: (A, B, C, D, E, F, G) => Unit)
  extends PropertyChangeHandler(Iterable(p1, p2, p3, p4, p5, p6, p7)) {

  def handleChanges = onNewValue(p1.value, p2.value, p3.value, p4.value, p5.value, p6.value, p7.value)
}

class PropertyChangeHandler8[A, B, C, D, E, F, G, H](
  p1: ReadOnlyProperty[A],
  p2: ReadOnlyProperty[B],
  p3: ReadOnlyProperty[C],
  p4: ReadOnlyProperty[D],
  p5: ReadOnlyProperty[E],
  p6: ReadOnlyProperty[F],
  p7: ReadOnlyProperty[G],
  p8: ReadOnlyProperty[H],
  onNewValue: (A, B, C, D, E, F, G, H) => Unit)
  extends PropertyChangeHandler(Iterable(p1, p2, p3, p4, p5, p6, p7, p8)) {

  def handleChanges = onNewValue(p1.value, p2.value, p3.value, p4.value, p5.value, p6.value, p7.value, p8.value)
}

class PropertyChangeHandler9[A, B, C, D, E, F, G, H, I](
  p1: ReadOnlyProperty[A],
  p2: ReadOnlyProperty[B],
  p3: ReadOnlyProperty[C],
  p4: ReadOnlyProperty[D],
  p5: ReadOnlyProperty[E],
  p6: ReadOnlyProperty[F],
  p7: ReadOnlyProperty[G],
  p8: ReadOnlyProperty[H],
  p9: ReadOnlyProperty[I],
  onNewValue: (A, B, C, D, E, F, G, H, I) => Unit)
  extends PropertyChangeHandler(Iterable(p1, p2, p3, p4, p5, p6, p7, p8, p9)) {

  def handleChanges = onNewValue(p1.value, p2.value, p3.value, p4.value, p5.value, p6.value, p7.value, p8.value, p9.value)
}

class PropertyChangeHandler10[A, B, C, D, E, F, G, H, I, J](
		p1: ReadOnlyProperty[A],
		p2: ReadOnlyProperty[B],
		p3: ReadOnlyProperty[C],
		p4: ReadOnlyProperty[D],
		p5: ReadOnlyProperty[E],
		p6: ReadOnlyProperty[F],
		p7: ReadOnlyProperty[G],
		p8: ReadOnlyProperty[H],
		p9: ReadOnlyProperty[I],
		p10: ReadOnlyProperty[J],
		onNewValue: (A, B, C, D, E, F, G, H, I, J) => Unit)
		extends PropertyChangeHandler(Iterable(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10)) {
	
	def handleChanges = onNewValue(p1.value, p2.value, p3.value, p4.value, p5.value, p6.value, p7.value, p8.value, p9.value, p10.value)
}