package ee.ui.display.scene

import ee.ui.display.Scene
import ee.ui.properties.ReadOnlyProperty
import ee.ui.events.Event
import ee.ui.primitives.Point
import ee.ui.display.Node
import ee.ui.properties.Property
import ee.ui.events.MouseEvent
import ee.ui.display.Group
import ee.ui.events.MouseButton
import ee.ui.events.ReadOnlyEvent

trait MouseHandling { self: Scene with FocusHandling =>

  val onMouseMoved = ReadOnlyEvent[MouseEvent]
  val onMouseDown = ReadOnlyEvent[MouseEvent]
  val onMouseUp = ReadOnlyEvent[MouseEvent]

  private val lastKnownMouseEvent = Property[Option[MouseEvent]](None)

  //make sure these are registered first, we need them in other property handling
  lastKnownMouseEvent <== onMouseMoved
  lastKnownMouseEvent <== onMouseDown
  lastKnownMouseEvent <== onMouseUp

  private val lastMouseDownNode = Property[Option[Node]](None)

  onMouseDown { e =>
    nodeAtMousePosition foreach { n =>
      writableFocusedNode.value = Some(n)
      lastMouseDownNode.value = Some(n)
      n.onMouseDown fire e
    }
    //TODO If we do not have a node at mouse position, reset focused node to None
  }

  onMouseUp { e =>
    for {
      n <- nodeAtMousePosition
      ln <- lastMouseDownNode
    } if (ln == n) {
      n.onMouseUp fire e
      n.onMouseClicked fire e
    } else {
      ln.onMouseUpOutside fire e
      n.onMouseUp fire e
    }
  }

  //TODO implement that when mouse moves out of application, this is set to None
  private val writableMousePosition = Property[Option[Point]](None)
  val mousePosition: ReadOnlyProperty[Option[Point]] = writableMousePosition

  private val writableNodesAtMousePosition = Property[Seq[Node]](Seq.empty)
  val nodesAtMousePosition: ReadOnlyProperty[Seq[Node]] = writableNodesAtMousePosition

  private val writableNodeAtMousePosition = Property[Option[Node]](None)
  val nodeAtMousePosition: ReadOnlyProperty[Option[Node]] = writableNodeAtMousePosition

  writableMousePosition <== onMouseMoved map { e =>
    Point(e.sceneX, e.sceneY)
  }

  writableNodesAtMousePosition <== mousePosition map { position =>
    val nodes =
      for (p <- position; r <- root.value)
        yield findNodes(p)(r)
        
    nodes getOrElse Seq.empty
  }

  writableNodeAtMousePosition <== writableNodesAtMousePosition map { _.headOption }

  //TODO think (my head won't allow me at this moment), should this happen before or after onMouseOut
  nodesAtMousePosition.valueChange collect {
    case (oldSeq, newSeq) => {
      lastKnownMouseEvent.value foreach { e =>
        val noDrag = e.button == MouseButton.NONE
        val out = oldSeq diff newSeq
        val over = newSeq diff oldSeq

        def fireOut(n: Node) =
          (if (noDrag) n.onRollOut else n.onMouseDragOut) fire e

        def fireOver(n: Node) =
          (if (noDrag) n.onRollOver else n.onMouseDragOver) fire e

        out foreach fireOut
        over foreach fireOver
      }
    }
  }

  nodeAtMousePosition.valueChange collect {
    case (oldNode, newNode) =>
      lastKnownMouseEvent.value foreach { e =>
        oldNode foreach (_.onMouseOut fire e)
        newNode foreach (_.onMouseOver fire e)
      }
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