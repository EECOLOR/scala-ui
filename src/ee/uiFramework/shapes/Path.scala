package ee.uiFramework.shapes

abstract class Path extends Shape {
    import ee.uiFramework.Environment.Graphics._
    
	val path:List[DrawCommand]
    
}