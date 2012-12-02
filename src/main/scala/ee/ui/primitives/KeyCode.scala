package ee.ui.primitives

import KeyCodeGroup._

case class KeyCode(
  code: Int, name: String) {

  val group: Option[KeyCodeGroup] = None

  lazy val internalGroup = group getOrElse new KeyCodeGroup(0)

  lazy val isFunction = internalGroup contains FUNCTION
  lazy val isNavigation = internalGroup contains NAVIGATION
  lazy val isArrow = internalGroup contains ARROW
  lazy val isModifier = internalGroup contains MODIFIER
  lazy val isLetter = internalGroup contains LETTER
  lazy val isDigit = internalGroup contains DIGIT
  lazy val isKeypad = internalGroup contains KEYPAD
  lazy val isWhitespace = internalGroup contains WHITESPACE
  lazy val isMedia = internalGroup contains MEDIA

}

class KeyCodeGroup(val value: Int) {
  def |(other: KeyCodeGroup) = new KeyCodeGroup(value | other.value)
  def contains(other: KeyCodeGroup) = (value & other.value) != 0
}

object KeyCodeGroup {
  object FUNCTION extends KeyCodeGroup(1)
  object NAVIGATION extends KeyCodeGroup(1 << 1)
  object ARROW extends KeyCodeGroup(1 << 2)
  object MODIFIER extends KeyCodeGroup(1 << 3)
  object LETTER extends KeyCodeGroup(1 << 4)
  object DIGIT extends KeyCodeGroup(1 << 5)
  object KEYPAD extends KeyCodeGroup(1 << 6)
  object WHITESPACE extends KeyCodeGroup(1 << 7)
  object MEDIA extends KeyCodeGroup(1 << 8)
}

object KeyCode {
  def from(code: Int): KeyCode = keyCodes(code)
  private var keyCodes = Map[Int, KeyCode]()
  @inline private def createKeyCode(code: Int, name: String, groupOption: Option[KeyCodeGroup]) = {
    val keyCode = new KeyCode(code, name) {
      override val group = groupOption
    }
    keyCodes = keyCodes.updated(code, keyCode)
    keyCode
  }
  @inline private def key(code: Int, name: String, group: KeyCodeGroup) = createKeyCode(code, name, Some(group))
  @inline private def key(code: Int, name: String) = createKeyCode(code, name, None)
  val ENTER = key(0x0A, "Enter", WHITESPACE)

  val BACK_SPACE = key(0x08, "Backspace")
  val TAB = key(0x09, "Tab", WHITESPACE)
  val CANCEL = key(0x03, "Cancel")
  val CLEAR = key(0x0C, "Clear")
  val SHIFT = key(0x10, "Shift", MODIFIER)
  val CONTROL = key(0x11, "Ctrl", MODIFIER)
  val COMMAND = key(0x300, "Command", MODIFIER)
  val ALT = key(0x12, "Alt", MODIFIER)
  val PAUSE = key(0x13, "Pause")
  val CAPS = key(0x14, "Caps Lock")
  val ESCAPE = key(0x1B, "Esc")
  val SPACE = key(0x20, "Space", WHITESPACE)
  val PAGE_UP = key(0x21, "Page Up", NAVIGATION)
  val PAGE_DOWN = key(0x22, "Page Down", NAVIGATION)
  val END = key(0x23, "End", NAVIGATION)
  val HOME = key(0x24, "Home", NAVIGATION)
  val LEFT = key(0x25, "Left", ARROW | NAVIGATION)
  val UP = key(0x26, "Up", ARROW | NAVIGATION)
  val RIGHT = key(0x27, "Right", ARROW | NAVIGATION)
  val DOWN = key(0x28, "Down", ARROW | NAVIGATION)
  val COMMA = key(0x2C, "Comma")
  val MINUS = key(0x2D, "Minus") // Constant for the minus key, "-"
  val PERIOD = key(0x2E, "Period")
  val SLASH = key(0x2F, "Slash")

  val DIGIT0 = key(0x30, "0", DIGIT)
  val DIGIT1 = key(0x31, "1", DIGIT)
  val DIGIT2 = key(0x32, "2", DIGIT)
  val DIGIT3 = key(0x33, "3", DIGIT)
  val DIGIT4 = key(0x34, "4", DIGIT)
  val DIGIT5 = key(0x35, "5", DIGIT)
  val DIGIT6 = key(0x36, "6", DIGIT)
  val DIGIT7 = key(0x37, "7", DIGIT)
  val DIGIT8 = key(0x38, "8", DIGIT)
  val DIGIT9 = key(0x39, "9", DIGIT)

  val SEMICOLON = key(0x3B, "Semicolon")
  val EQUALS = key(0x3D, "Equals")

  val A = key(0x41, "A", LETTER)
  val B = key(0x42, "B", LETTER)
  val C = key(0x43, "C", LETTER)
  val D = key(0x44, "D", LETTER)
  val E = key(0x45, "E", LETTER)
  val F = key(0x46, "F", LETTER)
  val G = key(0x47, "G", LETTER)
  val H = key(0x48, "H", LETTER)
  val I = key(0x49, "I", LETTER)
  val J = key(0x4A, "J", LETTER)
  val K = key(0x4B, "K", LETTER)
  val L = key(0x4C, "L", LETTER)
  val M = key(0x4D, "M", LETTER)
  val N = key(0x4E, "N", LETTER)
  val O = key(0x4F, "O", LETTER)
  val P = key(0x50, "P", LETTER)
  val Q = key(0x51, "Q", LETTER)
  val R = key(0x52, "R", LETTER)
  val S = key(0x53, "S", LETTER)
  val T = key(0x54, "T", LETTER)
  val U = key(0x55, "U", LETTER)
  val V = key(0x56, "V", LETTER)
  val W = key(0x57, "W", LETTER)
  val X = key(0x58, "X", LETTER)
  val Y = key(0x59, "Y", LETTER)
  val Z = key(0x5A, "Z", LETTER)

  val OPEN_BRACKET = key(0x5B, "Open Bracket") // Constant for the open bracket key, "["
  val BACK_SLASH = key(0x5C, "Back Slash")
  val CLOSE_BRACKET = key(0x5D, "Close Bracket") // Constant for the close bracket key, "]"

  val NUMPAD0 = key(0x60, "Numpad 0", DIGIT | KEYPAD)
  val NUMPAD1 = key(0x61, "Numpad 1", DIGIT | KEYPAD)
  val NUMPAD2 = key(0x62, "Numpad 2", DIGIT | KEYPAD)
  val NUMPAD3 = key(0x63, "Numpad 3", DIGIT | KEYPAD)
  val NUMPAD4 = key(0x64, "Numpad 4", DIGIT | KEYPAD)
  val NUMPAD5 = key(0x65, "Numpad 5", DIGIT | KEYPAD)
  val NUMPAD6 = key(0x66, "Numpad 6", DIGIT | KEYPAD)
  val NUMPAD7 = key(0x67, "Numpad 7", DIGIT | KEYPAD)
  val NUMPAD8 = key(0x68, "Numpad 8", DIGIT | KEYPAD)
  val NUMPAD9 = key(0x69, "Numpad 9", DIGIT | KEYPAD)

  val MULTIPLY = key(0x6A, "Multiply")
  val ADD = key(0x6B, "Add")
  val SEPARATOR = key(0x6C, "Separator") // Constant for the Numpad Separator key.
  val SUBTRACT = key(0x6D, "Subtract")
  val DECIMAL = key(0x6E, "Decimal")
  val DIVIDE = key(0x6F, "Divide")

  val DELETE = key(0x7F, "Delete") /* ASCII:Integer   DEL */

  val NUM_LOCK = key(0x90, "Num Lock")
  val SCROLL_LOCK = key(0x91, "Scroll Lock")

  val F1 = key(0x70, "F1", FUNCTION)
  val F2 = key(0x71, "F2", FUNCTION)
  val F3 = key(0x72, "F3", FUNCTION)
  val F4 = key(0x73, "F4", FUNCTION)
  val F5 = key(0x74, "F5", FUNCTION)
  val F6 = key(0x75, "F6", FUNCTION)
  val F7 = key(0x76, "F7", FUNCTION)
  val F8 = key(0x77, "F8", FUNCTION)
  val F9 = key(0x78, "F9", FUNCTION)
  val F10 = key(0x79, "F10", FUNCTION)
  val F11 = key(0x7A, "F11", FUNCTION)
  val F12 = key(0x7B, "F12", FUNCTION)
  val F13 = key(0xF000, "F13", FUNCTION)
  val F14 = key(0xF001, "F14", FUNCTION)
  val F15 = key(0xF002, "F15", FUNCTION)
  val F16 = key(0xF003, "F16", FUNCTION)
  val F17 = key(0xF004, "F17", FUNCTION)
  val F18 = key(0xF005, "F18", FUNCTION)
  val F19 = key(0xF006, "F19", FUNCTION)
  val F20 = key(0xF007, "F20", FUNCTION)
  val F21 = key(0xF008, "F21", FUNCTION)
  val F22 = key(0xF009, "F22", FUNCTION)
  val F23 = key(0xF00A, "F23", FUNCTION)
  val F24 = key(0xF00B, "F24", FUNCTION)

  val PRINTSCREEN = key(0x9A, "Print Screen")
  val INSERT = key(0x9B, "Insert")
  val HELP = key(0x9C, "Help")
  val META = key(0x9D, "Meta", MODIFIER)
  val BACK_QUOTE = key(0xC0, "Back Quote")
  val QUOTE = key(0xDE, "Quote")
  val KP_UP = key(0xE0, "Numpad Up", ARROW | NAVIGATION | KEYPAD)
  val KP_DOWN = key(0xE1, "Numpad Down", ARROW | NAVIGATION | KEYPAD)
  val KP_LEFT = key(0xE2, "Numpad Left", ARROW | NAVIGATION | KEYPAD)
  val KP_RIGHT = key(0xE3, "Numpad Right", ARROW | NAVIGATION | KEYPAD)

  val DEAD_GRAVE = key(0x80, "Dead Grave")
  val DEAD_ACUTE = key(0x81, "Dead Acute")
  val DEAD_CIRCUMFLEX = key(0x82, "Circumflex")
  val DEAD_TILDE = key(0x83, "Dead Tilde")
  val DEAD_MACRON = key(0x84, "Dead Macron")
  val DEAD_BREVE = key(0x85, "Dead Breve")
  val DEAD_ABOVEDOT = key(0x86, "Dead Abovedot")
  val DEAD_DIAERESIS = key(0x87, "Dead Diaeresis")
  val DEAD_ABOVERING = key(0x88, "Dead Abovering")
  val DEAD_DOUBLEACUTE = key(0x89, "Dead Doubleacute")
  val DEAD_CARON = key(0x8a, "Dead Caron")
  val DEAD_CEDILLA = key(0x8b, "Dead Cedilla")
  val DEAD_OGONEK = key(0x8c, "Dead Ogonek")
  val DEAD_IOTA = key(0x8d, "Dead Iota")
  val DEAD_VOICED_SOUND = key(0x8e, "Dead Voiced Sound")
  val DEAD_SEMIVOICED_SOUND = key(0x8f, "Dead Semivoiced Sound")

  val AMPERSAND = key(0x96, "Ampersand")
  val ASTERISK = key(0x97, "Asterisk")
  val QUOTEDBL = key(0x98, "Double Quote")
  val LESS = key(0x99, "Less")
  val GREATER = key(0xa0, "Greater")
  val BRACELEFT = key(0xa1, "Left Brace")
  val BRACERIGHT = key(0xa2, "Right Brace")
  val AT = key(0x0200, "At") // Constant for the "@" key.
  val COLON = key(0x0201, "Colon") // Constant for the ":" key.
  val CIRCUMFLEX = key(0x0202, "Circumflex") // Constant for the "^" key.
  val DOLLAR = key(0x0203, "Dollar") // Constant for the "$" key.
  val EURO_SIGN = key(0x0204, "Euro Sign") // Constant for the Euro currency sign key.
  val EXCLAMATION_MARK = key(0x0205, "Exclamation Mark") // Constant for the "!" key.
  val INVERTED_EXCLAMATION_MARK = key(0x0206, "Inverted Exclamation Mark") // Constant for the inverted exclamation mark key.
  val LEFT_PARENTHESIS = key(0x0207, "Left Parenthesis") //Constant for the " = key(" key.
  val NUMBER_SIGN = key(0x0208, "Number Sign") // Constant for the "#" key.
  val PLUS = key(0x0209, "Plus") // Constant for the "+" key.
  val RIGHT_PARENTHESIS = key(0x020A, "Right Parenthesis") // Constant for the ")" key.
  val UNDERSCORE = key(0x020B, "Underscore") // Constant for the "_" key.
  val WINDOWS = key(0x020C, "Windows", MODIFIER) //Constant for the Microsoft Windows "Windows" key.* It is used for both the left and right version of the key.
  val CONTEXT_MENU = key(0x020D, "Context Menu") // Constant for the Microsoft Windows Context Menu key.

  val FINAL = key(0x0018, "Final") // Constant for input method support on Asian Keyboards.
  val CONVERT = key(0x001C, "Convert")
  val NONCONVERT = key(0x001D, "Nonconvert")
  val ACCEPT = key(0x001E, "Accept")
  val MODECHANGE = key(0x001F, "Mode Change")
  val KANA = key(0x0015, "Kana")
  val KANJI = key(0x0019, "Kanji")
  val ALPHANUMERIC = key(0x00F0, "Alphanumeric")
  val KATAKANA = key(0x00F1, "Katakana")
  val HIRAGANA = key(0x00F2, "Hiragana")
  val FULL_WIDTH = key(0x00F3, "Full Width")
  val HALF_WIDTH = key(0x00F4, "Half Width")
  val ROMAN_CHARACTERS = key(0x00F5, "Roman Characters")
  val ALL_CANDIDATES = key(0x0100, "All Candidates")
  val PREVIOUS_CANDIDATE = key(0x0101, "Previous Candidate")
  val CODE_INPUT = key(0x0102, "Code Input")

  val JAPANESE_KATAKANA = key(0x0103, "Japanese Katakana") // Constant for the Japanese-Katakana function key.* This key switches to a Japanese input method and selects its Katakana input mode.
  val JAPANESE_HIRAGANA = key(0x0104, "Japanese Hiragana") // Constant for the Japanese-Hiragana function key.* This key switches to a Japanese input method and selects its Hiragana input mode.
  val JAPANESE_ROMAN = key(0x0105, "Japanese Roman") // Constant for the Japanese-Roman function key.* This key switches to a Japanese input method and selects its Roman-Direct input mode.
  val KANA_LOCK = key(0x0106, "Kana Lock") // Constant for the locking Kana function key.* This key locks the keyboard into a Kana layout.

  val INPUT_METHOD_ON_OFF = key(0x0107, "Input Method On/Off")

  val CUT = key(0xFFD1, "Cut")
  val COPY = key(0xFFCD, "Copy")
  val PASTE = key(0xFFCF, "Paste")
  val UNDO = key(0xFFCB, "Undo")
  val AGAIN = key(0xFFC9, "Again")
  val FIND = key(0xFFD0, "Find")
  val PROPS = key(0xFFCA, "Properties")
  val STOP = key(0xFFC8, "Stop")
  val COMPOSE = key(0xFF20, "Compose")
  val ALT_GRAPH = key(0xFF7E, "Alt Graph", MODIFIER)
  val BEGIN = key(0xFF58, "Begin") // Constant for the Begin key.

  /* Mobile and Embedded Specific Key Codes */
  val SOFTKEY_0 = key(0x1000, "Softkey 0")
  val SOFTKEY_1 = key(0x1001, "Softkey 1")
  val SOFTKEY_2 = key(0x1002, "Softkey 2")
  val SOFTKEY_3 = key(0x1003, "Softkey 3")
  val SOFTKEY_4 = key(0x1004, "Softkey 4")
  val SOFTKEY_5 = key(0x1005, "Softkey 5")
  val SOFTKEY_6 = key(0x1006, "Softkey 6")
  val SOFTKEY_7 = key(0x1007, "Softkey 7")
  val SOFTKEY_8 = key(0x1008, "Softkey 8")
  val SOFTKEY_9 = key(0x1009, "Softkey 9")
  val GAME_A = key(0x100A, "Game A")
  val GAME_B = key(0x100B, "Game B")
  val GAME_C = key(0x100C, "Game C")
  val GAME_D = key(0x100D, "Game D")
  val STAR = key(0x100E, "Star")
  val POUND = key(0x100F, "Pound")

  /* Set of TV Specific Key Codes */
  val POWER = key(0x199, "Power")
  val INFO = key(0x1C9, "Info")
  val COLORED_KEY_0 = key(0x193, "Colored Key 0")
  val COLORED_KEY_1 = key(0x194, "Colored Key 1")
  val COLORED_KEY_2 = key(0x195, "Colored Key 2")
  val COLORED_KEY_3 = key(0x196, "Colored Key 3")
  val EJECT_TOGGLE = key(0x19E, "Eject", MEDIA)
  val PLAY = key(0x19F, "Play", MEDIA)
  val RECORD = key(0x1A0, "Record", MEDIA)
  val FAST_FWD = key(0x1A1, "Fast Forward", MEDIA)
  val REWIND = key(0x19C, "Rewind", MEDIA)
  val TRACK_PREV = key(0x1A8, "Previous Track", MEDIA)
  val TRACK_NEXT = key(0x1A9, "Next Track", MEDIA)
  val CHANNEL_UP = key(0x1AB, "Channel Up", MEDIA)
  val CHANNEL_DOWN = key(0x1AC, "Channel Down", MEDIA)
  val VOLUME_UP = key(0x1bf, "Volume Up", MEDIA)
  val VOLUME_DOWN = key(0x1C0, "Volume Down", MEDIA)
  val MUTE = key(0x1C1, "Mute", MEDIA)

}

