package ee.ui.display.text

import ee.ui.primitives.KeyCode._
import ee.ui.system.ClipBoard
import java.text.BreakIterator
import ee.ui.system.OperatingSystem
import ee.ui.display.traits.KeyBindings
import ee.ui.display.traits.KeyEvents
import ee.ui.system.ClipBoard

trait TextKeyHandling extends TextKeyHandlers with TextKeyBindings { self: KeyEvents with TextInputLike =>

}

trait TextKeyHandlers { self: TextInputLike =>

  val systemClipBoard:ClipBoard
  val os:OperatingSystem
  
  def copy() =
    if (selectedText.length > 0) systemClipBoard.string = selectedText

  def cut() = {
    copy()
    deleteSelection()
  }

  def paste() =
    systemClipBoard.string foreach (replaceSelection(_))

  def home() = positionCaret(0)
  def end() = positionCaret(text.length)

  def forward() =
    positionCaret(
        selection.value map (_.end) getOrElse (1 + caretIndex))

  def backward() =
    positionCaret( 
      selection.value map (_.start) getOrElse (caretIndex - 1))

  def wordsAfterCaret = {
    var str: String = text
    val breakIterator = BreakIterator.getWordInstance
    breakIterator setText str
    breakIterator following caretIndex

    val breaks = (Stream continually breakIterator.next) takeWhile (_ != BreakIterator.DONE)
    val words = breaks filter (str(_).isLetterOrDigit)
    words
  }

  def wordsBeforeCaret = {
    var str: String = text
    val breakIterator = BreakIterator.getWordInstance
    breakIterator setText str
    breakIterator preceding caretIndex

    val breaks = (Stream continually breakIterator.previous) takeWhile (_ != BreakIterator.DONE)
    val words = breaks filter (i => i > 0 && str(i - 1).isLetterOrDigit)
    words
  }

  def nextWord() =
    positionCaret(wordsAfterCaret.headOption getOrElse text.length)

  def previousWord() =
    positionCaret(wordsBeforeCaret.headOption getOrElse 0)

  def findCaretPositions(f: () => Unit): (Int, Int) = {
    val first: Int = caretIndex
    f()
    val last: Int = caretIndex

    (first, last)
  }

  def deleteNextWord() = {
    val (start, end) = findCaretPositions(nextWord)
    deleteText(start, end)
  }

  def deletePreviousWord() = {
    val (end, start) = findCaretPositions(previousWord)
    deleteText(start, end)
  }

  def deleteNextChar() = {
    val (start, end) = findCaretPositions(forward)
    deleteText(start, end)
  }

  def deletePreviousChar() = {
    val (end, start) = findCaretPositions(backward)
    deleteText(start, end)
  }

  def selectAll() = selectRange(0, text.length)

  def selectEnd() = selectPositionCaret(text.length)
  def selectHome() = selectPositionCaret(0)

  def selectNextWord() = {
    val (start, end) = findCaretPositions(nextWord)
    selectRange(start, end)
  }
  def selectPreviousWord() = {
    val (start, end) = findCaretPositions(previousWord)
    selectRange(start, end)
  }

  def unselect() = positionCaret(caretIndex)

  def selectForward() = {
    val (start, end) = findCaretPositions(forward)
    selectRange(start, end)
  }

  def selectBackward() = {
    val (start, end) = findCaretPositions(backward)
    selectRange(start, end)
  }
  
  def selectHomeExtend() =
    selectRange(selection.value.map(_.end).getOrElse(0), 0)
    
  def selectEndExtend() = 
    selectRange(selection.value.map(_.start).getOrElse(0), text.length)

  def undo()
  def redo()
}

trait TextKeyBindings extends KeyBindings { self: KeyEvents with TextKeyHandlers =>
  override val bindings = textKeyBindings

  protected val textKeyBindings =
    Map(
      RIGHT -> forward, KP_RIGHT -> forward,
      LEFT -> backward, KP_LEFT -> backward,
      BACK_SPACE -> deletePreviousChar,
      SHIFT + BACK_SPACE -> deletePreviousChar,
      DELETE -> deleteNextChar,
      CUT -> cut, SHIFT + DELETE -> cut,
      COPY -> copy,
      PASTE -> paste, SHIFT + INSERT -> paste,
      SHIFT + RIGHT -> selectForward, SHIFT + KP_RIGHT -> selectForward,
      SHIFT + LEFT -> selectBackward, SHIFT + KP_LEFT -> selectBackward) ++
      operatingSystemSpecificBindings

  private val operatingSystemSpecificBindings =
    os match {
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
    CTRL + SHIFT + Z -> redo)

  private val macBindings = Map(
    META + HOME -> home,
    META + END -> end,
    ALT + LEFT -> previousWord, ALT + KP_LEFT -> previousWord,
    ALT + RIGHT -> nextWord, ALT + KP_RIGHT -> nextWord,
    META + DELETE -> deleteNextWord,
    META + BACK_SPACE -> deletePreviousWord,
    META + X -> cut,
    META + C -> copy, META + INSERT -> copy,
    META + V -> paste,
    SHIFT + META + HOME -> selectHome,
    SHIFT + META + END -> selectEnd,
    META + A -> selectAll,
    SHIFT + ALT + LEFT -> selectPreviousWord, SHIFT + ALT + KP_LEFT -> selectPreviousWord,
    SHIFT + ALT + RIGHT -> selectNextWord, SHIFT + ALT + KP_RIGHT -> selectNextWord,
    META + Z -> undo,
    SHIFT + META + Z -> redo)

  private val nonMacBindings = Map(
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
    CTRL + BACK_SLASH -> unselect)

  private val nonLinuxBindings = nonMacBindings ++ Map(
    CTRL + Z -> undo,
    CTRL + Y -> redo)
}