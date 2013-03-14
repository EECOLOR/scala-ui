package ee.ui.application

import org.specs2.Specification
import ee.ui.display.Window
import ee.ui.display.Scene
import ee.ui.display.Group
import ee.ui.display.Text

object ApplicationSpecification extends Specification {
  def is = "Implementation engine specification".title ^
    hide ^ end
  //show ^ end

  def hide = args(xonly = true) ^ show ^ end

  def show =
    """ |Introduction
        |
        |In order to create an application you need two parts:
        |
        |1. The application itself
        |2. A launcher for the application
      """.stripMargin ^
      br ^
      { // Hello world

        object MyFile {

          // The application class, note that it needs to be abstract
          abstract class MyApplication extends Application {

            def start(window: Window) = {

              // create a scene
              window.scene = new Scene {

                // give the scene a root
                root = new Text {
                  // set the text attribute
                  text = "Hello World"

                  // add a mouse down handler
                  onMouseDown {
                    println("Mouse down on hello world")
                  }
                }
              }

              // show the window
              show(window)
            }
          }

          // The launcher (provides a main method)
          object MyApplicationLauncher extends TestEngineLauncher {

            /*
             * Construct an instance of the application, note that we add window support
             * supplied by the engine (TestEngine) in this case
             */
            def createApplication = new MyApplication with Engine
          }
        }

        ok
      } ^
      end
}