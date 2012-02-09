package ee.uiFramework.shapes

class Color(private val c:Int) {

    //if only rgb is given, add alpha
    val color:Int = if ((c >> 24) == 0) c | 0xFF << 24 else c
    
    def this(r:Int, g:Int, b:Int, a:Int) {
        this(a << 24 | r << 16 | g << 8 | b )
    }
    
    def this(rgb:Int, a:Int) {
    	this(rgb | a << 24)
    }
    
    def this(rgb:Int, a:Double) {
    	this(rgb | (a * 0xFF).toInt << 24)
    }
    
	val a = color >> 24 & 0xFF 
    val r = color >> 16 & 0xFF
	val g = color >> 8 & 0xFF
	val b = color & 0xFF
    
}

object Color {
	def apply(color:Int) = new Color(color)
    def apply(r:Int, g:Int, b:Int, a:Int) = new Color(r, g, b, a)
	def apply(rgb:Int, a:Int) = new Color(rgb, a)
	def apply(rgb:Int, a:Double) = new Color(rgb, a)
    
	implicit def longToColor(color:Int) = new Color(color)
}

object NoColor extends Color(0, 0) {}
object Black extends Color(0) {}
object White extends Color(0xFFFFFF) {}