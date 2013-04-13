package ee.ui.display

import org.specs2.mutable.Specification

import ee.ui.display.detail.GroupChildren

object GroupTest extends Specification {
  
  xonly

  val group = new Group
  
  "Group" should {
    
    "extend node" in {
      group must beAnInstanceOf[Node]
    }
    
    "have a collection of children" in {
      group.children must beAnInstanceOf[GroupChildren]
    }
  }
}