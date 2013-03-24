package ee.ui.system

import scala.annotation.implicitNotFound

@implicitNotFound("This is not allowed for normal use. If you however know what you are doing you can use the RestrictedAccess object.")
trait AccessRestriction

object RestrictedAccess extends AccessRestriction