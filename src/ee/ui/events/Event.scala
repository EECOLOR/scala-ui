package ee.ui.events

import ee.ui.Observable

class Event[T] extends Observable[T] {
	def apply(listener:T => Unit):Unit = super.listen _
	def in(listener:PartialFunction[T, Unit]):Unit = super.listenIn _
	
	def handle = super.handle _
	def handleIn = super.handleIn _
}