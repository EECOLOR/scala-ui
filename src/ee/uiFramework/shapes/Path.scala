package ee.uiFramework.shapes
import scala.util.parsing.combinator.JavaTokenParsers
import ee.uiFramework.Environment.Graphics._

abstract class Path extends Shape {
    val path:String
    
    draw(Path.parsePath(path))
}

object Path {
    sealed abstract class Relative {
        type Positional = {val x:Long; val y:Long}

        def toAbsolute[A <: DrawCommand](previous:A):DrawCommand
    }
    case class RelativeMoveTo(x:Long, y:Long) extends Relative {

        def toAbsolute[A <: DrawCommand](previous:A) = {
        	val p = previous.asInstanceOf[Positional]
   			MoveTo(p.x + x, p.y + y)
        }
            
    }
    
    object pathParsers extends JavaTokenParsers {
        def path = rep(moveTo)
        
        def moveTo = ("m"|"M") ~ floatingPointNumber ~ floatingPointNumber ^^ {
            case "m" ~ x ~ y => MoveTo(x.toLong, y.toLong)
            case "M" ~ x ~ y => RelativeMoveTo(x.toLong, y.toLong)
        }
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