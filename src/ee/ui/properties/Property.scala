package ee.ui.properties

class Property[T](default: T) extends Bindable[T] {
	
    private var _value = default
    def value = _value
    def value_=(value: T) = {
        if (_value != value && notify(_value, value)) {
            _value = value
        }
    }

}

