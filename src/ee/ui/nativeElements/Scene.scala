package ee.ui.nativeElements

import ee.ui.properties.Property
import ee.ui.properties.ReadOnlyProperty

class Scene(useDepthBuffer:Boolean = false) extends NativeElement[Scene] {
	
  def nativeElement = createNativeElement
  
  lazy val depthBuffer:ReadOnlyProperty[Boolean] = new Property(useDepthBuffer) 
}