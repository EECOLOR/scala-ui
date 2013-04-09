package ee.ui.members.detail

trait Subscription {
  def unsubscribe():Unit
  def enable():Unit
  def disable():Unit
}