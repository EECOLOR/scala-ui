package ee.ui.members.detail

import org.specs2.mutable.Specification
import ee.ui.members.ReadOnlyProperty
import ee.ui.system.RestrictedAccess
import utils.TypeTest

class ReadOnlyTupleCombinatorTest extends Specification {

  xonly
  isolated

  val readOnlyTupleCombinator = new ReadOnlyTupleCombinator(ReadOnlyProperty((1, 1l)))
  val combined = readOnlyTupleCombinator | ReadOnlyProperty("1")

  "ReadOnlyTupleCombinator" should {

    "create a CombinedPropertyBase that throws an error if the value is set" in {

      TypeTest[ReadOnlyProperty[(Int, Long, String)]].forInstance(combined)

      combined must beAnInstanceOf[CombinedPropertyBase[_, _, _, _, _]]

      ReadOnlyProperty.setValue(combined, (2, 2l, "2"))(RestrictedAccess) must
        throwAn[UnsupportedOperationException]
    }

    "use the correct change and valueChange events" in {
      val c = combined.asInstanceOf[CombinedPropertyBase[_, _, _, _, _]]
      c.change === c.changeEvent
      c.valueChange === c.valueChangeEvent
    }
  }
}