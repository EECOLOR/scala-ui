

object tests {import scala.runtime.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(62); 
  println("Welcome to the Scala worksheet")
  
  import ee.ui.properties.Property
  ;$skip(83); 
  val _width = new Property(Double.NaN);System.out.println("""_width : ee.ui.properties.Property[Double] = """ + $show(_width ));$skip(21); 
  def width = _width;System.out.println("""width: => ee.ui.properties.Property[Double]""");$skip(50); 
  def width_=(value:Double) = width.value = value
  ;System.out.println("""width_$eq: (value: Double)Unit""");$skip(52); 
  width.forNewValue( { v =>
    println(v)
  })
;$skip(33); 
	def test = {
		width_=(10)
	}
	;System.out.println("""test: => Unit""");$skip(9); 
	test}
}
