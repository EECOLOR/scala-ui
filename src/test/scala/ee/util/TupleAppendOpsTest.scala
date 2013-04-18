package ee.util

import org.specs2.mutable.Specification
import utils.SignatureTest

object TupleAppendOpsTest extends Specification {

  xonly

  type Tuple = (String, Long)
  type Element = Int
  type Result = (String, Long, Int)
  type TestTupleAppendOps = TupleAppendOps[Tuple, Element, Result]
  val tupleOps = implicitly[TestTupleAppendOps]
  import tupleOps._

  "TupleAppendOps" should {

    "have the correct Signatures" in {
      SignatureTest[TestTupleAppendOps, Tuple, Element, Result](_.append(_, _))
      SignatureTest[TestTupleAppendOps, Result, Tuple](_.init(_))
      SignatureTest[TestTupleAppendOps, Result, Element](_.last(_))
    }

    "have have an enhancements class for append" in {
      (("1", 1l) :+ 1) === ("1", 1l, 1)
    }

    "have have an enhancements class for init" in {
      ("1", 1l, 1).init === ("1", 1l)
    }

    "have have an enhancements class for last" in {
      ("1", 1l, 1).last === 1
    }

  }

}