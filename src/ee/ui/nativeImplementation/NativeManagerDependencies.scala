package ee.ui.nativeImplementation

import ee.ui.application.ImplicitDependencies
import NativeManagerDependencies.di
import ee.ui.nativeElements.Window
import ee.ui.nativeElements.Stage
import ee.ui.nativeElements.Scene
import ee.ui.Node
import ee.ui.Group

trait NativeManagerDependencies {
  def windowManager: NativeManager[Window, _ <: NativeImplementation]
  def stageManager: NativeManager[Stage, _ <: NativeImplementation]
  def sceneManager: NativeManager[Scene, _ <: NativeImplementation]
  def nodeManager: NativeManager[Node, _ <: NativeImplementation]
  def groupManager: NativeManager[Group, _ <: NativeImplementation]
}

trait ImplicitNativeManagerDependencies {
  import NativeManagerDependencies.di

  implicit def implicitWindowManager = di.windowManager
  implicit def implicitStageManager = di.stageManager
  implicit def implicitSceneManager = di.sceneManager
  implicit def implicitNodeManager = di.nodeManager
  implicit def implicitGroupManager = di.groupManager

}

object NativeManagerDependencies extends ImplicitDependencies[NativeManagerDependencies]