package ee.ui.application

import ee.ui.nativeElements.Window
import ee.ui.nativeElements.Stage
import ee.ui.nativeImplementation.NativeManager
import ee.ui.nativeImplementation.NativeImplementation

trait Dependencies {
    def launcher:Launcher
    def application:Application
    
    def windowManager:NativeManager[Window, _ <: NativeImplementation] 
	def stageManager:NativeManager[Stage, _ <: NativeImplementation] 
}

