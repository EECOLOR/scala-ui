package ee.ui

package object observables {
	type Observer[T] = T => Unit
}