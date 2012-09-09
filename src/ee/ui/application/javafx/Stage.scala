package ee.ui.application.javafx

import ee.ui.application.StageStyle
import javafx.stage.{StageStyle => JavaFxStageStyle}
import javafx.stage.{Modality => JavaFxModality}
import ee.ui.application.Modality
import com.sun.javafx.tk.TKStage
import scala.collection.mutable.ListBuffer
import ee.ui.primitives.Image
import collection.JavaConversions._

class Stage(val primary:Boolean = false, val defaultStyle:StageStyle = StageStyle.DECORATED) extends Window with ee.ui.application.Stage {
    
    private def applyToInteralStage[T](method:(TKStage, T) => Unit)(value:T):Unit =
        internalStage foreach(method(_, value))
    
    //property forNewValue applyToInternalStage((stage, value) => stage setProperty value)
    resizable forNewValue applyToInteralStage(_ setResizable _)
    fullScreen forNewValue applyToInteralStage(_ setFullScreen _)
    iconified forNewValue applyToInteralStage(_ setIconified _)
    title forNewValue applyToInteralStage(_ setTitle _.orNull)
    
    minWidth forNewValue applyToInteralStage { (stage, n) =>
        stage setMinimumSize (n.toInt, minHeight.toInt)
    }
    minHeight forNewValue applyToInteralStage { (stage, n) =>
    	stage setMinimumSize (minWidth.toInt, n.toInt)
    }
    
    maxWidth forNewValue applyToInteralStage { (stage, n) =>
    stage setMaximumSize (n.toInt, maxHeight.toInt)
    }
    maxHeight forNewValue applyToInteralStage { (stage, n) =>
    stage setMaximumSize (maxWidth.toInt, n.toInt)
    }
    
	override lazy val internalStage:Option[TKStage] = {
	    // Setup the peer
            val window = owner
            
            val ownerStage = window flatMap(_.internalStage) orNull
            
            val tkStage = toolkit.createTKStage(getStyle(), primary, getModality(), ownerStage)
            tkStage setImportant true
            
            // Finish initialization
            tkStage setResizable resizable
            tkStage setFullScreen fullScreen
            tkStage setIconified iconified
            tkStage setTitle title.orNull
            tkStage setMinimumSize(minWidth.toInt, minHeight.toInt)
            tkStage setMaximumSize(maxWidth.toInt, maxHeight.toInt)
            tkStage setIcons icons

            // Insert this into stages so we have a references to all created stages
            ee.ui.application.Stage.stages += this
            
            Some(tkStage)
	}
    
    private def getStyle():JavaFxStageStyle = style.value match {
        case StageStyle.DECORATED => JavaFxStageStyle.DECORATED
        case StageStyle.TRANSPARENT => JavaFxStageStyle.TRANSPARENT
        case StageStyle.UNDECORATED => JavaFxStageStyle.UNDECORATED
        case StageStyle.UTILITY => JavaFxStageStyle.UTILITY
    }
    
    private def getModality():JavaFxModality = modality.value match {
    	case Modality.APPLICATION_MODAL => JavaFxModality.APPLICATION_MODAL
    	case Modality.NONE => JavaFxModality.NONE
    	case Modality.WINDOW_MODAL => JavaFxModality.WINDOW_MODAL
    }
}