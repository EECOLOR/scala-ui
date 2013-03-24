package utils

import org.specs2.matcher.StandardMatchResults

class TypeTest[A] extends StandardMatchResults {
  def forInstance[B](instance:B)(implicit ev: A =:= B) = ok
}
object TypeTest {
  def apply[A]() = new TypeTest[A]
}