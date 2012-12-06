package ee.ui.traits

import ee.ui.application.OperatingSystem
import ee.ui.primitives.KeyCode._
import ee.ui.application.ImplicitTextHelper
import ee.ui.application.TextHelper
import ee.ui.nativeElements.Text
import ee.ui.primitives.Point

trait MultilineTextKeyHandling extends MultilineTextKeyHandlers with MultilineTextKeyBindings { self: KeyEvents with TextInputLike with CalculatedBounds =>

}

trait MultilineTextKeyHandlers extends TextKeyHandlers with ImplicitTextHelper { self: TextInputLike with CalculatedBounds =>

  implicit val underlyingText: Text

  def getCaretIndex(position: Point)(implicit textHelper: TextHelper, text: Text): Int =
    textHelper getCaretIndex (text, position)

  def getCaretPosition(caretIndex: Int)(implicit textHelper: TextHelper, text: Text): Point =
    textHelper getCaretPosition (text, caretIndex)

  def currentCaretPosition = getCaretPosition(caretIndex)

  def fontMetrics(implicit textHelper: TextHelper, text: Text) =
    textHelper getFontMetrics text.font

  lazy val lineHeight = fontMetrics.lineHeight

  //TODO maybe we need to use different values for x, I can't think straight at the moment
  def lineStartIndex = getCaretIndex(currentCaretPosition copy (x = 0))
  def lineEndIndex = getCaretIndex(currentCaretPosition copy (x = width))

  def previousLineIndex = {
    val newY = currentCaretPosition.y - lineHeight

    if (newY < 0) 0
    else getCaretIndex(currentCaretPosition copy (y = newY))
  }

  def nextLineIndex = {
    val newY = currentCaretPosition.y + lineHeight

    if (newY > height) text.length
    else getCaretIndex(currentCaretPosition copy (y = newY))
  }

  def paragraphStartIndex: Int = {
    if (caretIndex > 0) {
      val str: String = text

      (1 to caretIndex - 1).reverse find (i => str.codePointAt(i - 1) == 0x0A) getOrElse 0
    } else caretIndex
  }

  def paragraphEndIndex: Int = {
    val str: String = text
    val end = str.length
    val index: Int = caretIndex
    
    if (index < end)
      (index to end) find (i => str.codePointAt(i) == 0x0A) getOrElse end
    else index
  }

  def lineStart() = positionCaret(lineStartIndex)
  def lineEnd() = positionCaret(lineEndIndex)
  def previousLine() = positionCaret(previousLineIndex)
  def nextLine() = positionCaret(nextLineIndex)
  def paragraphStart() = positionCaret(paragraphStartIndex)
  def paragraphEnd() = positionCaret(paragraphEndIndex)
  
  //TODO implement these and the select variants once you have some form of scrolling implemented for text. Don't forget to take a look at computeContentBounds or something similar in the javafx implementation
  def previousPage() = {}
  def nextPage() = {}

  def insertNewLine() = {
    val TextSelection(start, end) = selection.value
    replaceText(start, end, "\n")
  }

  def insertTab() = {
    val TextSelection(start, end) = selection.value
    replaceText(start, end, "\t")
  }

  def selectLineStart() = selectPositionCaret(lineStartIndex)
  def selectLineEnd() = selectPositionCaret(lineEndIndex)
  def selectPreviousLine() = selectPositionCaret(previousLineIndex)
  def selectNextLine() = selectPositionCaret(nextLineIndex)
  def selectParagraphStart() = selectPositionCaret(paragraphStartIndex)
  def selectParagraphEnd() = selectPositionCaret(paragraphEndIndex)

  def selectLineStartExtend() =
    selectRange(anchorIndex.value max caretIndex, lineStartIndex)
    
  def selectLineEndExtend() = 
    selectRange(anchorIndex.value min caretIndex, lineEndIndex)
    
  def selectPreviousPage() = {}
  def selectNextPage() = {}
}

trait MultilineTextKeyBindings extends TextKeyBindings { self: KeyEvents with MultilineTextKeyHandlers =>
  override val bindings = textKeyBindings ++ Map(
    HOME -> lineStart,
    END -> lineEnd,
    UP -> previousLine, KP_UP -> previousLine,
    DOWN -> nextLine, KP_DOWN -> nextLine,
    PAGE_UP -> previousPage,
    PAGE_DOWN -> nextPage,
    ENTER -> insertNewLine,
    TAB -> insertTab,
    SHIFT + HOME -> selectLineStart,
    SHIFT + END -> selectLineEnd,
    SHIFT + PAGE_UP -> selectPreviousPage,
    SHIFT + PAGE_DOWN -> selectNextPage,
    SHIFT + UP -> selectPreviousLine, SHIFT + KP_UP -> selectPreviousLine,
    SHIFT + DOWN -> selectNextLine, SHIFT + KP_DOWN -> selectNextLine)

  private val operatingSystemSpecificBindings =
    OperatingSystem.current match {
      case OperatingSystem.WINDOWS => windowsBindings
      case OperatingSystem.MAC => macBindings
      case OperatingSystem.LINUX => linuxBindings
      case OperatingSystem.SOLARIS => solarisBindings
      case OperatingSystem.UNKNOWN => unknownBindings
    }

  private val windowsBindings = nonMacBindings
  private val solarisBindings = nonMacBindings
  private val unknownBindings = nonMacBindings
  private val linuxBindings = nonMacBindings

  private val macBindings = Map(
    SHIFT + META + LEFT -> selectLineStartExtend,
    SHIFT + META + KP_LEFT -> selectLineStartExtend,
    SHIFT + META + RIGHT -> selectLineEndExtend,
    SHIFT + META + KP_RIGHT -> selectLineEndExtend,
    META + LEFT -> lineStart, META + KP_LEFT -> lineStart,
    META + RIGHT -> lineEnd, META + KP_RIGHT -> lineEnd,
    META + UP -> home, META + KP_UP -> home,
    META + DOWN -> end, META + DOWN -> end,
    SHIFT + META + UP -> selectHomeExtend, SHIFT + META + KP_UP -> selectHomeExtend,
    SHIFT + META + DOWN -> selectEndExtend, SHIFT + META + KP_DOWN -> selectEndExtend,
    ALT + UP -> paragraphStart, ALT + KP_UP -> paragraphStart,
    ALT + DOWN -> paragraphEnd, ALT + KP_DOWN -> paragraphEnd,
    SHIFT + ALT + UP -> selectParagraphStart, SHIFT + ALT + KP_UP -> selectParagraphStart,
    SHIFT + ALT + DOWN -> selectParagraphEnd, SHIFT + ALT + KP_DOWN -> selectParagraphEnd)

  private val nonMacBindings = Map(
    CTRL + UP -> paragraphStart, CTRL + KP_UP -> paragraphStart,
    CTRL + DOWN -> paragraphEnd, CTRL + KP_DOWN -> paragraphEnd,
    SHIFT + CTRL + UP -> selectParagraphStart, SHIFT + CTRL + KP_UP -> selectParagraphStart,
    SHIFT + CTRL + DOWN -> selectParagraphEnd, SHIFT + CTRL + KP_DOWN -> selectParagraphEnd)
}