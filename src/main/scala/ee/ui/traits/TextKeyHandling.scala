package ee.ui.traits

import ee.ui.primitives.KeyCode._
import ee.ui.application.OperatingSystem

trait TextKeyHandling extends TabFocusHandling { self:KeyTraits =>
  	override val bindings = textKeyBindings

  	protected val textKeyBindings = tabFocusBindings ++ 
  	Map(
	    // caret movement
        RIGHT -> forward, KP_RIGHT -> forward,
        LEFT -> backward, KP_LEFT -> backward,
        //TODO move to singleLineText
        UP -> home, KP_UP -> home,
        HOME -> home,
        DOWN -> end, KP_DOWN -> end,
        END -> end,
        ENTER -> fire,
        // deletion
        BACK_SPACE -> deletePreviousChar,
        SHIFT + BACK_SPACE -> deletePreviousChar,
        DELETE -> deleteNextChar,
        // cut/copy/paste
        CUT -> cut, SHIFT + DELETE -> cut,
        COPY -> copy, 
        PASTE -> paste, SHIFT + INSERT -> paste,
        // selection
        SHIFT + RIGHT -> selectForward, SHIFT + KP_RIGHT -> selectForward,
        SHIFT + LEFT -> selectBackward, SHIFT + KP_LEFT -> selectBackward,
        SHIFT + UP -> selectHome, SHIFT + KP_UP -> selectHome,
        SHIFT + DOWN -> selectEnd, SHIFT + KP_DOWN -> selectEnd) ++ 
        operatingSystemSpecificBindings
	
	protected val operatingSystemSpecificBindings = 
	  OperatingSystem.current match {
	  case OperatingSystem.WINDOWS => windowsBindings
	  case OperatingSystem.MAC => macBindings
	  case OperatingSystem.LINUX => linuxBindings
	  case OperatingSystem.SOLARIS => solarisBindings
	  case OperatingSystem.UNKNOWN => unknownBindings
	}
	
	private val windowsBindings = nonLinuxBindings
	private val solarisBindings = nonLinuxBindings
	private val unknownBindings = nonLinuxBindings
	
	private val linuxBindings = nonMacBindings ++ Map(
	    CTRL + Z -> undo,
                CTRL + SHIFT + Z -> redo
	    )
	    
	    private val macBindings = Map(
	        SHIFT + HOME -> selectHomeExtend,
            SHIFT + END -> selectEndExtend,
            META + HOME -> home,
            META + END -> end,
            META + LEFT -> home, META + KP_LEFT -> home,
            META + RIGHT -> end, META + KP_RIGHT -> end,
            ALT + LEFT -> previousWord, ALT + KP_LEFT -> previousWord,
            ALT + RIGHT -> nextWord, ALT + KP_RIGHT -> nextWord,
            META + DELETE -> deleteNextWord,
            META + BACK_SPACE -> deletePreviousWord,
            META + X -> cut,
            META + C -> copy, META + INSERT -> copy,
            META + V -> paste,
            SHIFT + META + HOME -> selectHome,
            SHIFT + META + END -> selectEnd,
            SHIFT + META + LEFT -> selectHomeExtend, SHIFT + META + KP_LEFT -> selectHomeExtend,
            SHIFT + META + RIGHT -> selectEndExtend, SHIFT + META + KP_RIGHT -> selectEndExtend,
            META + A -> selectAll,
            SHIFT + ALT + LEFT -> selectPreviousWord, SHIFT + ALT + KP_LEFT -> selectPreviousWord,
            SHIFT + ALT + RIGHT -> selectNextWord, SHIFT + ALT + KP_RIGHT -> selectNextWord,
            META + Z -> undo,
            SHIFT + META + Z -> redo)
            
	private val nonMacBindings = Map(
	    SHIFT + HOME -> selectHome,
            SHIFT + END -> selectEnd,
            CTRL + HOME -> home,
            CTRL + END -> end,
            CTRL + LEFT -> previousWord, CTRL + KP_LEFT -> previousWord,
            CTRL + RIGHT -> nextWord, CTRL + KP_RIGHT -> nextWord,
            CTRL + H -> deletePreviousChar,
            CTRL + DELETE -> deleteNextWord,
            CTRL + BACK_SPACE -> deletePreviousWord,
            CTRL + X -> cut,
            CTRL + C -> copy,
            CTRL + INSERT -> copy,
            CTRL + V -> paste,
            CTRL + SHIFT + HOME -> selectHome,
            CTRL + SHIFT + END -> selectEnd,
            CTRL + SHIFT + LEFT -> selectPreviousWord, CTRL + SHIFT + KP_LEFT -> selectPreviousWord,
            CTRL + SHIFT + RIGHT -> selectNextWord, CTRL + SHIFT + KP_RIGHT -> selectNextWord,
            CTRL + A -> selectAll,
            CTRL + BACK_SLASH -> unselect
	)
	
	private val nonLinuxBindings = nonMacBindings ++ Map(
	    CTRL + Z -> undo,
                CTRL + Y -> redo
	)            

	def copy() = {}
	def cut() = {}
	def deleteNextWord() = {}
	def deletePreviousChar() = {}
	def deletePreviousWord() = {}
	def end() = {}
	def forward() = {}
	def home() = {}
	def nextWord() = {}
	def paste() = {}
	def previousWord() = {}
	def selectAll() = {}
	def selectEnd() = {}
	def selectEndExtend() = {}
	def selectHome() = {}
	def selectHomeExtend() = {}
	def selectNextWord() = {}
	def selectPreviousWord() = {}
	def unselect() = {}
	def undo() = {}
	def redo() = {}
	def backward() = {}
	def fire() = {}
	def deleteNextChar() = {}
	def selectForward() = {}
	def selectBackward() = {}
	
}