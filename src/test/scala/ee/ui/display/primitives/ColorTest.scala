package ee.ui.display.primitives

import org.specs2.mutable.Specification

object ColorTest extends Specification {

  xonly

  "Color" should {
    
    "have a value and an alpha property" in {
      val color = Color(value = 1, alpha = 0.5) 
      color.value ==== 1
      color.alpha ==== 0.5
    }
    
    "have an alpha property with a default of 1" in{
      Color(0).alpha ==== 1d
    }
    
    "should throw an error if alpha is < 0 or > 1" in {
      Color(0, -1) must throwA[IllegalArgumentException]
      Color(0, 2) must throwA[IllegalArgumentException]
    }
    
    "have defaults for BLACK and WHITE" in {
      Color.BLACK === Color(0)
      Color.WHITE === Color(0xFFFFFF)
    }
  }

}