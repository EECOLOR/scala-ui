package ee.ui.dummy.nativeElements

import ee.ui.nativeImplementation.NativeManagerDependencies
import ee.ui.nativeImplementation.NativeManager
import ee.ui.nativeElements.Window
import ee.ui.nativeElements.Stage
import ee.ui.nativeElements.Scene
import ee.ui.Node
import ee.ui.Group

trait DummyNativeManagerDependencies extends NativeManagerDependencies {
  implicit def windowManager = DummyWindowManager
  implicit def stageManager = DummyStageManager
  implicit def sceneManager = DummySceneManager
  implicit def groupManager = DummyGroupManager
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

object DummyGroupManager extends NativeManager[Group, DummyGroup] {
	protected def createInstance(element: Group) = new DummyGroup(element)
}