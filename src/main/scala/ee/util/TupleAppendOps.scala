package ee.util

import scala.language.implicitConversions

object GenerateTupleOps {

  def main(args: Array[String]) {

    def char(n: Int) = ('A' + n).toChar
    def prop(n: Int) = "t._" + (n + 1)

    val result =
      for (n <- 1 to 21) yield {

        val range = (0 until n)
        val tupleTypeParams = range map char mkString ", "
        val tupleProperties = range map prop mkString ", "

        val elementType = char(n)
        val elementProperty = prop(n)

        val first = n == 1
        val tupleType = if (first) s"Tuple1[$tupleTypeParams]" else s"($tupleTypeParams)"
        val tupleInstance = if (first) s"Tuple1($tupleProperties)" else s"($tupleProperties)"

        val resultType = s"($tupleTypeParams, $elementType)"

        s"""|implicit def tupleOps$n[$tupleTypeParams, $elementType] = 
            |  new TupleAppendOps[$tupleType, $elementType, $resultType] {
            |    def append(t: $tupleType, e: $elementType) = ($tupleProperties, e)
            |    def init(t: $resultType) = $tupleInstance
            |    def last(t: $resultType) = $elementProperty
            |  }""".stripMargin
      }

    println(result mkString "\n")
  }
}

trait TupleAppendOps[Tuple, Element, Result] {

  def append(x: Tuple, y: Element): Result
  def init(z: Result): Tuple
  def last(z: Result): Element

  implicit class AppendEnhancement(x: Tuple) {
    def :+(y: Element) = TupleAppendOps.this.append(x, y)
  }
  implicit class InitEnhancement(z: Result) {
    def init = TupleAppendOps.this.init(z)
  }
  implicit class LastEnhancement(z: Result) {
    def last = TupleAppendOps.this.last(z)
  }
}

object TupleAppendOps {

  implicit def tupleOps1[A, B] =
    new TupleAppendOps[Tuple1[A], B, (A, B)] {
      def append(t: Tuple1[A], e: B) = (t._1, e)
      def init(t: (A, B)) = Tuple1(t._1)
      def last(t: (A, B)) = t._2
    }
  implicit def tupleOps2[A, B, C] =
    new TupleAppendOps[(A, B), C, (A, B, C)] {
      def append(t: (A, B), e: C) = (t._1, t._2, e)
      def init(t: (A, B, C)) = (t._1, t._2)
      def last(t: (A, B, C)) = t._3
    }
  implicit def tupleOps3[A, B, C, D] =
    new TupleAppendOps[(A, B, C), D, (A, B, C, D)] {
      def append(t: (A, B, C), e: D) = (t._1, t._2, t._3, e)
      def init(t: (A, B, C, D)) = (t._1, t._2, t._3)
      def last(t: (A, B, C, D)) = t._4
    }
  implicit def tupleOps4[A, B, C, D, E] =
    new TupleAppendOps[(A, B, C, D), E, (A, B, C, D, E)] {
      def append(t: (A, B, C, D), e: E) = (t._1, t._2, t._3, t._4, e)
      def init(t: (A, B, C, D, E)) = (t._1, t._2, t._3, t._4)
      def last(t: (A, B, C, D, E)) = t._5
    }
  implicit def tupleOps5[A, B, C, D, E, F] =
    new TupleAppendOps[(A, B, C, D, E), F, (A, B, C, D, E, F)] {
      def append(t: (A, B, C, D, E), e: F) = (t._1, t._2, t._3, t._4, t._5, e)
      def init(t: (A, B, C, D, E, F)) = (t._1, t._2, t._3, t._4, t._5)
      def last(t: (A, B, C, D, E, F)) = t._6
    }
  implicit def tupleOps6[A, B, C, D, E, F, G] =
    new TupleAppendOps[(A, B, C, D, E, F), G, (A, B, C, D, E, F, G)] {
      def append(t: (A, B, C, D, E, F), e: G) = (t._1, t._2, t._3, t._4, t._5, t._6, e)
      def init(t: (A, B, C, D, E, F, G)) = (t._1, t._2, t._3, t._4, t._5, t._6)
      def last(t: (A, B, C, D, E, F, G)) = t._7
    }
  implicit def tupleOps7[A, B, C, D, E, F, G, H] =
    new TupleAppendOps[(A, B, C, D, E, F, G), H, (A, B, C, D, E, F, G, H)] {
      def append(t: (A, B, C, D, E, F, G), e: H) = (t._1, t._2, t._3, t._4, t._5, t._6, t._7, e)
      def init(t: (A, B, C, D, E, F, G, H)) = (t._1, t._2, t._3, t._4, t._5, t._6, t._7)
      def last(t: (A, B, C, D, E, F, G, H)) = t._8
    }
  implicit def tupleOps8[A, B, C, D, E, F, G, H, I] =
    new TupleAppendOps[(A, B, C, D, E, F, G, H), I, (A, B, C, D, E, F, G, H, I)] {
      def append(t: (A, B, C, D, E, F, G, H), e: I) = (t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, e)
      def init(t: (A, B, C, D, E, F, G, H, I)) = (t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8)
      def last(t: (A, B, C, D, E, F, G, H, I)) = t._9
    }
  implicit def tupleOps9[A, B, C, D, E, F, G, H, I, J] =
    new TupleAppendOps[(A, B, C, D, E, F, G, H, I), J, (A, B, C, D, E, F, G, H, I, J)] {
      def append(t: (A, B, C, D, E, F, G, H, I), e: J) = (t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, e)
      def init(t: (A, B, C, D, E, F, G, H, I, J)) = (t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9)
      def last(t: (A, B, C, D, E, F, G, H, I, J)) = t._10
    }
  implicit def tupleOps10[A, B, C, D, E, F, G, H, I, J, K] =
    new TupleAppendOps[(A, B, C, D, E, F, G, H, I, J), K, (A, B, C, D, E, F, G, H, I, J, K)] {
      def append(t: (A, B, C, D, E, F, G, H, I, J), e: K) = (t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, e)
      def init(t: (A, B, C, D, E, F, G, H, I, J, K)) = (t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10)
      def last(t: (A, B, C, D, E, F, G, H, I, J, K)) = t._11
    }
  implicit def tupleOps11[A, B, C, D, E, F, G, H, I, J, K, L] =
    new TupleAppendOps[(A, B, C, D, E, F, G, H, I, J, K), L, (A, B, C, D, E, F, G, H, I, J, K, L)] {
      def append(t: (A, B, C, D, E, F, G, H, I, J, K), e: L) = (t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, e)
      def init(t: (A, B, C, D, E, F, G, H, I, J, K, L)) = (t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11)
      def last(t: (A, B, C, D, E, F, G, H, I, J, K, L)) = t._12
    }
  implicit def tupleOps12[A, B, C, D, E, F, G, H, I, J, K, L, M] =
    new TupleAppendOps[(A, B, C, D, E, F, G, H, I, J, K, L), M, (A, B, C, D, E, F, G, H, I, J, K, L, M)] {
      def append(t: (A, B, C, D, E, F, G, H, I, J, K, L), e: M) = (t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, e)
      def init(t: (A, B, C, D, E, F, G, H, I, J, K, L, M)) = (t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12)
      def last(t: (A, B, C, D, E, F, G, H, I, J, K, L, M)) = t._13
    }
  implicit def tupleOps13[A, B, C, D, E, F, G, H, I, J, K, L, M, N] =
    new TupleAppendOps[(A, B, C, D, E, F, G, H, I, J, K, L, M), N, (A, B, C, D, E, F, G, H, I, J, K, L, M, N)] {
      def append(t: (A, B, C, D, E, F, G, H, I, J, K, L, M), e: N) = (t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, e)
      def init(t: (A, B, C, D, E, F, G, H, I, J, K, L, M, N)) = (t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13)
      def last(t: (A, B, C, D, E, F, G, H, I, J, K, L, M, N)) = t._14
    }
  implicit def tupleOps14[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] =
    new TupleAppendOps[(A, B, C, D, E, F, G, H, I, J, K, L, M, N), O, (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)] {
      def append(t: (A, B, C, D, E, F, G, H, I, J, K, L, M, N), e: O) = (t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, e)
      def init(t: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)) = (t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14)
      def last(t: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)) = t._15
    }
  implicit def tupleOps15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] =
    new TupleAppendOps[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O), P, (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)] {
      def append(t: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O), e: P) = (t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, e)
      def init(t: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)) = (t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15)
      def last(t: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)) = t._16
    }
  implicit def tupleOps16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] =
    new TupleAppendOps[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P), Q, (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)] {
      def append(t: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P), e: Q) = (t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, e)
      def init(t: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)) = (t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16)
      def last(t: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)) = t._17
    }
  implicit def tupleOps17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] =
    new TupleAppendOps[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q), R, (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)] {
      def append(t: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q), e: R) = (t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17, e)
      def init(t: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)) = (t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17)
      def last(t: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)) = t._18
    }
  implicit def tupleOps18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] =
    new TupleAppendOps[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R), S, (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)] {
      def append(t: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R), e: S) = (t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17, t._18, e)
      def init(t: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)) = (t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17, t._18)
      def last(t: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)) = t._19
    }
  implicit def tupleOps19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] =
    new TupleAppendOps[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S), T, (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)] {
      def append(t: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S), e: T) = (t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17, t._18, t._19, e)
      def init(t: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)) = (t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17, t._18, t._19)
      def last(t: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)) = t._20
    }
  implicit def tupleOps20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] =
    new TupleAppendOps[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T), U, (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)] {
      def append(t: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T), e: U) = (t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17, t._18, t._19, t._20, e)
      def init(t: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)) = (t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17, t._18, t._19, t._20)
      def last(t: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)) = t._21
    }
  implicit def tupleOps21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] =
    new TupleAppendOps[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U), V, (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)] {
      def append(t: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U), e: V) = (t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17, t._18, t._19, t._20, t._21, e)
      def init(t: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)) = (t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17, t._18, t._19, t._20, t._21)
      def last(t: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)) = t._22
    }

}