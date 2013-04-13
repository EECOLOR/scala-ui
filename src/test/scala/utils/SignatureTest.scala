package utils

import org.specs2.matcher.StandardMatchResults

object SignatureTest extends StandardMatchResults {
  def apply[Owner, ReturnType](code: Owner => ReturnType) = ok
  def apply[Owner, Argument1Type, ReturnType](code: (Owner, Argument1Type) => ReturnType) = ok
}