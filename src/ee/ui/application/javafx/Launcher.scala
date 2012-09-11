package ee.ui.application.javafx

import ee.ui.application.Application
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.CountDownLatch
import ee.ui.application.ImplicitDependencies
import com.sun.javafx.application.PlatformImpl
import com.sun.javafx.application.ParametersImpl
import scala.util.control.ControlThrowable
import com.sun.javafx.PlatformUtil
import javafx.application.ConditionalFeature
import java.security.AccessController
import java.security.PrivilegedAction
import java.lang.{Boolean => JBoolean}

object Launcher extends ee.ui.application.Launcher with ImplicitDependencies {

    def launch(args:Array[String]):Unit = {
        val launchCalled = new AtomicBoolean
        
        if (launchCalled getAndSet true) 
            throw new IllegalStateException("Application launch must not be called more than once")
        
        // Create a new Launcher thread and then wait for that thread to finish
        var launchException:Option[Exception] = None
        val launchLatch = new CountDownLatch(1);
        val launcherThread = new Thread(new Runnable() {
            def run() {
                try {
                    launchApplication(args)
                } catch {
                    case rte:RuntimeException => 
                        launchException = Some(rte)
                    case err:Error => 
                        launchException = Some(new RuntimeException("Application launch error", err))
                    case ex:Exception => 
                        launchException = Some(new RuntimeException("Application launch exception", ex))
                } finally {
                    launchLatch countDown
                }
            }
        });
        launcherThread setName "JavaFX-Launcher"
        launcherThread start

        // Wait for FX launcher thread to finish before returning to user
        try {
            launchLatch await
        } catch  {
            case ex:InterruptedException => 
                throw new RuntimeException("Unexpected exception: ", ex)
        }

        if (launchException isDefined) {
            throw launchException.get
        }
	}
    
    private def launchApplication(args:Array[String])
    	(implicit createApplication:() => Application) = {
        
        val startupLatch = new CountDownLatch(1)
        
        PlatformImpl startup new Runnable() {
            // Note, this method is called on the FX Application Thread
            def run() {
                startupLatch countDown
            }
        }

        // Wait for FX platform to start
        startupLatch await

        val isStartCalled = new AtomicBoolean
        val isExitCalled = new AtomicBoolean
        val shutdownLatch = new CountDownLatch(1)

        val listener = new PlatformImpl.FinishListener() {
            def idle(implicitExit:Boolean) {
                if (!implicitExit) {
                    return;
                }

                if (isStartCalled get)  shutdownLatch countDown
            }

            def exitCalled() {
                isExitCalled set true
                shutdownLatch countDown
            }
        }
        PlatformImpl addListener listener

        var error:Boolean = false
        
        try {
            // Construct an instance of the application and call its init
            // method on this thread. Then call the start method on the FX thread.
            var app:Application = null
            var constructorError:Throwable = null
            
            if (!isExitCalled.get) {

                try {
                    app = createApplication()
                    // Set startup parameters
                    Parameters.registerParameters(app, new ParametersImpl(args));
                } catch {
                    case ct:ControlThrowable => throw ct
                    case t:Throwable =>
	                    System.err println "Exception in Application constructor"
	                    constructorError = t
	                    error = true
                }
            }
            
	        // Call init method unless exit called or error detected
            val theApp:Application = app
            var initError:Throwable = null
            
            if (!error && !isExitCalled.get) {

                try {
                    // Call the application init method (on the Launcher thread)
                    theApp init
                } catch {
                    case ct:ControlThrowable => throw ct
                    case t:Throwable =>
	                    System.err println "Exception in Application init method"
	                    initError = t
	                    error = true
                }
            }

            var startError:Throwable = null
            
            // Call start method unless exit called or error detected
            if (!error && !isExitCalled.get) {

                // Call the application start method on FX thread
                PlatformImpl runAndWait new Runnable() {
                    def run() = {
                        try {
                            isStartCalled set true

                            // Create primary stage and call application start method
                            val primaryStage = new ee.ui.application.Stage(true)
                            theApp start primaryStage
                        } catch {
                    case ct:ControlThrowable => throw ct
                    case t:Throwable =>
                            System.err println "Exception in Application start method"
                            startError = t
                            error = true
                        }
                    }
                }
            }

            if (!error) {
                shutdownLatch await
            }

            var stopError:Throwable = null
            
            // Call stop method if start was called
            if (isStartCalled get) {
                // Call Application stop method on FX thread
                PlatformImpl runAndWait new Runnable() {
                    def run() {
                        try {
                            theApp stop
                        } catch {
                    case ct:ControlThrowable => throw ct
                    case t:Throwable =>
                            System.err println "Exception in Application stop method"
                            stopError = t
                            error = true
                        }
                    }
                }
            }

            // RT-19600: workaround for Mac crash on exit with J2D pipeline
            val isMac = PlatformUtil.isMac
            if (isMac) {
                // Exit if using the J2D pipeline. Note that currently SCENE3D
                // is false only if we are running the J2D pipeline.
                val exitOnClose = !PlatformImpl.isSupported(ConditionalFeature.SCENE3D);
                val keepAlive = AccessController doPrivileged new PrivilegedAction[Boolean] {
                    def run():Boolean = JBoolean getBoolean "javafx.keepalive"
                }
                
                if (exitOnClose && !keepAlive) {
                    if (constructorError != null) {
                        constructorError.printStackTrace();
                    } else if (initError != null) {
                        initError.printStackTrace();
                    } else if(startError != null) {
                        startError.printStackTrace();
                    } else if (stopError != null) {
                        stopError.printStackTrace();
                    }
                    System.err println "JavaFX application launcher: calling System.exit"
                    System exit 0
                }
            }
            // END OF WORKAROUND

            if (error) {
                if (constructorError != null) {
                    val msg = "Unable to construct Application instance"
                    throw new RuntimeException(msg, constructorError)
                } else if (initError != null) {
                    val msg = "Exception in Application init method"
                    throw new RuntimeException(msg, initError)
                } else if(startError != null) {
                    val msg = "Exception in Application start method"
                    throw new RuntimeException(msg, startError)
                } else if (stopError != null) {
                    val msg = "Exception in Application stop method"
                    throw new RuntimeException(msg, stopError)
                }
            }
        } finally {
            PlatformImpl removeListener listener
            // Workaround until RT-13281 is implemented
            // Don't call exit if we detect an error in javaws mode
//            PlatformImpl.tkExit();
            val isJavaws = System.getSecurityManager() != null;
            if (error && isJavaws) {
                System.err println "Workaround until RT-13281 is implemented: keep toolkit alive"
            } else {
                PlatformImpl tkExit
            }
        }
    }
}