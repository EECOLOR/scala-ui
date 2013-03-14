package ee.ui.application

import org.specs2.Specification
import ee.ui.application.details.ApplicationDependencies
import ee.ui.application.details.ImplementationContract
import ee.ui.layout.LayoutEngine
import ee.ui.application.details.PulseHandler
import ee.ui.layout.DefaultLayoutEngine
import ee.ui.application.details.DefaultPulseHandler
import ee.ui.application.details.Launcher
import ee.ui.events.PulseEvent
import ee.ui.display.implementation.DisplayImplementationHandler
import ee.ui.system.ClipBoard
import ee.ui.display.text.TextHelper
import ee.ui.members.ReadOnlyEvent
import ee.ui.members.Event
import ee.ui.events.PulseEvent
import scala.actors.Actor
import scala.actors.TIMEOUT
import ee.ui.system.ClipBoard

object ImplementationEngine extends Specification {

  def is = "Implementation engine specification".title ^
    hide ^ end
    //show ^ end

  def hide = args(xonly = true) ^ show ^ end

  def show =
    """ |Introduction
        |
        |An implementation engine will supply the following elements that are specific to the 
        |operating system (or device) that the framework is used on. It will provide elements 
        |like rendering, text display, clipboard access etc.
        |
        |This engine should provides the minimal set of elements required by the ui framework 
        |to function.
      """.stripMargin ^
      br ^
      "The first step is to create an engine specific launcher" ^
      { // Engine specific launcher

        abstract class TestEngineLauncher extends ApplicationLauncher {

        }

        ok
      } ^
      br ^
      "The ApplicationLauncher requires the application to fulfill the ApplicationDependencies" ^
      { // Application dependencies

        abstract class TestEngineLauncher extends ApplicationLauncher {

          lazy val applicationDependencies = new ApplicationDependencies {

            val implementationContract: ImplementationContract = ???

            // allows you to override the default LayoutEngine implementation
            override val layoutEngine: LayoutEngine = DefaultLayoutEngine

            // allows you to override the default PulseHandler provider
            override val createPulseHandler: (Application) => PulseHandler =
              { application =>
                new DefaultPulseHandler(application, implementationContract.displayImplementationHandler, layoutEngine)
              }
          }
        }

        ok
      } ^
      br ^ "The ImplementationContract specifies the mandatory components that the engine needs to provide" ^
      { // Implementation contract

        lazy val implementationContract =
          new ImplementationContract {
            // The launcher launches the actual application
            val launcher: Launcher = ???

            /* 
             The pulse event is fired when the application needs to perform an update cycle. Check 
             the DefaultPulseHandler for more information
            */
            val pulseEvent: PulseEvent = ???

            // This handles all display related elements, Text, Group, Rectangle, etc.
            val displayImplementationHandler: DisplayImplementationHandler = ???

            // Provides the clipboard
            val systemClipBoard: ClipBoard = ???

            // Text and font helpers
            val textHelper: TextHelper = ???
          }

        ok
      } ^
      br ^ """|The launcher creates the application. It should at least implement a launch 
              |method and a fire the launchComplete event.""".stripMargin ^
      { // Launcher

        lazy val launcherExample =
          new Launcher {
            val launchComplete = Event[Application]

            def launch(args: Array[String])(implicit createApplication: () => Application): Unit = {

              // create the process that the application will run in

              // create the application itself
              val application = createApplication()

              // call this method within the main application thread
              application.init()

              // this will cause the application to create elements and windows 
              application.start()

              // call this to make the ui framework run
              launchComplete fire application

            }
            
            def exit(application:Application) = {
              // if the process is stopped from the outside call this to notify the application
              application.stop()
            }
          }

        ok
      } ^
      br ^ "The pulse events notifies the system (PulseHandler) to perform a round of actions (most notably layout)." ^
      { // PulseEvent

        lazy val examplePulseEvent =
          new PulseEvent { self =>

            val timer = new Actor {
              def act =
                loop(
                  reactWithin(100) {
                    case TIMEOUT => self.fire()
                  })
            }
            timer.start
          }

        ok
      } ^
      br ^ """|The display implementation handler takes care of all engine specific implementations 
              |and is the bulk of the work for a custom implementation engine""".stripMargin ^
      { // DisplayImplementationHandler
        lazy val exampleDisplayImplementationHandler =
          new DisplayImplementationHandler {

            /*
             * The Scene and Window update methods retrieve a 'contract' because apart from
             * processing the changes, the engine is required to trigger events on the underlying 
             * object as well.
             */
            protected def update(o: ee.ui.display.implementation.SceneContract): Unit = ???
            protected def update(o: ee.ui.display.implementation.WindowContract): Unit = ???

            // The changes in these elements need to be represented in their 'real' counterparts
            protected def update(o: ee.ui.display.shape.Rectangle): Unit = ???
            protected def update(o: ee.ui.display.Text): Unit = ???
            protected def update(o: ee.ui.display.Group): Unit = ???

            // These are special methods to notify the engine when a window should be shown or hidden
            protected def hide(o: ee.ui.display.implementation.WindowContract): Unit = ???
            protected def show(o: ee.ui.display.implementation.WindowContract): Unit = ???
          }

        ok
      } ^
      br ^ "The system clip board" ^
      { // ClipBoard
        lazy val exampleClipBoard =
          new ClipBoard {

            // Determines is the clip board contains something of the given data format
            def contains(key: ee.ui.system.DataFormat): Boolean = ???

            // Retrieves the given the element corresponding to the given data format
            def get(key: ee.ui.system.DataFormat): Option[AnyRef] = ???

            // Puts the content onto the clip board, returns true if successful 
            def set(key: ee.ui.system.DataFormat, value: AnyRef): Boolean = ???
          }

        ok
      } ^
      br ^ "The text helper" ^
      { // ClipBoard
        lazy val exampleTextHelper =
          new TextHelper {

            // Retrieves the caret index within the given text field for the given point
            def getCaretIndex(text: ee.ui.display.Text, position: ee.ui.primitives.Point): Int = ???
            
            // Retrieves the point of the caret based on the index in the given text field
            def getCaretPosition(text: ee.ui.display.Text, index: Int): ee.ui.primitives.Point = ???
            
            // Retrieves the metrics of the given font
            def getFontMetrics(font: ee.ui.primitives.Font): ee.ui.primitives.FontMetrics = ???
          }

        ok
      } ^
      end
}