package ee.ui.application.javafx

import com.sun.javafx.tk.{Toolkit => JavaFxToolkit}



trait Toolkit {
	def toolkit = JavaFxToolkit getToolkit
}