package ee.ui.nativeElements

import ee.ui.properties.Property
import ee.ui.properties.ReadOnlyProperty
import ee.ui.Group
import ee.ui.primitives.Color
import ee.ui.primitives.Camera
import ee.ui.events.Event
import ee.ui.events.MouseEvent
import ee.ui.events.NullEvent
import ee.ui.traits.Fill
import ee.ui.traits.LayoutSize
import ee.ui.traits.LayoutPosition
import ee.ui.Node
import ee.ui.primitives.Point
import ee.ui.properties.Binding._
import ee.ui.events.NullEvent
import ee.ui.events.MouseEvent

class Scene(defaultDepthBuffer: Boolean = false) extends LayoutPosition with LayoutSize with Fill with MouseHandling {

  def defaultFill = Color.WHITE

  lazy val depthBuffer: ReadOnlyProperty[Boolean] = new Property(defaultDepthBuffer)

  private val _root = new Property[Option[Node]](None)
  def root = _root
  def root_=(value: Node) = root.value = Some(value)
  def root_=(value: Option[Node]) = root.value = value

  private val _camera = new Property[Option[Camera]](None)
  def camera = _camera
  def camera_=(value: Camera) = camera.value = Some(value)
  def camera_=(value: Option[Camera]) = camera.value = value

  
}

trait MouseHandling { self:Scene =>
  
  val onMouseMoved = new Event[MouseEvent]
  val onMouseClicked = new Event[MouseEvent]
  
  private val lastKnownMouseEvent = new Property[MouseEvent](null)
  
  lastKnownMouseEvent <== onMouseMoved
  lastKnownMouseEvent <== onMouseClicked
  

  private val writableMousePosition = new Property(Point(0, 0))
  val mousePosition:ReadOnlyProperty[Point] = writableMousePosition
  
  private val writableNodeAtMousePosition = new Property[Option[Node]](None)
  val nodeAtMousePosition:ReadOnlyProperty[Option[Node]] = writableNodeAtMousePosition
  
  writableMousePosition <== onMouseMoved map { e =>
    Point(e.sceneX, e.sceneY)
  }
  
  writableNodeAtMousePosition <== mousePosition map { p =>
    root flatMap findNode(p)
  }
  
  nodeAtMousePosition onChangedIn {
    case (oldNode, newNode) => 
      oldNode.foreach (_.onMouseOut fire lastKnownMouseEvent)
      newNode.foreach (_.onMouseOver fire lastKnownMouseEvent)
  }
  
  private def findNode(point: Point)(node: Node): Option[Node] =

      node.totalTransformation.inverted flatMap { inverted =>
        val normalizedPoint = inverted transform point

        if (node.untransformedBounds contains normalizedPoint)
          node match {
            case group: Group => {
              val childFinder = findNode(normalizedPoint) _
              group.children.reverse.view.map(childFinder).collectFirst {
                case Some(node) => node
              } orElse Some(group)
            }
            case node => Some(node)
          }
        else None
      }
  
}