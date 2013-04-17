package ee.util

import shapeless.::
import shapeless.HList
import shapeless.HListerAux
import shapeless.HNil
import shapeless.PrependAux
import shapeless.TuplerAux

object GenerateTupleOps {

  def main(args: Array[String]) {

    val result =
      for (n <- 1 to 21) yield {
        val m = if (n == 0) 1 else n
        val xTypeParams = (0 until m).map(i => ('A' + i).toChar).mkString(", ")
        val yTypeParam = ('A' + m).toChar
        val xProperties = if (n == 0) "x" else (0 until m).map(i => "x._" + (i + 1)).mkString(", ")
        val yProperty = if (n == 0) "???" else "y._" + (m + 1)
        val xType = if (n == 0) xTypeParams else if (n == 1) s"Tuple1[$xTypeParams]" else s"($xTypeParams)"
        val xInstance = if (n == 0) "???" else if (n == 1) s"Tuple1($xProperties)" else s"($xProperties)"
        s"""|implicit def tupleOps$n[$xTypeParams, $yTypeParam] = new TupleOps[$xType, $yTypeParam, ($xTypeParams, $yTypeParam)] {
            |  def append(x: $xType, y: $yTypeParam) = ($xProperties, y)
            |  def init(x: ($xTypeParams, $yTypeParam)) = $xInstance
            |  def last(y: ($xTypeParams, $yTypeParam)) = $yProperty
            |}""".stripMargin
      }

    println(result mkString "\n")
  }
}

object Tuples {

  println("Cleanup the shapeless stuff and make the TupleOps more usable")
  
  trait TupleOps[A, B, C] {
    def append(x: A, y: B): C
    def init(z: C): A
    def last(z: C): B

    implicit class TupleOpsAppend(x: A) {
      def :+(y: B): C = TupleOps.this.append(x, y)
    }
    implicit class TupleOpsInit(z: C) {
      def init = TupleOps.this.init(z)
    }
    implicit class TupleOpsLast(z: C) {
      def last = TupleOps.this.last(z)
    }
  }

  implicit def tupleOps1[A, B] = new TupleOps[Tuple1[A], B, (A, B)] {
    def append(x: Tuple1[A], y: B) = (x._1, y)
    def init(x: (A, B)) = Tuple1(x._1)
    def last(y: (A, B)) = y._2
  }
  implicit def tupleOps2[A, B, C] = new TupleOps[(A, B), C, (A, B, C)] {
    def append(x: (A, B), y: C) = (x._1, x._2, y)
    def init(x: (A, B, C)) = (x._1, x._2)
    def last(y: (A, B, C)) = y._3
  }
  implicit def tupleOps3[A, B, C, D] = new TupleOps[(A, B, C), D, (A, B, C, D)] {
    def append(x: (A, B, C), y: D) = (x._1, x._2, x._3, y)
    def init(x: (A, B, C, D)) = (x._1, x._2, x._3)
    def last(y: (A, B, C, D)) = y._4
  }
  implicit def tupleOps4[A, B, C, D, E] = new TupleOps[(A, B, C, D), E, (A, B, C, D, E)] {
    def append(x: (A, B, C, D), y: E) = (x._1, x._2, x._3, x._4, y)
    def init(x: (A, B, C, D, E)) = (x._1, x._2, x._3, x._4)
    def last(y: (A, B, C, D, E)) = y._5
  }
  implicit def tupleOps5[A, B, C, D, E, F] = new TupleOps[(A, B, C, D, E), F, (A, B, C, D, E, F)] {
    def append(x: (A, B, C, D, E), y: F) = (x._1, x._2, x._3, x._4, x._5, y)
    def init(x: (A, B, C, D, E, F)) = (x._1, x._2, x._3, x._4, x._5)
    def last(y: (A, B, C, D, E, F)) = y._6
  }
  implicit def tupleOps6[A, B, C, D, E, F, G] = new TupleOps[(A, B, C, D, E, F), G, (A, B, C, D, E, F, G)] {
    def append(x: (A, B, C, D, E, F), y: G) = (x._1, x._2, x._3, x._4, x._5, x._6, y)
    def init(x: (A, B, C, D, E, F, G)) = (x._1, x._2, x._3, x._4, x._5, x._6)
    def last(y: (A, B, C, D, E, F, G)) = y._7
  }
  implicit def tupleOps7[A, B, C, D, E, F, G, H] = new TupleOps[(A, B, C, D, E, F, G), H, (A, B, C, D, E, F, G, H)] {
    def append(x: (A, B, C, D, E, F, G), y: H) = (x._1, x._2, x._3, x._4, x._5, x._6, x._7, y)
    def init(x: (A, B, C, D, E, F, G, H)) = (x._1, x._2, x._3, x._4, x._5, x._6, x._7)
    def last(y: (A, B, C, D, E, F, G, H)) = y._8
  }
  implicit def tupleOps8[A, B, C, D, E, F, G, H, I] = new TupleOps[(A, B, C, D, E, F, G, H), I, (A, B, C, D, E, F, G, H, I)] {
    def append(x: (A, B, C, D, E, F, G, H), y: I) = (x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, y)
    def init(x: (A, B, C, D, E, F, G, H, I)) = (x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8)
    def last(y: (A, B, C, D, E, F, G, H, I)) = y._9
  }
  implicit def tupleOps9[A, B, C, D, E, F, G, H, I, J] = new TupleOps[(A, B, C, D, E, F, G, H, I), J, (A, B, C, D, E, F, G, H, I, J)] {
    def append(x: (A, B, C, D, E, F, G, H, I), y: J) = (x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9, y)
    def init(x: (A, B, C, D, E, F, G, H, I, J)) = (x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9)
    def last(y: (A, B, C, D, E, F, G, H, I, J)) = y._10
  }
  implicit def tupleOps10[A, B, C, D, E, F, G, H, I, J, K] = new TupleOps[(A, B, C, D, E, F, G, H, I, J), K, (A, B, C, D, E, F, G, H, I, J, K)] {
    def append(x: (A, B, C, D, E, F, G, H, I, J), y: K) = (x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9, x._10, y)
    def init(x: (A, B, C, D, E, F, G, H, I, J, K)) = (x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9, x._10)
    def last(y: (A, B, C, D, E, F, G, H, I, J, K)) = y._11
  }
  implicit def tupleOps11[A, B, C, D, E, F, G, H, I, J, K, L] = new TupleOps[(A, B, C, D, E, F, G, H, I, J, K), L, (A, B, C, D, E, F, G, H, I, J, K, L)] {
    def append(x: (A, B, C, D, E, F, G, H, I, J, K), y: L) = (x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9, x._10, x._11, y)
    def init(x: (A, B, C, D, E, F, G, H, I, J, K, L)) = (x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9, x._10, x._11)
    def last(y: (A, B, C, D, E, F, G, H, I, J, K, L)) = y._12
  }
  implicit def tupleOps12[A, B, C, D, E, F, G, H, I, J, K, L, M] = new TupleOps[(A, B, C, D, E, F, G, H, I, J, K, L), M, (A, B, C, D, E, F, G, H, I, J, K, L, M)] {
    def append(x: (A, B, C, D, E, F, G, H, I, J, K, L), y: M) = (x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9, x._10, x._11, x._12, y)
    def init(x: (A, B, C, D, E, F, G, H, I, J, K, L, M)) = (x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9, x._10, x._11, x._12)
    def last(y: (A, B, C, D, E, F, G, H, I, J, K, L, M)) = y._13
  }
  implicit def tupleOps13[A, B, C, D, E, F, G, H, I, J, K, L, M, N] = new TupleOps[(A, B, C, D, E, F, G, H, I, J, K, L, M), N, (A, B, C, D, E, F, G, H, I, J, K, L, M, N)] {
    def append(x: (A, B, C, D, E, F, G, H, I, J, K, L, M), y: N) = (x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9, x._10, x._11, x._12, x._13, y)
    def init(x: (A, B, C, D, E, F, G, H, I, J, K, L, M, N)) = (x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9, x._10, x._11, x._12, x._13)
    def last(y: (A, B, C, D, E, F, G, H, I, J, K, L, M, N)) = y._14
  }
  implicit def tupleOps14[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = new TupleOps[(A, B, C, D, E, F, G, H, I, J, K, L, M, N), O, (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)] {
    def append(x: (A, B, C, D, E, F, G, H, I, J, K, L, M, N), y: O) = (x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9, x._10, x._11, x._12, x._13, x._14, y)
    def init(x: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)) = (x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9, x._10, x._11, x._12, x._13, x._14)
    def last(y: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)) = y._15
  }
  implicit def tupleOps15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = new TupleOps[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O), P, (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)] {
    def append(x: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O), y: P) = (x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9, x._10, x._11, x._12, x._13, x._14, x._15, y)
    def init(x: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)) = (x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9, x._10, x._11, x._12, x._13, x._14, x._15)
    def last(y: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)) = y._16
  }
  implicit def tupleOps16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = new TupleOps[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P), Q, (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)] {
    def append(x: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P), y: Q) = (x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9, x._10, x._11, x._12, x._13, x._14, x._15, x._16, y)
    def init(x: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)) = (x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9, x._10, x._11, x._12, x._13, x._14, x._15, x._16)
    def last(y: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)) = y._17
  }
  implicit def tupleOps17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = new TupleOps[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q), R, (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)] {
    def append(x: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q), y: R) = (x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9, x._10, x._11, x._12, x._13, x._14, x._15, x._16, x._17, y)
    def init(x: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)) = (x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9, x._10, x._11, x._12, x._13, x._14, x._15, x._16, x._17)
    def last(y: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)) = y._18
  }
  implicit def tupleOps18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = new TupleOps[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R), S, (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)] {
    def append(x: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R), y: S) = (x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9, x._10, x._11, x._12, x._13, x._14, x._15, x._16, x._17, x._18, y)
    def init(x: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)) = (x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9, x._10, x._11, x._12, x._13, x._14, x._15, x._16, x._17, x._18)
    def last(y: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)) = y._19
  }
  implicit def tupleOps19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = new TupleOps[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S), T, (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)] {
    def append(x: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S), y: T) = (x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9, x._10, x._11, x._12, x._13, x._14, x._15, x._16, x._17, x._18, x._19, y)
    def init(x: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)) = (x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9, x._10, x._11, x._12, x._13, x._14, x._15, x._16, x._17, x._18, x._19)
    def last(y: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)) = y._20
  }
  implicit def tupleOps20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = new TupleOps[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T), U, (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)] {
    def append(x: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T), y: U) = (x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9, x._10, x._11, x._12, x._13, x._14, x._15, x._16, x._17, x._18, x._19, x._20, y)
    def init(x: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)) = (x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9, x._10, x._11, x._12, x._13, x._14, x._15, x._16, x._17, x._18, x._19, x._20)
    def last(y: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)) = y._21
  }
  implicit def tupleOps21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = new TupleOps[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U), V, (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)] {
    def append(x: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U), y: V) = (x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9, x._10, x._11, x._12, x._13, x._14, x._15, x._16, x._17, x._18, x._19, x._20, x._21, y)
    def init(x: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)) = (x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9, x._10, x._11, x._12, x._13, x._14, x._15, x._16, x._17, x._18, x._19, x._20, x._21)
    def last(y: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)) = y._22
  }
}