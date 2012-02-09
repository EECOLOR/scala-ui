package ee.uiFramework.layouts

sealed abstract class HorizontalAlign {}

case object Left extends HorizontalAlign {}
case object Right extends HorizontalAlign {}
case object Center extends HorizontalAlign {}