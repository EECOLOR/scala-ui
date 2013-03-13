package ee.ui.application

import org.specs2.Specification
import ee.ui.display.Window

object ApplicationSpecification extends Specification {
  def is = "Implementation engine specification".title ^
    hide ^ end
  //show ^ end

  def hide = args(xonly = true) ^ show ^ end

  def show =
    """ Introduction
    
      
      """ ^ "" ^
      { // 

        object MyFile {

          abstract class MyApplication extends Application {
            def start(window:Window) = {
              
            }
          }

          object MyApplicationLauncher extends TestEngineLauncher {
              def createApplication = new MyApplication with Windows
          }
        }

        ok
      } ^
      end

  abstract class TestEngineLauncher extends ApplicationLauncher {
    lazy val applicationDependencies = ???
  }
}