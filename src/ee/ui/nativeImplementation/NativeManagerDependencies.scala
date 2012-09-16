package ee.ui.nativeImplementation

import ee.ui.application.ImplicitDependencies
import NativeManagerDependencies.di
import ee.ui.nativeElements.Window
import ee.ui.nativeElements.Stage
import ee.ui.nativeElements.Scene

trait NativeManagerDependencies {
  def windowManager: NativeManager[Window, _ <: NativeImplementation]
  def stageManager: NativeManager[Stage, _ <: NativeImplementation]
  def sceneManager: NativeManager[Scene, _ <: NativeImplementation]
}

trait ImplicitNativeManagerDependencies {
  import NativeManagerDependencies.di

  implicit def implicitWindowManager: NativeManager[Window, _ <: NativeImplementation] = di.windowManager
  implicit def implicitStageManager: NativeManager[Stage, _ <: NativeImplementation] = di.stageManager
  implicit def implicitSceneManager: NativeManager[Scene, _ <: NativeImplementation] = di.sceneManager

}

object NativeManagerDependencies extends ImplicitDependencies[NativeManagerDependencies]