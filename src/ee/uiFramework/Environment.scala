package ee.uiFramework
import ee.uiFramework.shapes.Color
import ee.uiFramework.components.Component

trait Environment {

}

object Environment {
    var graphics:Graphics = _
    
    object Graphics {
        implicit val graphics = Environment.this.graphics

        sealed abstract class DrawCommand {}
	        
	    case class BeginFill(val color:Color) extends DrawCommand {}
	    case object EndFill extends DrawCommand {}
	    case class BeginLine(val thickness:Int, val color:Color) extends DrawCommand {}
	    case class MoveTo(val x:Long, val y:Long) extends DrawCommand {}
	    case class LineTo(val x:Long, val y:Long) extends DrawCommand {}
	    case class CurveTo(val x:Long, val y:Long, val anchorX:Long, val anchorY:Long) extends DrawCommand {}
    }
    
    trait Graphics {
        import Graphics._
        def drawCommands(commands:List[DrawCommand], component:Component)
    }
    
}