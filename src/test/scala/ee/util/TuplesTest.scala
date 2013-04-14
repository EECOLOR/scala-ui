package ee.util

import org.specs2.mutable.Specification

object TuplesTest extends Specification {
  
  xonly
  
  "Tuples" should {
    
    "Allow adding values to a tuple" in {
      import Tuples._
      
      ((1, 1l) :+ "1") === (1, 1l, "1") 
    }
    
  }
  
}