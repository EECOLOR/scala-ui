package utils

import org.specs2.matcher.StandardMatchResults

class TypeTest[A] extends StandardMatchResults {
  def forInstance[B](instance:B)(implicit ev: A =:= B) = ok
}
object TypeTest {
  def apply[A]() = new TypeTest[A]
}
object SubtypeTest extends StandardMatchResults {
  def apply[A <: <:<[_, _]]()(implicit a: A) = ok
}
class MemberTypeTest[Type, ExpectedMemberType] extends StandardMatchResults {
  def forMember[ReturnType](code: Type => ReturnType)(implicit ev: ExpectedMemberType =:= ReturnType) = ok
}
object MemberTypeTest {
  def apply[Type, ExpectedMemberType]() = new MemberTypeTest[Type, ExpectedMemberType]
}