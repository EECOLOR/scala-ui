package ee.ui.dummy.nativeElements

import ee.ui.nativeImplementation.NativeManagerDependencies
import ee.ui.nativeImplementation.NativeManager
import ee.ui.nativeElements.Window
import ee.ui.nativeElements.Stage
import ee.ui.nativeElements.Scene

trait DummyNativeManagerDependencies extends NativeManagerDependencies {
  implicit def windowManager = DummyWindowManager
  implicit def stageManager = DummyStageManager
  implicit def sceneManager = DummySceneManager
}

object DummyNativeManagerDependencies extends DummyNativeManagerDependencies

object DummyWindowManager extends NativeManager[Window, DummyWindow] {
  protected def createInstance(element: Window) = new DummyWindow(element)
}

object DummyStageManager extends NativeManager[Stage, DummyStage] {
  protected def createInstance(element: Stage) = new DummyStage(element)
}

object DummySceneManager extends NativeManager[Scene, DummyScene] {
	protected def createInstance(element: Scene) = new DummyScene(element)
}