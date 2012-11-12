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

trait MouseHandling { self: Scene =>

  val onMouseMoved = new Event[MouseEvent]
  val onMouseClicked = new Event[MouseEvent]

  private val lastKnownMouseEvent = new Property[MouseEvent](null)

  lastKnownMouseEvent <== onMouseMoved
  lastKnownMouseEvent <== onMouseClicked

  private val writableMousePosition = new Property(Point(0, 0))
  val mousePosition: ReadOnlyProperty[Point] = writableMousePosition

  private val writableNodesAtMousePosition = new Property[Seq[Node]](Seq.empty)
  val nodesAtMousePosition: ReadOnlyProperty[Seq[Node]] = writableNodesAtMousePosition

  private val writableNodeAtMousePosition = new Property[Option[Node]](None)
  val nodeAtMousePosition: ReadOnlyProperty[Option[Node]] = writableNodeAtMousePosition

  writableMousePosition <== onMouseMoved map { e =>
    Point(e.sceneX, e.sceneY)
  }

  writableNodesAtMousePosition <== mousePosition map { p =>
    //TODO .value can be removed once we fix bindings and we do not import Binding._ anymore
    root.value map findNodes(p) getOrElse Seq.empty
  }
  
  writableNodeAtMousePosition <== writableNodesAtMousePosition map { _.headOption }
  
  //TODO think (my head won't allow me at this moment), should this happen before or after onMouseOut
  nodesAtMousePosition onChangedIn {
    case (oldSeq, newSeq) =>
      (oldSeq diff newSeq) foreach (_.onRollOut fire lastKnownMouseEvent)
      (newSeq diff oldSeq) foreach (_.onRollOver fire lastKnownMouseEvent)
      //those of the oldSeq that are not in the newSeq should fire onRollOut
      //those of the newSeq that are not in the oldSeq should fire onRolOver
  }
  
  nodeAtMousePosition onChangedIn {
    case (oldNode, newNode) =>
      oldNode foreach (_.onMouseOut fire lastKnownMouseEvent)
      newNode foreach (_.onMouseOver fire lastKnownMouseEvent)
  }

  private def findNodes(point: Point)(node: Node): Seq[Node] =

    node.totalTransformation.inverted map { inverted =>
      val normalizedPoint = inverted transform point

      if (node.untransformedBounds contains normalizedPoint)
        node match {
          case group: Group => {
            val childFinder = findNodes(normalizedPoint) _
            group.children.reverse.flatMap(childFinder) :+ group
          }
          case node => Seq(node)
        }
      else Seq.empty
    } getOrElse Seq.empty

}