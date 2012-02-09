package ee.uiFramework.layouts

sealed abstract class VerticalAlign {}

case object Top extends VerticalAlign {}
case object Bottom extends VerticalAlign {}
case object Middle extends VerticalAlign {}