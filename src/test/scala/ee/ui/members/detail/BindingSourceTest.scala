package ee.ui.members.detail

import org.specs2.mutable.Specification

import ee.ui.members.Property
import ee.ui.members.ReadOnlyProperty
import ee.ui.system.RestrictedAccess

class BindingSourceTest extends Specification {

  xonly
  isolated

  val prop = Property(0)
  val sourceProp = ReadOnlyProperty(1)
  val source = new BindingSource(sourceProp)
  def setSourceValue(value: Int) =
    ReadOnlyProperty.setValue(sourceProp, value)(RestrictedAccess)

  "BindingSource" should {

    "bind to another property" in {
      source bindTo prop

      prop.value === 1
      setSourceValue(2)
      prop.value === 2
    }

    "bind to a property that is a subtype" in {
      val prop = Property[AnyVal](0)
      source bindTo prop
      prop.value === 1
    }

    "bind to a method" in {
      var value = 0
      source bindWith { value = _ }
      value === 1
      setSourceValue(2)
      value === 2
    }

    "be able to filter" in {
      val filtered = source filter (_ > 2)
      filtered bindTo prop
      prop.value === 0
      setSourceValue(2)
      prop.value === 0
      setSourceValue(3)
      prop.value === 3
    }
  }
}