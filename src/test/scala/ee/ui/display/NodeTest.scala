package ee.ui.display

import org.specs2.mutable.Specification
import ee.ui.display.detail.ReadOnlyNode
import utils.SubtypeTest
import utils.TestUtils
import scala.tools.reflect.ToolBoxError

object NodeTest extends Specification {

  xonly

  "Node" should {

    "be abstract" in {
      TestUtils.eval("new ee.ui.display.Node") must throwA[ToolBoxError].like {
        case e => e.getMessage must contain("class Node is abstract")
      }
    }
    
    "extends the correct traits" in {
      SubtypeTest[Node <:< ReadOnlyNode]
    }
  }
}