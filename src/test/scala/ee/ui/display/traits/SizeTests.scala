package ee.ui.display.traits

import org.specs2.mutable.Specification

class SizeTests extends Specification {

  xonly
  isolated

  val size = new Size {}

  "Size" should {

    "extend ReadOnlySize" in {
      size must beAnInstanceOf[ReadOnlySize]
    }

    "have a width property with a default value of 0" in {
      size.width.value === 0d
      size.width = 1d
      size.width.value === 1d
    }

    "have a height property with a default value of 0" in {
      size.height.value === 0d
      size.height = 1d
      size.height.value === 1d
    }
  }
}