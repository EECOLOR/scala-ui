package ee.ui.traits

import scala.annotation.implicitNotFound

trait RestrictedAccess {
	implicit protected object GrantAccess extends AccessRestriction
}

object RestrictedAccess extends AccessRestriction

@implicitNotFound("This is not allowed for normal use. If you however know what you are doing you can implement the RestrictedAccess trait or use the RestrictedAccess object. This message is common for properties that are affected by layout")
trait AccessRestriction