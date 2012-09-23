

object tests {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  import ee.ui.properties.Property
  
  val _width = new Property(Double.NaN)           //> _width : ee.ui.properties.Property[Double] = ee.ui.properties.Property@28aca
                                                  //| a28
  def width = _width                              //> width: => ee.ui.properties.Property[Double]
  def width_=(value:Double) = width.value = value //> width_$eq: (value: Double)Unit
  
  width.forNewValue( { v =>
    println(v)
  })

	def test = {
		width_=(10)
	}                                         //> test: => Unit
	
	test
}