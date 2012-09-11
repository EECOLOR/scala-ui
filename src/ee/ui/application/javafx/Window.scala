package ee.ui.application.javafx

import com.sun.javafx.tk.TKStage
import ee.ui.impl.NativeImplementation

class Window(val implemented:ee.ui.application.Window) extends NativeImplementation with Toolkit {
    
    def internalStage:Option[TKStage] = None
    
    def init = Unit
}