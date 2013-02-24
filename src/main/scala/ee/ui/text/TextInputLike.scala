package ee.ui.text

import ee.ui.properties.Property
import ee.ui.properties.ReadOnlyProperty
import ee.ui.observables.ObservableValue

//TODO handle selection
trait TextInputLike extends TextInputLikeHelper with UndoHandling {

  private val _text = new Property("")
  def text = _text
  def text_=(value: String) = _text.value = value

  protected val _writableCaretIndex = new Property(0)
  def caretIndex: ReadOnlyProperty[Int] = _writableCaretIndex

  protected val _writableAnchorIndex = new Property(0)
  def anchorIndex: ReadOnlyProperty[Int] = _writableAnchorIndex

  protected val _writableSelection = new Property[Option[TextSelection]](None)
  def selection: ReadOnlyProperty[Option[TextSelection]] = _writableSelection

  private val _writableSelectedText = new Property("")
  def selectedText: ReadOnlyProperty[String] = _writableSelectedText

  _writableSelectedText <== selection.raw map {
    case Some(TextSelection(start, end)) => text substring (start, end)
    case None => ""
  }

}

trait UndoHandling extends TextInputLikeHelper { self: TextInputLike =>

  //TODO implement
  def undo() = {}
  def redo() = {}
  private def addChange(start: Int, oldText: String, newText: String): Unit = {}

  override protected def replaceText(start: Int, end: Int, replacement: String) = {
    val oldText = text.substring(start, end)
    
    super.replaceText(start, end, replacement)
    
    addChange(start, oldText, replacement)
  }
}

//TODO check these methods, some of them can probably be done with binding
trait TextInputLikeHelper { self: TextInputLike =>
  
  protected def inText = between(0, text.length) _
  protected def between(minValue: Int, maxValue: Int)(value: Int) = value max minValue min maxValue
  
  protected def deleteSelection() = replaceSelection("")
  
  protected def deleteText(start: Int, end: Int) = replaceText(start, end, "")

  protected def replaceSelection(replacement:String) = {
    val TextSelection(start, end) = selection getOrElse TextSelection(caretIndex, caretIndex)
    replaceText(start, end, replacement)
  }

  protected def replaceText(start: Int, end: Int, replacement: String) = {
    val str: String = text
    text = str patch (start, replacement, end - start)
  }

  protected def positionCaret(caretIndex: Int) = selectRange(caretIndex, caretIndex)

  protected def selectPositionCaret(caretIndex: Int) = selectRange(anchorIndex, caretIndex)

  protected def selectRange(anchorIndex: Int, caretIndex: Int) = {
    val safeAnchorIndex = inText(anchorIndex)
	val safeCaretIndex = inText(caretIndex)
    _writableAnchorIndex.value = safeAnchorIndex
    _writableCaretIndex.value = safeCaretIndex
    _writableSelection.value = Some(TextSelection(safeAnchorIndex, safeCaretIndex).normalize)
  }
}

case class TextSelection(start: Int, end: Int) {
  lazy val isEmpty = start == end
  def normalize = TextSelection(start min end, start max end)
}