package ee.ui.traits

import ee.ui.application.OperatingSystem
import ee.ui.primitives.KeyCode._

trait MultilineTextKeyHandling extends MultilineTextKeyHandlers with MultilineTextKeyBindings { self: KeyEvents with TextInputLike =>

}

trait MultilineTextKeyHandlers extends TextKeyHandlers { self: TextInputLike =>

  def lineStart() = {}
  def lineEnd() = {}
  def previousLine() = {}
  def nextLine() = {}
  def previousPage() = {}
  def nextPage() = {}
  def paragraphStart() =
    if (caretIndex > 0) {
      val str: String = text
      val paragraphStart =
        (1 to caretIndex - 1).reverse find (i => str.codePointAt(i - 1) == 0x0A) getOrElse 0
      positionCaret(paragraphStart)
    }

  def paragraphEnd() = {
    val str: String = text
    val end = str.length
    val index: Int = caretIndex
    if (index < end) {
      val paragraphEnd =
        (index to end) find (i => str.codePointAt(i) == 0x0A) getOrElse end
      positionCaret(paragraphEnd)
    }
  }

  def insertNewLine() = {
    val TextSelection(start, end) = selection.value
    replaceText(start, end, "\n")
  }

  def insertTab() = {
    val TextSelection(start, end) = selection.value
    replaceText(start, end, "\t")
  }

  def selectLineStart() = {}
  def selectLineEnd() = {}
  def selectPreviousPage() = {}
  def selectNextPage() = {}
  def selectNextLine() = {}
  def selectPreviousLine() = {}
  def selectLineStartExtend() = {}
  def selectLineEndExtend() = {}
  def selectParagraphStart() = {}
  def selectParagraphEnd() = {}
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