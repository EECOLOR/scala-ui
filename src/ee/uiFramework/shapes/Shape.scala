package ee.uiFramework.shapes
import ee.uiFramework.components.Component
import ee.uiFramework.Environment.Graphics

class Shape extends Component {

    import ee.uiFramework.Environment.Graphics._
    
    var fillColor:Color = NoColor
	var lineColor:Color = NoColor
	var lineThickness:Int = 1
    
    def draw(commands:List[DrawCommand], generateBeginCommands:Boolean = true)(implicit graphics:Graphics) = {
        
        var newCommands = commands
        
        if (generateBeginCommands) {
        	if (fillColor != NoColor) newCommands ++= drawCommandBuilder.beginFill(fillColor)   
   			if (lineColor != NoColor) newCommands ++= drawCommandBuilder.beginLine(lineThickness, lineColor)
        }
	
        graphics.drawCommands(newCommands, this)
    }
    
    object drawCommandBuilder {
	    def beginFill(color:Color) = (new DrawCommands(List()) with BeginFillBuilder).beginFill(color)
		def beginLine(thickness:Int, color:Color) = (new DrawCommands(List()) with BeginLineBuilder).beginLine(thickness, color)
		
	    def genericBuilder = new DrawCommands(List()) 
	    	with LineToBuilder with MoveToBuilder with CurveToBuilder
		
	    
	    	
	    class DrawCommands(val list:List[DrawCommand]) {}
	
		object DrawCommands {
		    implicit def drawCommandsToList(drawCommands:DrawCommands):List[DrawCommand] = drawCommands.list.reverse
		}
		
		private[drawCommandBuilder] trait BeginLineBuilder {self:DrawCommands =>
		    def beginLine(thickness:Int, color:Color) = 
		        new DrawCommands(new BeginLine(thickness, color) :: list) with BeginFillAfterLineBuilder
		        	with EndFillBuilder with MoveToBuilder with LineToBuilder with CurveToBuilder
		}
		
		private[drawCommandBuilder] trait BeginLineAfterFillBuilder {self:DrawCommands =>
			def beginLine(thickness:Int, color:Color) = 
				new DrawCommands(new BeginLine(thickness, color) :: list)
					with EndFillBuilder with MoveToBuilder with LineToBuilder with CurveToBuilder
		}
		
		private[drawCommandBuilder] trait BeginFillBuilder {self:DrawCommands =>
			def beginFill(color:Color) = 
			    new DrawCommands(new BeginFill(color) :: list) with BeginLineAfterFillBuilder
			    	with EndFillBuilder with MoveToBuilder with LineToBuilder with CurveToBuilder
		}
		
		private[drawCommandBuilder] trait BeginFillAfterLineBuilder {self:DrawCommands =>
			def beginFill(color:Color) = 
				new DrawCommands(new BeginFill(color) :: list)
					with EndFillBuilder with MoveToBuilder with LineToBuilder with CurveToBuilder
		}
		
		private[drawCommandBuilder] trait EndFillBuilder {self:DrawCommands => 
		    def endFill() = new DrawCommands(EndFill :: list)
		}
		
		private[drawCommandBuilder] trait MoveToBuilder {self:DrawCommands =>
			def moveTo(x:Long, y:Long) = new DrawCommands(new MoveTo(x, y) :: list)
				with EndFillBuilder with MoveToBuilder with LineToBuilder with CurveToBuilder
		}
		
		private[drawCommandBuilder] trait LineToBuilder {self:DrawCommands =>
		    def lineTo(x:Long, y:Long) = new DrawCommands(new LineTo(x, y) :: list)
		    	with EndFillBuilder with MoveToBuilder with LineToBuilder with CurveToBuilder
		}
		
		private[drawCommandBuilder] trait CurveToBuilder {self:DrawCommands =>
	   		def curveTo(x:Long, y:Long, anchorX:Long, anchorY:Long) = 
		    		new DrawCommands(new CurveTo(x, y, anchorX, anchorY) :: list) 
		    with EndFillBuilder with MoveToBuilder with LineToBuilder with CurveToBuilder
		}
	}
}





