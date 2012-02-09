package ee.uiFramework.shapes
import scala.util.parsing.combinator.JavaTokenParsers
import ee.uiFramework.Environment.Graphics._

abstract class Path extends Shape {
    val path:String
    
    draw(Path.parsePath(path))
}

object Path {
    sealed abstract class Relative(x:Long, y:Long) {
        
        type Positional = {val x:Long; val y:Long}

        val constructor:(Long, Long) => DrawCommand
        
        def toAbsolute[A <: DrawCommand](previous:A) = {
        		val p = previous.asInstanceOf[Positional]
        		constructor(p.x + x, p.y + y)
        }
    }
    case class RelativeMoveTo(x:Long, y:Long) extends Relative(x, y) {
            val constructor = MoveTo
    }
    case class RelativeLineTo(x:Long, y:Long) extends Relative(x, y) {
    	val constructor = LineTo
    }
    case class RelativeCurveTo(x:Long, y:Long, anchorX:Long, anchorY:Long) extends Relative(x, y) {
    	val constructor = (x:Long, y:Long) => CurveTo(x, y, x - this.x + anchorX, y - this.y + anchorY)
    }
    
    object pathParsers extends JavaTokenParsers {
        def path = rep(command)
        
        def command = xyCommand | yxabCommand
        
        def xyCommands = m | mr | l | lr
        def xyabCommands = c | cr
        
        def xyCommand = xyCommands ~ floatingPointNumber ~ floatingPointNumber ^^ {
            case xyCommands ~ x ~ y => xyCommands(x.toLong, y.toLong)
        }

        def yxabCommand = xyabCommands ~ floatingPointNumber ~ floatingPointNumber ~ floatingPointNumber ~ floatingPointNumber ^^ {
        	case xyabCommands ~ x ~ y ~ a ~ b => xyabCommands(x.toLong, y.toLong, a.toLong, b.toLong)
        }

        def m = "M" ^^ {_ => MoveTo}
        def mr = "m" ^^ {_ => RelativeMoveTo}
        def l = "L" ^^ {_ => LineTo}
        def lr = "l" ^^ {_ => RelativeLineTo}
        def c = "C" ^^ {_ => CurveTo}
        def cr = "c" ^^ {_ => RelativeCurveTo}
    }

    
    def parsePath(s:String):List[DrawCommand] = {
        pathParsers.parseAll(pathParsers.path, s) match {
            case pathParsers.Success(parsedCommands:List[_], _) => 
               parsedCommands.foldLeft(List[DrawCommand]()) { (acc, parsedCommand) =>
                   (parsedCommand match {
                       case d:DrawCommand => d
                       case r:Relative => r.toAbsolute(acc.head)
                   }) :: acc
               }.reverse
        }
    }
}