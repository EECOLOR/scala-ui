package ee.ui.application.javafx

import ee.ui.application.StageStyle
import javafx.stage.{ StageStyle => JavaFxStageStyle }
import javafx.stage.{ Modality => JavaFxModality }
import ee.ui.application.Modality
import com.sun.javafx.tk.TKStage
import scala.collection.mutable.ListBuffer
import ee.ui.primitives.Image
import collection.JavaConversions._
import javafx.scene.image.{ Image => JavaFxImage }
import scala.collection.mutable.Buffer
import ee.ui.impl.NativeImplementation

class Stage(override val implemented: ee.ui.application.Stage) extends Window(implemented) with NativeImplementation with JavaFxManagers {
    
    private def applyToInteralStage[T](method: (TKStage, T) => Unit)(value: T): Unit =
        internalStage foreach (method(_, value))

    //property forNewValue applyToInternalStage((stage, value) => stage setProperty value)
    implemented.resizable forNewValue applyToInteralStage(_ setResizable _)
    implemented.fullScreen forNewValue applyToInteralStage(_ setFullScreen _)
    implemented.iconified forNewValue applyToInteralStage(_ setIconified _)
    implemented.title forNewValue applyToInteralStage(_ setTitle _.orNull)

    implemented.minWidth forNewValue applyToInteralStage { (s, n) =>
        s setMinimumSize (n.toInt, implemented.minHeight.toInt)
    }
    implemented.minHeight forNewValue applyToInteralStage { (s, n) =>
        s setMinimumSize (implemented.minWidth.toInt, n.toInt)
    }

    implemented.maxWidth forNewValue applyToInteralStage { (s, n) =>
        s setMaximumSize (n.toInt, implemented.maxHeight.toInt)
    }
    implemented.maxHeight forNewValue applyToInteralStage { (s, n) =>
        s setMaximumSize (implemented.maxWidth.toInt, n.toInt)
    }

    override lazy val internalStage: Option[TKStage] = {
        // Setup the peer
        val window = implemented.owner

        val ownerStage = window flatMap (_.nativeImplementation.internalStage) orNull
        
        val tkStage = toolkit.createTKStage(getStyle(), implemented.primary, getModality(), ownerStage)
        tkStage setImportant true

        // Finish initialization
        tkStage setResizable implemented.resizable
        tkStage setFullScreen implemented.fullScreen
        tkStage setIconified implemented.iconified
        tkStage setTitle implemented.title.orNull
        tkStage setMinimumSize (implemented.minWidth.toInt, implemented.minHeight.toInt)
        tkStage setMaximumSize (implemented.maxWidth.toInt, implemented.maxHeight.toInt)

        implicit def iToI(image: Image): JavaFxImage = new JavaFxImage("")

        val javaFxIcons = implemented.icons map Converters.convertImage

        tkStage setIcons javaFxIcons

        Some(tkStage)
    }

    private def getStyle(): JavaFxStageStyle = implemented.style.value match {
        case StageStyle.DECORATED => JavaFxStageStyle.DECORATED
        case StageStyle.TRANSPARENT => JavaFxStageStyle.TRANSPARENT
        case StageStyle.UNDECORATED => JavaFxStageStyle.UNDECORATED
        case StageStyle.UTILITY => JavaFxStageStyle.UTILITY
    }

    private def getModality(): JavaFxModality = implemented.modality.value match {
        case Modality.APPLICATION_MODAL => JavaFxModality.APPLICATION_MODAL
        case Modality.NONE => JavaFxModality.NONE
        case Modality.WINDOW_MODAL => JavaFxModality.WINDOW_MODAL
    }
}
