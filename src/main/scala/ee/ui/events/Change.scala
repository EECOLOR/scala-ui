package ee.ui.events

sealed trait Change[T]
case class Remove[T](index: Int, element: T) extends Change[T]
case class Add[T](index: Int, element: T) extends Change[T]
case class Clear[T](elements: Seq[T]) extends Change[T]