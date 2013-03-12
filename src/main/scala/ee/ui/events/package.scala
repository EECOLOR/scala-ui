package ee.ui

package object events {
	type Observer[T] = T => Unit
}