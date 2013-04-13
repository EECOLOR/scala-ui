package ee.ui.members.detail

import org.specs2.mutable.Specification
import ee.ui.members.Property
import scala.collection.mutable.ListBuffer

class BidirectionBindingTest extends Specification {

  xonly
  isolated

  val prop1 = Property[Int](1)
  val prop2 = Property[Int](2)

  val leftChanges = ListBuffer.empty[Int]
  val rightChanges = ListBuffer.empty[Int]

  val binding = prop1 <==> prop2
  binding.left.change { leftChanges += _ }
  binding.right.change { rightChanges += _ }

  "BidirectionalBinding" should {
    "set initial value l -> r" in {

      prop2.value === 1

    }
    "l -> r set value" in {
      prop1.value = 2

      prop2.value === 2
    }
    "l -> r emit correct changes" in {
      prop1.value = 2

      leftChanges.toSeq === Seq.empty[Int]
      rightChanges.toSeq === Seq(2)
    }
    "r -> l set value" in {
      prop2.value = 2

      prop1.value === 2
    }
    "r -> l emit correct changes" in {
      prop2.value = 2

      rightChanges.toSeq === Seq.empty[Int]
      leftChanges.toSeq === Seq(2)
    }
  }
}