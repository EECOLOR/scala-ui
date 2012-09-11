package ee.ui.application.javafx

import javafx.scene.image.{Image => JavaFxImage}
import ee.ui.primitives.Image

object Converters {
	def convertImage(image:Image):JavaFxImage = new JavaFxImage(image.url)
}