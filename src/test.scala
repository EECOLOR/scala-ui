object x {

    trait Styles[X] {
        var name: String = _

        def named(name: String): this.type = {
            this.name = name
            this
        }

    }
    class StyleProp[X](var value: X) {
        def apply(value: X) = Unit
        def unary_!(value: X) = Unit
    }

    object StyleProp {
        implicit def stylePropValue[X](s: StyleProp[X]): X = s.value
    }

    trait Stylable[X] {
        def styles[Y <: Styles[X]](s: Y) = Unit
    }

    trait Button

    object Button extends Stylable[Button]

    trait ButtonStyles extends Styles[Button] {
        val _border = new StyleProp[Int](0)
        def border = _border
        def border_=(border: Int) = _border.value = border
    }

    def y(c: => Unit) = Unit

    y {
        val z = new ButtonStyles {
            border = 12
        } named "test"

        new ButtonStyles {
            border = z.border
        }
    }
}
