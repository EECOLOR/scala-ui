package ee.ui.application

sealed abstract class Modality

object Modality {
    
    /**
     * Defines a top-level window that is not modal and does not block any other
     * window.
     */
    object NONE extends Modality

    /**
     * Defines a modal window that block events from being delivered to its
     * entire owner window hierarchy.
     *
     * Note: A Stage with modality set to WINDOW_MODAL, but its owner is null,
     * is treated as if its modality is set to NONE.
     */
    object WINDOW_MODAL extends Modality

    /**
     * Defines a modal window that blocks events from being delivered to any
     * other application window.
     */
    object APPLICATION_MODAL extends Modality
}