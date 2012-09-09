package ee.ui.application

sealed abstract class StageStyle()

object StageStyle {
	
    /**
     * Defines a normal {@code Stage} style with a solid white background and platform decorations.
     */
    object DECORATED extends StageStyle

    /**
     * Defines a {@code Stage} style with a solid white background and no decorations.
     */
    object UNDECORATED extends StageStyle

    /**
     * Defines a {@code Stage} style with a transparent background and no decorations.
     */
    object TRANSPARENT extends StageStyle

    /**
     * Defines a {@code Stage} style with a solid white background and minimal
     * platform decorations used for a utility window.
     */
    object UTILITY extends StageStyle

}