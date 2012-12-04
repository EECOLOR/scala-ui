package ee.ui.layout

import ee.ui.Node
import ee.ui.Group
import ee.ui.traits.RestrictedAccess
import ee.ui.traits.PartialExplicitSize
import ee.ui.traits.ExplicitSize
import ee.ui.traits.ExplicitHeight
import ee.ui.traits.ExplicitWidth
import ee.ui.primitives.Bounds
import ee.ui.traits.ReadOnlySize

//TODO build something so that only shizzle is measured if something has changed
//TODO introduce LayoutClient.includeInLayout
//TODO add a form of cache for things like size calculators
object DefaultResizeEngine {

  def adjustSizeWithParent(parent: ReadOnlySize, node: Node): Unit = {

    implicit val sizeInformation = retrieve.sizeInformation of parent

    val actions = adjust.size ofChild node
    //perform the actions
    actions.force
  }

  /*
   * We end up with a stream of functions that do not return anything and only 
   * perform side effects. If we would type the stream as Stream[Unit] we might 
   * end up with functions that do not perform the actual side effect.
   * 
   * We prevent that by creating the Action type. This allows us to have a 
   * return type of Stream[Action]
   */
  private type Action = Action.type
  private object Action
  private type ParentRelatedSize = ParentRelatedWidth with ParentRelatedHeight

  object update extends RestrictedAccess {

    object width {

      def ofChild(n: Node with ParentRelatedWidth)(implicit p: ParentWidthInformation): Action = {
        n.width = p.childWidthFunction(n); Action
      }

      def of(g: Group)(implicit p: ParentWidthInformation): Action = {
        g.width = p.width; Action
      }
    }

    object height {

      def ofChild(n: Node with ParentRelatedHeight)(implicit p: ParentHeightInformation): Action = {
        n.height = p.childHeightFunction(n); Action
      }

      def of(g: Group)(implicit p: ParentHeightInformation): Action = {
        g.height = p.height; Action
      }
    }
  }

  object retrieve {

    object sizeInformation {

      def of(group: Group): ParentSizeInformation = {
        val parentWidthInformation = retrieve.widthInformation of group
        val parentHeightInformation = retrieve.heightInformation of group

        new ParentSizeInformation {
          val width = parentWidthInformation.width
          val childWidthFunction = parentWidthInformation.childWidthFunction

          val height = parentHeightInformation.height
          val childHeightFunction = parentHeightInformation.childHeightFunction
        }
      }

      def of(layoutSize: ReadOnlySize): ParentSizeInformation = {

        new ParentSizeInformation {
          val width: Double = layoutSize.width
          val height: Double = layoutSize.height

          val childWidthFunction =
            { node: Node with ParentRelatedWidth => node calculateWidth width }

          val childHeightFunction =
            { node: Node with ParentRelatedHeight => node calculateHeight height }
        }
      }
    }

    def toChildWidthCalculator(g: Group) =
      g match {
        case layout: Layout => layout
        case g => new GroupChildWidthCalculator {
          val group = g
        }
      }

    def toChildHeightCalculator(g: Group) =
      g match {
        case layout: Layout => layout
        case g => new GroupChildHeightCalculator {
          val group = g
        }
      }

    def getChildWidth(node: Node): Width =
      node match {
        case node: ExplicitWidth => node.bounds.width
        case node: ParentRelatedWidth => node.minRequiredWidth
        case group: Group =>
          toChildWidthCalculator(group)
            .determineTotalChildWidth(getChildWidth)
            .calculatedMinimalSize
        case node => node.bounds.width
      }

    def getChildHeight(node: Node): Height =
      node match {
        case node: ExplicitHeight => node.bounds.height
        case node: ParentRelatedHeight => node.minRequiredHeight
        case group: Group =>
          toChildHeightCalculator(group)
            .determineTotalChildHeight(getChildHeight)
            .calculatedMinimalSize

        case node => node.bounds.height
      }

    object widthInformation {

      def of(group: Group): ParentWidthInformation = {
        val calculator = toChildWidthCalculator(group)
        val sizeInformation = calculator determineTotalChildWidth getChildWidth
        val groupWidth: Width =
          group match {
            case explicit: ExplicitWidth => group.width
            case explicit: ParentRelatedWidth => group.width
            case _ => sizeInformation.calculatedMinimalSize
          }

        new ParentWidthInformation {
          val width = groupWidth
          val childWidthFunction = calculator calculateChildWidth (_: Node with ParentRelatedWidth, groupWidth, sizeInformation)
        }
      }
    }

    object heightInformation {

      def of(group: Group): ParentHeightInformation = {
        val calculator = toChildHeightCalculator(group)
        val sizeInformation = calculator determineTotalChildHeight getChildHeight
        val groupHeight: Height =
          group match {
            case explicit: ExplicitHeight => group.height
            case explicit: ParentRelatedHeight => group.height
            case _ => sizeInformation.calculatedMinimalSize
          }

        new ParentHeightInformation {
          val height = groupHeight
          val childHeightFunction = calculator calculateChildHeight (_: Node with ParentRelatedHeight, groupHeight, sizeInformation)
        }
      }
    }
  }

  object adjust {

    object size {

      def of(g: Group): Stream[Action] = {
        implicit val sizeInformation = retrieve.sizeInformation of g

        (update.width of g) #:: (update.height of g) #:: (adjust.size of g.children)
      }

      def ofChild(n: Node)(implicit p: ParentSizeInformation): Stream[Action] =

        n match {
          case g: Group with ParentRelatedSize => adjust.size ofChild g
          case g: Group with ParentRelatedWidth => adjust.size ofChild g
          case g: Group with ParentRelatedHeight => adjust.size ofChild g

          case g: Group => adjust.size of g

          case n: ParentRelatedSize => Stream(update.width ofChild n, update.height ofChild n)
          case n: ParentRelatedWidth => Stream(update.width ofChild n)
          case n: ParentRelatedHeight => Stream(update.height ofChild n)

          case n => Stream.empty
        }

      def ofChild(g: Group with ParentRelatedSize)(implicit p: ParentSizeInformation): Stream[Action] =
        (update.width ofChild g) #:: (update.height ofChild g) #:: (adjust.children.size of g)

      def ofChild(g: Group with ParentRelatedWidth)(implicit p: ParentWidthInformation): Stream[Action] =
        (update.width ofChild g) #:: (adjust.children.width of g) #::: (adjust.height of g)

      def ofChild(g: Group with ParentRelatedHeight)(implicit p: ParentHeightInformation): Stream[Action] =
        (update.height ofChild g) #:: (adjust.children.height of g) #::: (adjust.width of g)

      def of(children: Group#Children)(implicit p: ParentSizeInformation): Stream[Action] =
        (children foldLeft Stream[Action]()) { (actions, child) =>
          actions ++ (adjust.size ofChild child)
        }
    }

    object width {

      def of(g: Group): Stream[Action] = {
        implicit val sizeInformation = retrieve.widthInformation of g

        (update.width of g) #:: (adjust.width of g.children)
      }

      def ofChild(n: Node)(implicit p: ParentWidthInformation): Stream[Action] =

        n match {

          case g: Group with ParentRelatedWidth => adjust.width ofChild g

          case g: Group => adjust.width of g

          case n: ParentRelatedWidth => Stream(update.width ofChild n)

          case n => Stream.empty
        }

      def of(children: Group#Children)(implicit p: ParentWidthInformation): Stream[Action] =
        (children foldLeft Stream[Action]()) { (actions, child) =>
          actions ++ (adjust.width ofChild child)
        }
    }

    object height {

      def of(g: Group): Stream[Action] = {
        implicit val sizeInformation = retrieve.heightInformation of g

        (update.height of g) #:: (adjust.height of g.children)
      }

      def ofChild(n: Node)(implicit p: ParentHeightInformation): Stream[Action] =

        n match {

          case g: Group with ParentRelatedHeight => adjust.size ofChild g

          case g: Group => adjust.height of g

          case n: ParentRelatedHeight => Stream(update.height ofChild n)

          case n => Stream.empty
        }

      def of(children: Group#Children)(implicit p: ParentHeightInformation): Stream[Action] =
        (children foldLeft Stream[Action]()) { (actions, child) =>
          actions ++ (adjust.height ofChild child)
        }
    }

    object children {

      object size {

        def of(g: Group): Stream[Action] = {
          implicit val sizeInformation = retrieve.sizeInformation of g

          adjust.size of g.children
        }
      }

      object width {

        def of(g: Group): Stream[Action] = {
          implicit val sizeInformation = retrieve.widthInformation of g

          adjust.width of g.children
        }
      }

      object height {

        def of(g: Group): Stream[Action] = {
          implicit val sizeInformation = retrieve.heightInformation of g

          adjust.height of g.children
        }
      }
    }
  }

  trait ParentSizeInformation extends ParentWidthInformation with ParentHeightInformation

  trait ParentWidthInformation {
    val width: Width
    val childWidthFunction: Node with ParentRelatedWidth => Width
  }

  trait ParentHeightInformation {
    val height: Height
    val childHeightFunction: Node with ParentRelatedHeight => Height
  }
}