package ee.ui.members.detail

import org.specs2.mutable.Specification
import ee.ui.members.Property
import scala.collection.mutable.ListBuffer

class BindingSourceTest extends Specification {

  xonly
  isolated

  val prop1 = Property(1)
  val prop2 = Property("1")
  val prop3 = Property(1l)
  val bindingSource1 = prop1: BindingSource[Int]
  val bindingSource2 = prop2: BindingSource[String]
  val bindingSource3 = prop3: BindingSource[Long]
  val changes = ListBuffer.empty[(Int, String, Long)]

  "BindingSource" should {
    
  }
}