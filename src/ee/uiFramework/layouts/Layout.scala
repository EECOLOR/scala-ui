package ee.uiFramework.layouts
import ee.uiFramework.components.Container

/**
 * Base trait for all layouts
 */
trait Layout {
	def layoutChildren(container:Container)
}

object NoLayout extends Layout {
    /**
     * Performs no action
     */
	def layoutChildren(container:Container) = {/*do nothing*/}
}