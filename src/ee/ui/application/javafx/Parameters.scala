package ee.ui.application.javafx

import ee.ui.application.Application
import scala.collection.mutable
import javafx.application.Application.{Parameters => JfxParameters}

object Parameters {
    val params = mutable.Map[Application, JfxParameters]()
    
    def getParameters(a:Application):JfxParameters = params(a)

    def registerParameters(a:Application, p:JfxParameters) = params += a -> p
}