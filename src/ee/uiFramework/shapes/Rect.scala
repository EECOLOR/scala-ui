package ee.uiFramework.shapes

class Rect extends Shape {
	
	var cornerRadius:Int = 0
	
	def draw():Unit = {
	    
	    
	   draw(
	        drawCommandBuilder.genericBuilder
	    	.moveTo(cornerRadius, 0)
	    	.lineTo(width - cornerRadius, 0)
	    	.curveTo(width, cornerRadius, width, 0)
	    	.lineTo(width, height - cornerRadius)
	    	.curveTo(width - cornerRadius, height, width, height)
	    	.lineTo(cornerRadius, height)
	    	.curveTo(0, height - cornerRadius, 0, height)
	    	.lineTo(0, cornerRadius)
	    	.curveTo(cornerRadius, 0, 0, 0)
	    	.endFill()
        )
	}
}
