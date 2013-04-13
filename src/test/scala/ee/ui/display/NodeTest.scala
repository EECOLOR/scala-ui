package ee.ui.display

import org.specs2.mutable.Specification
import ee.ui.display.detail.ReadOnlyNode
import utils.SubtypeTest

object NodeTest extends Specification {

  xonly

  "Node" should {

    "extends the correct traits" in {
      SubtypeTest[Node <:< ReadOnlyNode]
    }
  }
}