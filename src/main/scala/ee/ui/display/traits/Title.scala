package ee.ui.display.traits

import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty

trait ReadOnlyTitle {
  protected val _title = Property[Option[String]](None)
  def title: ReadOnlyProperty[Option[String]] = _title
}

trait Title extends ReadOnlyTitle {
  override def title: Property[Option[String]] = _title
  def title_=(value: String) = _title.value = Some(value)
  def title_=(value: Option[String]) = _title.value = value
}