package ee.ui.layout

import ee.ui.display.Node
import org.specs2.matcher.Matcher
import org.specs2.Specification
import org.specs2.execute.Result
import ee.ui.display.Group
import ee.ui.properties.ReadOnlyProperty

trait LayoutTestHelpers { self: Specification =>

  trait Expected[T] { self: Node =>

    val default: T
    protected var expected: T = default

    val name: String

    def beExpected: Matcher[T] = (e: T) => {

      def displayName(node: Node with Expected[T]): String =
        node.parent.map(parent => displayName(parent.asInstanceOf[Node with Expected[T]]) + " - ").getOrElse("") + node.name

      val path = displayName(this)
      (expected == e, s"$path failed, expected $expected, got $e")
    }

    def sizeExpected = value must beExpected
    def value: T
  }

  trait ExpectedSize extends Expected[Size] { self: Node with ee.ui.display.traits.Size =>

    val default = (0d, 0d)

    def expectingSize(width: Width, height: Height) = {
      expected = (width, height)
    }

    def value = (width, height)
  }

  trait ExpectedPosition extends Expected[Position] { self: Node with ee.ui.display.traits.Size =>

    val default = (0d, 0d)

    def expectingPosition(x: X, y: Y) = {
      expected = (x, y)
    }

    def value = (x, y)
  }

  trait ExpectedSizeAndPosition extends Expected[(Size, Position)] { self: Node =>
    val default = ((0d, 0d), (0d, 0d))

    def expecting(x: X, y: Y)(width: Width, height: Height) = {
      expected = ((x, y), (width, height))
    }

    def value = ((x, y), (width, height))
  }

  def checkResults(node: Expected[_]): Result = {
    node match {
      case group: TestGroupS => {
        group.children.foldLeft(node.sizeExpected: Result) { (matcher, node) =>
          matcher and checkResults(node.asInstanceOf[Expected[_]])
        }

      }
      case node => node.sizeExpected
    }
  }

  trait TestNodeS extends Node with ExpectedSize
  trait TestGroupS extends Group with ExpectedSize
  
  trait TestNodeP extends Node with ExpectedPosition
  trait TestGroupP extends Group with ExpectedPosition
  
  trait TestNode extends Node with ExpectedSizeAndPosition
  trait TestGroup extends Group with ExpectedSizeAndPosition
}