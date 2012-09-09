package ee.ui.application.javafx

import com.sun.javafx.tk.TKStage

class Window extends ee.ui.application.Window with Toolkit {
    type WindowType = Window
    
    def internalStage:Option[TKStage] = None
}