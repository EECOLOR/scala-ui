package ee.ui.properties

import ee.ui.observables.Observable

import language.implicitConversions

case class PropertyChangeHandler(properties: PropertyGroup#PropertiesChangedHandler*) {

  def apply: Unit = properties foreach (_.apply)
}

case class PropertyChangeCollector(properties: PropertyGroup#PropertiesChangedCollector*) {

  def applyIfChanged: Unit = properties foreach (_.applyIfChanged)
  def changed: Boolean = properties forall (_.changed)
  def changed_=(value: Boolean) = properties foreach (_.changed = value)
}

trait PropertyGroup {
  trait PropertiesChangedHandler {
    def apply(): Unit
    def propertyChanged(value:Any) = apply()
  }

  trait PropertiesChangedCollector { self: PropertiesChangedHandler =>

    var changed = false

    def applyIfChanged() =

      if (changed) {
        changed = false
        apply()
      }

    override def propertyChanged(value:Any) = {
      changed = true
    }

  }

  type Callback
  type PropertiesChangedHandlerType <: PropertiesChangedHandler

  def ~> = onChanged _
  def onChanged(callback: Callback): PropertiesChangedHandlerType

  def ~~> = collect _
  def collect(callback: Callback): PropertiesChangedHandlerType with PropertiesChangedCollector
}

//TODO clean up code
object PropertyGroupGeneration {

  def main(args: Array[String]) {

    import ee.util.Generate._

    val propertyGroupContents = (2 to 22) map { i =>
      val readOnlyPropertyTypes = generateTypeClasses("ReadOnlyProperty", i)
      val readOnlyProperties = generateProperties("p", "ReadOnlyProperty", i)
      val types = generateTypes(i)
      val propertyNames = generateStrings(i, "p")
      val tupleValues = generateStrings(i, "t._")
      s"  @inline implicit def tupleToPropertyGroup$i[$types](t:($readOnlyPropertyTypes)):PropertyGroup$i[$types] = new PropertyGroup$i($tupleValues)\n" +
        s"  def apply[$types]($readOnlyProperties):PropertyGroup$i[$types] = new PropertyGroup$i($propertyNames)"
    } mkString "\n"

    val propertyGroup = s"""
object PropertyGroup {
  @inline implicit def propertytoPropertyGroup1[T1](p:ReadOnlyProperty[T1]):PropertyGroup1[T1] = new PropertyGroup1(p)
  def apply[T1](p: ReadOnlyProperty[T1]):PropertyGroup1[T1] = new PropertyGroup1(p)
$propertyGroupContents
}
"""
    println(propertyGroup)
    val propertyGroups = (1 to 22).map { i =>
      val readOnlyPropertyTypes = generateTypeClasses("ReadOnlyProperty", i)
      val readOnlyProperties = generateProperties("p", "ReadOnlyProperty", i)
      val types = generateTypes(i)
      val propertyNames = generateStrings(i, "p")
      val onChangedNotify = generateStrings(i, "    p", " foreach propertyChanged()", "\n")

      s"""
class PropertyGroup$i[$types]($readOnlyProperties) extends PropertyGroup {
  type Callback = ($types) => Unit
  type PropertiesChangedHandlerType = PropertiesChangedHandler$i[$types]
  class PropertiesChangedHandler$i[$types](callback:Callback) extends PropertiesChangedHandler {
  	def apply() = callback($propertyNames)
  	
$onChangedNotify
  }

  def onChanged(callback:Callback) = new PropertiesChangedHandler$i(callback)
  def collect(callback:Callback) = new PropertiesChangedHandler$i[$types](callback) with PropertiesChangedCollector
}
"""
    }.mkString
    println(propertyGroups)
  }
}


object PropertyGroup {
  /*
  @inline implicit def propertytoPropertyGroup1[T1](p:ReadOnlyProperty[T1]):PropertyGroup1[T1] = new PropertyGroup1(p)
  def apply[T1](p: ReadOnlyProperty[T1]):PropertyGroup1[T1] = new PropertyGroup1(p)
  @inline implicit def tupleToPropertyGroup2[T1, T2](t:(ReadOnlyProperty[T1], ReadOnlyProperty[T2])):PropertyGroup2[T1, T2] = new PropertyGroup2(t._1, t._2)
  def apply[T1, T2](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2]):PropertyGroup2[T1, T2] = new PropertyGroup2(p1, p2)
  @inline implicit def tupleToPropertyGroup3[T1, T2, T3](t:(ReadOnlyProperty[T1], ReadOnlyProperty[T2], ReadOnlyProperty[T3])):PropertyGroup3[T1, T2, T3] = new PropertyGroup3(t._1, t._2, t._3)
  def apply[T1, T2, T3](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3]):PropertyGroup3[T1, T2, T3] = new PropertyGroup3(p1, p2, p3)
  @inline implicit def tupleToPropertyGroup4[T1, T2, T3, T4](t:(ReadOnlyProperty[T1], ReadOnlyProperty[T2], ReadOnlyProperty[T3], ReadOnlyProperty[T4])):PropertyGroup4[T1, T2, T3, T4] = new PropertyGroup4(t._1, t._2, t._3, t._4)
  def apply[T1, T2, T3, T4](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4]):PropertyGroup4[T1, T2, T3, T4] = new PropertyGroup4(p1, p2, p3, p4)
  @inline implicit def tupleToPropertyGroup5[T1, T2, T3, T4, T5](t:(ReadOnlyProperty[T1], ReadOnlyProperty[T2], ReadOnlyProperty[T3], ReadOnlyProperty[T4], ReadOnlyProperty[T5])):PropertyGroup5[T1, T2, T3, T4, T5] = new PropertyGroup5(t._1, t._2, t._3, t._4, t._5)
  def apply[T1, T2, T3, T4, T5](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5]):PropertyGroup5[T1, T2, T3, T4, T5] = new PropertyGroup5(p1, p2, p3, p4, p5)
  @inline implicit def tupleToPropertyGroup6[T1, T2, T3, T4, T5, T6](t:(ReadOnlyProperty[T1], ReadOnlyProperty[T2], ReadOnlyProperty[T3], ReadOnlyProperty[T4], ReadOnlyProperty[T5], ReadOnlyProperty[T6])):PropertyGroup6[T1, T2, T3, T4, T5, T6] = new PropertyGroup6(t._1, t._2, t._3, t._4, t._5, t._6)
  def apply[T1, T2, T3, T4, T5, T6](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6]):PropertyGroup6[T1, T2, T3, T4, T5, T6] = new PropertyGroup6(p1, p2, p3, p4, p5, p6)
  @inline implicit def tupleToPropertyGroup7[T1, T2, T3, T4, T5, T6, T7](t:(ReadOnlyProperty[T1], ReadOnlyProperty[T2], ReadOnlyProperty[T3], ReadOnlyProperty[T4], ReadOnlyProperty[T5], ReadOnlyProperty[T6], ReadOnlyProperty[T7])):PropertyGroup7[T1, T2, T3, T4, T5, T6, T7] = new PropertyGroup7(t._1, t._2, t._3, t._4, t._5, t._6, t._7)
  def apply[T1, T2, T3, T4, T5, T6, T7](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7]):PropertyGroup7[T1, T2, T3, T4, T5, T6, T7] = new PropertyGroup7(p1, p2, p3, p4, p5, p6, p7)
  @inline implicit def tupleToPropertyGroup8[T1, T2, T3, T4, T5, T6, T7, T8](t:(ReadOnlyProperty[T1], ReadOnlyProperty[T2], ReadOnlyProperty[T3], ReadOnlyProperty[T4], ReadOnlyProperty[T5], ReadOnlyProperty[T6], ReadOnlyProperty[T7], ReadOnlyProperty[T8])):PropertyGroup8[T1, T2, T3, T4, T5, T6, T7, T8] = new PropertyGroup8(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8)
  def apply[T1, T2, T3, T4, T5, T6, T7, T8](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7], p8: ReadOnlyProperty[T8]):PropertyGroup8[T1, T2, T3, T4, T5, T6, T7, T8] = new PropertyGroup8(p1, p2, p3, p4, p5, p6, p7, p8)
  @inline implicit def tupleToPropertyGroup9[T1, T2, T3, T4, T5, T6, T7, T8, T9](t:(ReadOnlyProperty[T1], ReadOnlyProperty[T2], ReadOnlyProperty[T3], ReadOnlyProperty[T4], ReadOnlyProperty[T5], ReadOnlyProperty[T6], ReadOnlyProperty[T7], ReadOnlyProperty[T8], ReadOnlyProperty[T9])):PropertyGroup9[T1, T2, T3, T4, T5, T6, T7, T8, T9] = new PropertyGroup9(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9)
  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7], p8: ReadOnlyProperty[T8], p9: ReadOnlyProperty[T9]):PropertyGroup9[T1, T2, T3, T4, T5, T6, T7, T8, T9] = new PropertyGroup9(p1, p2, p3, p4, p5, p6, p7, p8, p9)
  @inline implicit def tupleToPropertyGroup10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](t:(ReadOnlyProperty[T1], ReadOnlyProperty[T2], ReadOnlyProperty[T3], ReadOnlyProperty[T4], ReadOnlyProperty[T5], ReadOnlyProperty[T6], ReadOnlyProperty[T7], ReadOnlyProperty[T8], ReadOnlyProperty[T9], ReadOnlyProperty[T10])):PropertyGroup10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = new PropertyGroup10(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10)
  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7], p8: ReadOnlyProperty[T8], p9: ReadOnlyProperty[T9], p10: ReadOnlyProperty[T10]):PropertyGroup10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = new PropertyGroup10(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10)
  @inline implicit def tupleToPropertyGroup11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](t:(ReadOnlyProperty[T1], ReadOnlyProperty[T2], ReadOnlyProperty[T3], ReadOnlyProperty[T4], ReadOnlyProperty[T5], ReadOnlyProperty[T6], ReadOnlyProperty[T7], ReadOnlyProperty[T8], ReadOnlyProperty[T9], ReadOnlyProperty[T10], ReadOnlyProperty[T11])):PropertyGroup11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = new PropertyGroup11(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11)
  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7], p8: ReadOnlyProperty[T8], p9: ReadOnlyProperty[T9], p10: ReadOnlyProperty[T10], p11: ReadOnlyProperty[T11]):PropertyGroup11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = new PropertyGroup11(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11)
  @inline implicit def tupleToPropertyGroup12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](t:(ReadOnlyProperty[T1], ReadOnlyProperty[T2], ReadOnlyProperty[T3], ReadOnlyProperty[T4], ReadOnlyProperty[T5], ReadOnlyProperty[T6], ReadOnlyProperty[T7], ReadOnlyProperty[T8], ReadOnlyProperty[T9], ReadOnlyProperty[T10], ReadOnlyProperty[T11], ReadOnlyProperty[T12])):PropertyGroup12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = new PropertyGroup12(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12)
  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7], p8: ReadOnlyProperty[T8], p9: ReadOnlyProperty[T9], p10: ReadOnlyProperty[T10], p11: ReadOnlyProperty[T11], p12: ReadOnlyProperty[T12]):PropertyGroup12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = new PropertyGroup12(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12)
  @inline implicit def tupleToPropertyGroup13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](t:(ReadOnlyProperty[T1], ReadOnlyProperty[T2], ReadOnlyProperty[T3], ReadOnlyProperty[T4], ReadOnlyProperty[T5], ReadOnlyProperty[T6], ReadOnlyProperty[T7], ReadOnlyProperty[T8], ReadOnlyProperty[T9], ReadOnlyProperty[T10], ReadOnlyProperty[T11], ReadOnlyProperty[T12], ReadOnlyProperty[T13])):PropertyGroup13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = new PropertyGroup13(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13)
  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7], p8: ReadOnlyProperty[T8], p9: ReadOnlyProperty[T9], p10: ReadOnlyProperty[T10], p11: ReadOnlyProperty[T11], p12: ReadOnlyProperty[T12], p13: ReadOnlyProperty[T13]):PropertyGroup13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = new PropertyGroup13(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13)
  @inline implicit def tupleToPropertyGroup14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](t:(ReadOnlyProperty[T1], ReadOnlyProperty[T2], ReadOnlyProperty[T3], ReadOnlyProperty[T4], ReadOnlyProperty[T5], ReadOnlyProperty[T6], ReadOnlyProperty[T7], ReadOnlyProperty[T8], ReadOnlyProperty[T9], ReadOnlyProperty[T10], ReadOnlyProperty[T11], ReadOnlyProperty[T12], ReadOnlyProperty[T13], ReadOnlyProperty[T14])):PropertyGroup14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = new PropertyGroup14(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14)
  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7], p8: ReadOnlyProperty[T8], p9: ReadOnlyProperty[T9], p10: ReadOnlyProperty[T10], p11: ReadOnlyProperty[T11], p12: ReadOnlyProperty[T12], p13: ReadOnlyProperty[T13], p14: ReadOnlyProperty[T14]):PropertyGroup14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = new PropertyGroup14(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14)
  @inline implicit def tupleToPropertyGroup15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](t:(ReadOnlyProperty[T1], ReadOnlyProperty[T2], ReadOnlyProperty[T3], ReadOnlyProperty[T4], ReadOnlyProperty[T5], ReadOnlyProperty[T6], ReadOnlyProperty[T7], ReadOnlyProperty[T8], ReadOnlyProperty[T9], ReadOnlyProperty[T10], ReadOnlyProperty[T11], ReadOnlyProperty[T12], ReadOnlyProperty[T13], ReadOnlyProperty[T14], ReadOnlyProperty[T15])):PropertyGroup15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = new PropertyGroup15(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15)
  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7], p8: ReadOnlyProperty[T8], p9: ReadOnlyProperty[T9], p10: ReadOnlyProperty[T10], p11: ReadOnlyProperty[T11], p12: ReadOnlyProperty[T12], p13: ReadOnlyProperty[T13], p14: ReadOnlyProperty[T14], p15: ReadOnlyProperty[T15]):PropertyGroup15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = new PropertyGroup15(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15)
  @inline implicit def tupleToPropertyGroup16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](t:(ReadOnlyProperty[T1], ReadOnlyProperty[T2], ReadOnlyProperty[T3], ReadOnlyProperty[T4], ReadOnlyProperty[T5], ReadOnlyProperty[T6], ReadOnlyProperty[T7], ReadOnlyProperty[T8], ReadOnlyProperty[T9], ReadOnlyProperty[T10], ReadOnlyProperty[T11], ReadOnlyProperty[T12], ReadOnlyProperty[T13], ReadOnlyProperty[T14], ReadOnlyProperty[T15], ReadOnlyProperty[T16])):PropertyGroup16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = new PropertyGroup16(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16)
  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7], p8: ReadOnlyProperty[T8], p9: ReadOnlyProperty[T9], p10: ReadOnlyProperty[T10], p11: ReadOnlyProperty[T11], p12: ReadOnlyProperty[T12], p13: ReadOnlyProperty[T13], p14: ReadOnlyProperty[T14], p15: ReadOnlyProperty[T15], p16: ReadOnlyProperty[T16]):PropertyGroup16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = new PropertyGroup16(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16)
  @inline implicit def tupleToPropertyGroup17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](t:(ReadOnlyProperty[T1], ReadOnlyProperty[T2], ReadOnlyProperty[T3], ReadOnlyProperty[T4], ReadOnlyProperty[T5], ReadOnlyProperty[T6], ReadOnlyProperty[T7], ReadOnlyProperty[T8], ReadOnlyProperty[T9], ReadOnlyProperty[T10], ReadOnlyProperty[T11], ReadOnlyProperty[T12], ReadOnlyProperty[T13], ReadOnlyProperty[T14], ReadOnlyProperty[T15], ReadOnlyProperty[T16], ReadOnlyProperty[T17])):PropertyGroup17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = new PropertyGroup17(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17)
  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7], p8: ReadOnlyProperty[T8], p9: ReadOnlyProperty[T9], p10: ReadOnlyProperty[T10], p11: ReadOnlyProperty[T11], p12: ReadOnlyProperty[T12], p13: ReadOnlyProperty[T13], p14: ReadOnlyProperty[T14], p15: ReadOnlyProperty[T15], p16: ReadOnlyProperty[T16], p17: ReadOnlyProperty[T17]):PropertyGroup17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = new PropertyGroup17(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17)
  @inline implicit def tupleToPropertyGroup18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](t:(ReadOnlyProperty[T1], ReadOnlyProperty[T2], ReadOnlyProperty[T3], ReadOnlyProperty[T4], ReadOnlyProperty[T5], ReadOnlyProperty[T6], ReadOnlyProperty[T7], ReadOnlyProperty[T8], ReadOnlyProperty[T9], ReadOnlyProperty[T10], ReadOnlyProperty[T11], ReadOnlyProperty[T12], ReadOnlyProperty[T13], ReadOnlyProperty[T14], ReadOnlyProperty[T15], ReadOnlyProperty[T16], ReadOnlyProperty[T17], ReadOnlyProperty[T18])):PropertyGroup18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = new PropertyGroup18(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17, t._18)
  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7], p8: ReadOnlyProperty[T8], p9: ReadOnlyProperty[T9], p10: ReadOnlyProperty[T10], p11: ReadOnlyProperty[T11], p12: ReadOnlyProperty[T12], p13: ReadOnlyProperty[T13], p14: ReadOnlyProperty[T14], p15: ReadOnlyProperty[T15], p16: ReadOnlyProperty[T16], p17: ReadOnlyProperty[T17], p18: ReadOnlyProperty[T18]):PropertyGroup18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = new PropertyGroup18(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18)
  @inline implicit def tupleToPropertyGroup19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](t:(ReadOnlyProperty[T1], ReadOnlyProperty[T2], ReadOnlyProperty[T3], ReadOnlyProperty[T4], ReadOnlyProperty[T5], ReadOnlyProperty[T6], ReadOnlyProperty[T7], ReadOnlyProperty[T8], ReadOnlyProperty[T9], ReadOnlyProperty[T10], ReadOnlyProperty[T11], ReadOnlyProperty[T12], ReadOnlyProperty[T13], ReadOnlyProperty[T14], ReadOnlyProperty[T15], ReadOnlyProperty[T16], ReadOnlyProperty[T17], ReadOnlyProperty[T18], ReadOnlyProperty[T19])):PropertyGroup19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = new PropertyGroup19(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17, t._18, t._19)
  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7], p8: ReadOnlyProperty[T8], p9: ReadOnlyProperty[T9], p10: ReadOnlyProperty[T10], p11: ReadOnlyProperty[T11], p12: ReadOnlyProperty[T12], p13: ReadOnlyProperty[T13], p14: ReadOnlyProperty[T14], p15: ReadOnlyProperty[T15], p16: ReadOnlyProperty[T16], p17: ReadOnlyProperty[T17], p18: ReadOnlyProperty[T18], p19: ReadOnlyProperty[T19]):PropertyGroup19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = new PropertyGroup19(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19)
  @inline implicit def tupleToPropertyGroup20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](t:(ReadOnlyProperty[T1], ReadOnlyProperty[T2], ReadOnlyProperty[T3], ReadOnlyProperty[T4], ReadOnlyProperty[T5], ReadOnlyProperty[T6], ReadOnlyProperty[T7], ReadOnlyProperty[T8], ReadOnlyProperty[T9], ReadOnlyProperty[T10], ReadOnlyProperty[T11], ReadOnlyProperty[T12], ReadOnlyProperty[T13], ReadOnlyProperty[T14], ReadOnlyProperty[T15], ReadOnlyProperty[T16], ReadOnlyProperty[T17], ReadOnlyProperty[T18], ReadOnlyProperty[T19], ReadOnlyProperty[T20])):PropertyGroup20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] = new PropertyGroup20(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17, t._18, t._19, t._20)
  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7], p8: ReadOnlyProperty[T8], p9: ReadOnlyProperty[T9], p10: ReadOnlyProperty[T10], p11: ReadOnlyProperty[T11], p12: ReadOnlyProperty[T12], p13: ReadOnlyProperty[T13], p14: ReadOnlyProperty[T14], p15: ReadOnlyProperty[T15], p16: ReadOnlyProperty[T16], p17: ReadOnlyProperty[T17], p18: ReadOnlyProperty[T18], p19: ReadOnlyProperty[T19], p20: ReadOnlyProperty[T20]):PropertyGroup20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] = new PropertyGroup20(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20)
  @inline implicit def tupleToPropertyGroup21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](t:(ReadOnlyProperty[T1], ReadOnlyProperty[T2], ReadOnlyProperty[T3], ReadOnlyProperty[T4], ReadOnlyProperty[T5], ReadOnlyProperty[T6], ReadOnlyProperty[T7], ReadOnlyProperty[T8], ReadOnlyProperty[T9], ReadOnlyProperty[T10], ReadOnlyProperty[T11], ReadOnlyProperty[T12], ReadOnlyProperty[T13], ReadOnlyProperty[T14], ReadOnlyProperty[T15], ReadOnlyProperty[T16], ReadOnlyProperty[T17], ReadOnlyProperty[T18], ReadOnlyProperty[T19], ReadOnlyProperty[T20], ReadOnlyProperty[T21])):PropertyGroup21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21] = new PropertyGroup21(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17, t._18, t._19, t._20, t._21)
  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7], p8: ReadOnlyProperty[T8], p9: ReadOnlyProperty[T9], p10: ReadOnlyProperty[T10], p11: ReadOnlyProperty[T11], p12: ReadOnlyProperty[T12], p13: ReadOnlyProperty[T13], p14: ReadOnlyProperty[T14], p15: ReadOnlyProperty[T15], p16: ReadOnlyProperty[T16], p17: ReadOnlyProperty[T17], p18: ReadOnlyProperty[T18], p19: ReadOnlyProperty[T19], p20: ReadOnlyProperty[T20], p21: ReadOnlyProperty[T21]):PropertyGroup21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21] = new PropertyGroup21(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20, p21)
  @inline implicit def tupleToPropertyGroup22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](t:(ReadOnlyProperty[T1], ReadOnlyProperty[T2], ReadOnlyProperty[T3], ReadOnlyProperty[T4], ReadOnlyProperty[T5], ReadOnlyProperty[T6], ReadOnlyProperty[T7], ReadOnlyProperty[T8], ReadOnlyProperty[T9], ReadOnlyProperty[T10], ReadOnlyProperty[T11], ReadOnlyProperty[T12], ReadOnlyProperty[T13], ReadOnlyProperty[T14], ReadOnlyProperty[T15], ReadOnlyProperty[T16], ReadOnlyProperty[T17], ReadOnlyProperty[T18], ReadOnlyProperty[T19], ReadOnlyProperty[T20], ReadOnlyProperty[T21], ReadOnlyProperty[T22])):PropertyGroup22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22] = new PropertyGroup22(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17, t._18, t._19, t._20, t._21, t._22)
  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7], p8: ReadOnlyProperty[T8], p9: ReadOnlyProperty[T9], p10: ReadOnlyProperty[T10], p11: ReadOnlyProperty[T11], p12: ReadOnlyProperty[T12], p13: ReadOnlyProperty[T13], p14: ReadOnlyProperty[T14], p15: ReadOnlyProperty[T15], p16: ReadOnlyProperty[T16], p17: ReadOnlyProperty[T17], p18: ReadOnlyProperty[T18], p19: ReadOnlyProperty[T19], p20: ReadOnlyProperty[T20], p21: ReadOnlyProperty[T21], p22: ReadOnlyProperty[T22]):PropertyGroup22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22] = new PropertyGroup22(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20, p21, p22)
}


class PropertyGroup1[T1](p1: ReadOnlyProperty[T1]) extends PropertyGroup {
  type Callback = (T1) => Unit
  type PropertiesChangedHandlerType = PropertiesChangedHandler1[T1]
  class PropertiesChangedHandler1[T1](callback:Callback) extends PropertiesChangedHandler {
  	def apply() = callback(p1)
  	
    p1.change observe propertyChanged _
  }

  def onChanged(callback:Callback) = new PropertiesChangedHandler1(callback)
  def collect(callback:Callback) = new PropertiesChangedHandler1[T1](callback) with PropertiesChangedCollector
}

class PropertyGroup2[T1, T2](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2]) extends PropertyGroup {
  type Callback = (T1, T2) => Unit
  type PropertiesChangedHandlerType = PropertiesChangedHandler2[T1, T2]
  class PropertiesChangedHandler2[T1, T2](callback:Callback) extends PropertiesChangedHandler {
  	def apply() = callback(p1, p2)
  	
    p1.change observe propertyChanged _
    p2.change observe propertyChanged _
  }

  def onChanged(callback:Callback) = new PropertiesChangedHandler2(callback)
  def collect(callback:Callback) = new PropertiesChangedHandler2[T1, T2](callback) with PropertiesChangedCollector
}

class PropertyGroup3[T1, T2, T3](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3]) extends PropertyGroup {
  type Callback = (T1, T2, T3) => Unit
  type PropertiesChangedHandlerType = PropertiesChangedHandler3[T1, T2, T3]
  class PropertiesChangedHandler3[T1, T2, T3](callback:Callback) extends PropertiesChangedHandler {
  	def apply() = callback(p1, p2, p3)
  	
    p1.change observe propertyChanged _
    p2.change observe propertyChanged _
    p3.change observe propertyChanged _
  }

  def onChanged(callback:Callback) = new PropertiesChangedHandler3(callback)
  def collect(callback:Callback) = new PropertiesChangedHandler3[T1, T2, T3](callback) with PropertiesChangedCollector
}

class PropertyGroup4[T1, T2, T3, T4](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4]) extends PropertyGroup {
  type Callback = (T1, T2, T3, T4) => Unit
  type PropertiesChangedHandlerType = PropertiesChangedHandler4[T1, T2, T3, T4]
  class PropertiesChangedHandler4[T1, T2, T3, T4](callback:Callback) extends PropertiesChangedHandler {
  	def apply() = callback(p1, p2, p3, p4)
  	
    p1.change observe propertyChanged _
    p2.change observe propertyChanged _
    p3.change observe propertyChanged _
    p4.change observe propertyChanged _
  }

  def onChanged(callback:Callback) = new PropertiesChangedHandler4(callback)
  def collect(callback:Callback) = new PropertiesChangedHandler4[T1, T2, T3, T4](callback) with PropertiesChangedCollector
}

class PropertyGroup5[T1, T2, T3, T4, T5](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5]) extends PropertyGroup {
  type Callback = (T1, T2, T3, T4, T5) => Unit
  type PropertiesChangedHandlerType = PropertiesChangedHandler5[T1, T2, T3, T4, T5]
  class PropertiesChangedHandler5[T1, T2, T3, T4, T5](callback:Callback) extends PropertiesChangedHandler {
  	def apply() = callback(p1, p2, p3, p4, p5)
  	
    p1.change observe propertyChanged _
    p2.change observe propertyChanged _
    p3.change observe propertyChanged _
    p4.change observe propertyChanged _
    p5.change observe propertyChanged _
  }

  def onChanged(callback:Callback) = new PropertiesChangedHandler5(callback)
  def collect(callback:Callback) = new PropertiesChangedHandler5[T1, T2, T3, T4, T5](callback) with PropertiesChangedCollector
}

class PropertyGroup6[T1, T2, T3, T4, T5, T6](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6]) extends PropertyGroup {
  type Callback = (T1, T2, T3, T4, T5, T6) => Unit
  type PropertiesChangedHandlerType = PropertiesChangedHandler6[T1, T2, T3, T4, T5, T6]
  class PropertiesChangedHandler6[T1, T2, T3, T4, T5, T6](callback:Callback) extends PropertiesChangedHandler {
  	def apply() = callback(p1, p2, p3, p4, p5, p6)
  	
    p1.change observe propertyChanged _
    p2.change observe propertyChanged _
    p3.change observe propertyChanged _
    p4.change observe propertyChanged _
    p5.change observe propertyChanged _
    p6.change observe propertyChanged _
  }

  def onChanged(callback:Callback) = new PropertiesChangedHandler6(callback)
  def collect(callback:Callback) = new PropertiesChangedHandler6[T1, T2, T3, T4, T5, T6](callback) with PropertiesChangedCollector
}

class PropertyGroup7[T1, T2, T3, T4, T5, T6, T7](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7]) extends PropertyGroup {
  type Callback = (T1, T2, T3, T4, T5, T6, T7) => Unit
  type PropertiesChangedHandlerType = PropertiesChangedHandler7[T1, T2, T3, T4, T5, T6, T7]
  class PropertiesChangedHandler7[T1, T2, T3, T4, T5, T6, T7](callback:Callback) extends PropertiesChangedHandler {
  	def apply() = callback(p1, p2, p3, p4, p5, p6, p7)
  	
    p1.change observe propertyChanged _
    p2.change observe propertyChanged _
    p3.change observe propertyChanged _
    p4.change observe propertyChanged _
    p5.change observe propertyChanged _
    p6.change observe propertyChanged _
    p7.change observe propertyChanged _
  }

  def onChanged(callback:Callback) = new PropertiesChangedHandler7(callback)
  def collect(callback:Callback) = new PropertiesChangedHandler7[T1, T2, T3, T4, T5, T6, T7](callback) with PropertiesChangedCollector
}

class PropertyGroup8[T1, T2, T3, T4, T5, T6, T7, T8](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7], p8: ReadOnlyProperty[T8]) extends PropertyGroup {
  type Callback = (T1, T2, T3, T4, T5, T6, T7, T8) => Unit
  type PropertiesChangedHandlerType = PropertiesChangedHandler8[T1, T2, T3, T4, T5, T6, T7, T8]
  class PropertiesChangedHandler8[T1, T2, T3, T4, T5, T6, T7, T8](callback:Callback) extends PropertiesChangedHandler {
  	def apply() = callback(p1, p2, p3, p4, p5, p6, p7, p8)
  	
    p1.change observe propertyChanged _
    p2.change observe propertyChanged _
    p3.change observe propertyChanged _
    p4.change observe propertyChanged _
    p5.change observe propertyChanged _
    p6.change observe propertyChanged _
    p7.change observe propertyChanged _
    p8.change observe propertyChanged _
  }

  def onChanged(callback:Callback) = new PropertiesChangedHandler8(callback)
  def collect(callback:Callback) = new PropertiesChangedHandler8[T1, T2, T3, T4, T5, T6, T7, T8](callback) with PropertiesChangedCollector
}

class PropertyGroup9[T1, T2, T3, T4, T5, T6, T7, T8, T9](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7], p8: ReadOnlyProperty[T8], p9: ReadOnlyProperty[T9]) extends PropertyGroup {
  type Callback = (T1, T2, T3, T4, T5, T6, T7, T8, T9) => Unit
  type PropertiesChangedHandlerType = PropertiesChangedHandler9[T1, T2, T3, T4, T5, T6, T7, T8, T9]
  class PropertiesChangedHandler9[T1, T2, T3, T4, T5, T6, T7, T8, T9](callback:Callback) extends PropertiesChangedHandler {
  	def apply() = callback(p1, p2, p3, p4, p5, p6, p7, p8, p9)
  	
    p1.change observe propertyChanged _
    p2.change observe propertyChanged _
    p3.change observe propertyChanged _
    p4.change observe propertyChanged _
    p5.change observe propertyChanged _
    p6.change observe propertyChanged _
    p7.change observe propertyChanged _
    p8.change observe propertyChanged _
    p9.change observe propertyChanged _
  }

  def onChanged(callback:Callback) = new PropertiesChangedHandler9(callback)
  def collect(callback:Callback) = new PropertiesChangedHandler9[T1, T2, T3, T4, T5, T6, T7, T8, T9](callback) with PropertiesChangedCollector
}

class PropertyGroup10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7], p8: ReadOnlyProperty[T8], p9: ReadOnlyProperty[T9], p10: ReadOnlyProperty[T10]) extends PropertyGroup {
  type Callback = (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) => Unit
  type PropertiesChangedHandlerType = PropertiesChangedHandler10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  class PropertiesChangedHandler10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](callback:Callback) extends PropertiesChangedHandler {
  	def apply() = callback(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10)
  	
    p1.change observe propertyChanged _
    p2.change observe propertyChanged _
    p3.change observe propertyChanged _
    p4.change observe propertyChanged _
    p5.change observe propertyChanged _
    p6.change observe propertyChanged _
    p7.change observe propertyChanged _
    p8.change observe propertyChanged _
    p9.change observe propertyChanged _
    p10.change observe propertyChanged _
  }

  def onChanged(callback:Callback) = new PropertiesChangedHandler10(callback)
  def collect(callback:Callback) = new PropertiesChangedHandler10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](callback) with PropertiesChangedCollector
}

class PropertyGroup11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7], p8: ReadOnlyProperty[T8], p9: ReadOnlyProperty[T9], p10: ReadOnlyProperty[T10], p11: ReadOnlyProperty[T11]) extends PropertyGroup {
  type Callback = (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) => Unit
  type PropertiesChangedHandlerType = PropertiesChangedHandler11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  class PropertiesChangedHandler11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](callback:Callback) extends PropertiesChangedHandler {
  	def apply() = callback(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11)
  	
    p1.change observe propertyChanged _
    p2.change observe propertyChanged _
    p3.change observe propertyChanged _
    p4.change observe propertyChanged _
    p5.change observe propertyChanged _
    p6.change observe propertyChanged _
    p7.change observe propertyChanged _
    p8.change observe propertyChanged _
    p9.change observe propertyChanged _
    p10.change observe propertyChanged _
    p11.change observe propertyChanged _
  }

  def onChanged(callback:Callback) = new PropertiesChangedHandler11(callback)
  def collect(callback:Callback) = new PropertiesChangedHandler11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](callback) with PropertiesChangedCollector
}

class PropertyGroup12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7], p8: ReadOnlyProperty[T8], p9: ReadOnlyProperty[T9], p10: ReadOnlyProperty[T10], p11: ReadOnlyProperty[T11], p12: ReadOnlyProperty[T12]) extends PropertyGroup {
  type Callback = (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) => Unit
  type PropertiesChangedHandlerType = PropertiesChangedHandler12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  class PropertiesChangedHandler12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](callback:Callback) extends PropertiesChangedHandler {
  	def apply() = callback(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12)
  	
    p1.change observe propertyChanged _
    p2.change observe propertyChanged _
    p3.change observe propertyChanged _
    p4.change observe propertyChanged _
    p5.change observe propertyChanged _
    p6.change observe propertyChanged _
    p7.change observe propertyChanged _
    p8.change observe propertyChanged _
    p9.change observe propertyChanged _
    p10.change observe propertyChanged _
    p11.change observe propertyChanged _
    p12.change observe propertyChanged _
  }

  def onChanged(callback:Callback) = new PropertiesChangedHandler12(callback)
  def collect(callback:Callback) = new PropertiesChangedHandler12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](callback) with PropertiesChangedCollector
}

class PropertyGroup13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7], p8: ReadOnlyProperty[T8], p9: ReadOnlyProperty[T9], p10: ReadOnlyProperty[T10], p11: ReadOnlyProperty[T11], p12: ReadOnlyProperty[T12], p13: ReadOnlyProperty[T13]) extends PropertyGroup {
  type Callback = (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) => Unit
  type PropertiesChangedHandlerType = PropertiesChangedHandler13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  class PropertiesChangedHandler13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](callback:Callback) extends PropertiesChangedHandler {
  	def apply() = callback(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13)
  	
    p1.change observe propertyChanged _
    p2.change observe propertyChanged _
    p3.change observe propertyChanged _
    p4.change observe propertyChanged _
    p5.change observe propertyChanged _
    p6.change observe propertyChanged _
    p7.change observe propertyChanged _
    p8.change observe propertyChanged _
    p9.change observe propertyChanged _
    p10.change observe propertyChanged _
    p11.change observe propertyChanged _
    p12.change observe propertyChanged _
    p13.change observe propertyChanged _
  }

  def onChanged(callback:Callback) = new PropertiesChangedHandler13(callback)
  def collect(callback:Callback) = new PropertiesChangedHandler13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](callback) with PropertiesChangedCollector
}

class PropertyGroup14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7], p8: ReadOnlyProperty[T8], p9: ReadOnlyProperty[T9], p10: ReadOnlyProperty[T10], p11: ReadOnlyProperty[T11], p12: ReadOnlyProperty[T12], p13: ReadOnlyProperty[T13], p14: ReadOnlyProperty[T14]) extends PropertyGroup {
  type Callback = (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) => Unit
  type PropertiesChangedHandlerType = PropertiesChangedHandler14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  class PropertiesChangedHandler14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](callback:Callback) extends PropertiesChangedHandler {
  	def apply() = callback(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14)
  	
    p1.change observe propertyChanged _
    p2.change observe propertyChanged _
    p3.change observe propertyChanged _
    p4.change observe propertyChanged _
    p5.change observe propertyChanged _
    p6.change observe propertyChanged _
    p7.change observe propertyChanged _
    p8.change observe propertyChanged _
    p9.change observe propertyChanged _
    p10.change observe propertyChanged _
    p11.change observe propertyChanged _
    p12.change observe propertyChanged _
    p13.change observe propertyChanged _
    p14.change observe propertyChanged _
  }

  def onChanged(callback:Callback) = new PropertiesChangedHandler14(callback)
  def collect(callback:Callback) = new PropertiesChangedHandler14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](callback) with PropertiesChangedCollector
}

class PropertyGroup15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7], p8: ReadOnlyProperty[T8], p9: ReadOnlyProperty[T9], p10: ReadOnlyProperty[T10], p11: ReadOnlyProperty[T11], p12: ReadOnlyProperty[T12], p13: ReadOnlyProperty[T13], p14: ReadOnlyProperty[T14], p15: ReadOnlyProperty[T15]) extends PropertyGroup {
  type Callback = (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) => Unit
  type PropertiesChangedHandlerType = PropertiesChangedHandler15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  class PropertiesChangedHandler15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](callback:Callback) extends PropertiesChangedHandler {
  	def apply() = callback(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15)
  	
    p1.change observe propertyChanged _
    p2.change observe propertyChanged _
    p3.change observe propertyChanged _
    p4.change observe propertyChanged _
    p5.change observe propertyChanged _
    p6.change observe propertyChanged _
    p7.change observe propertyChanged _
    p8.change observe propertyChanged _
    p9.change observe propertyChanged _
    p10.change observe propertyChanged _
    p11.change observe propertyChanged _
    p12.change observe propertyChanged _
    p13.change observe propertyChanged _
    p14.change observe propertyChanged _
    p15.change observe propertyChanged _
  }

  def onChanged(callback:Callback) = new PropertiesChangedHandler15(callback)
  def collect(callback:Callback) = new PropertiesChangedHandler15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](callback) with PropertiesChangedCollector
}

class PropertyGroup16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7], p8: ReadOnlyProperty[T8], p9: ReadOnlyProperty[T9], p10: ReadOnlyProperty[T10], p11: ReadOnlyProperty[T11], p12: ReadOnlyProperty[T12], p13: ReadOnlyProperty[T13], p14: ReadOnlyProperty[T14], p15: ReadOnlyProperty[T15], p16: ReadOnlyProperty[T16]) extends PropertyGroup {
  type Callback = (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) => Unit
  type PropertiesChangedHandlerType = PropertiesChangedHandler16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  class PropertiesChangedHandler16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](callback:Callback) extends PropertiesChangedHandler {
  	def apply() = callback(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16)
  	
    p1.change observe propertyChanged _
    p2.change observe propertyChanged _
    p3.change observe propertyChanged _
    p4.change observe propertyChanged _
    p5.change observe propertyChanged _
    p6.change observe propertyChanged _
    p7.change observe propertyChanged _
    p8.change observe propertyChanged _
    p9.change observe propertyChanged _
    p10.change observe propertyChanged _
    p11.change observe propertyChanged _
    p12.change observe propertyChanged _
    p13.change observe propertyChanged _
    p14.change observe propertyChanged _
    p15.change observe propertyChanged _
    p16.change observe propertyChanged _
  }

  def onChanged(callback:Callback) = new PropertiesChangedHandler16(callback)
  def collect(callback:Callback) = new PropertiesChangedHandler16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](callback) with PropertiesChangedCollector
}

class PropertyGroup17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7], p8: ReadOnlyProperty[T8], p9: ReadOnlyProperty[T9], p10: ReadOnlyProperty[T10], p11: ReadOnlyProperty[T11], p12: ReadOnlyProperty[T12], p13: ReadOnlyProperty[T13], p14: ReadOnlyProperty[T14], p15: ReadOnlyProperty[T15], p16: ReadOnlyProperty[T16], p17: ReadOnlyProperty[T17]) extends PropertyGroup {
  type Callback = (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) => Unit
  type PropertiesChangedHandlerType = PropertiesChangedHandler17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  class PropertiesChangedHandler17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](callback:Callback) extends PropertiesChangedHandler {
  	def apply() = callback(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17)
  	
    p1.change observe propertyChanged _
    p2.change observe propertyChanged _
    p3.change observe propertyChanged _
    p4.change observe propertyChanged _
    p5.change observe propertyChanged _
    p6.change observe propertyChanged _
    p7.change observe propertyChanged _
    p8.change observe propertyChanged _
    p9.change observe propertyChanged _
    p10.change observe propertyChanged _
    p11.change observe propertyChanged _
    p12.change observe propertyChanged _
    p13.change observe propertyChanged _
    p14.change observe propertyChanged _
    p15.change observe propertyChanged _
    p16.change observe propertyChanged _
    p17.change observe propertyChanged _
  }

  def onChanged(callback:Callback) = new PropertiesChangedHandler17(callback)
  def collect(callback:Callback) = new PropertiesChangedHandler17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](callback) with PropertiesChangedCollector
}

class PropertyGroup18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7], p8: ReadOnlyProperty[T8], p9: ReadOnlyProperty[T9], p10: ReadOnlyProperty[T10], p11: ReadOnlyProperty[T11], p12: ReadOnlyProperty[T12], p13: ReadOnlyProperty[T13], p14: ReadOnlyProperty[T14], p15: ReadOnlyProperty[T15], p16: ReadOnlyProperty[T16], p17: ReadOnlyProperty[T17], p18: ReadOnlyProperty[T18]) extends PropertyGroup {
  type Callback = (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) => Unit
  type PropertiesChangedHandlerType = PropertiesChangedHandler18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
  class PropertiesChangedHandler18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](callback:Callback) extends PropertiesChangedHandler {
  	def apply() = callback(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18)
  	
    p1.change observe propertyChanged _
    p2.change observe propertyChanged _
    p3.change observe propertyChanged _
    p4.change observe propertyChanged _
    p5.change observe propertyChanged _
    p6.change observe propertyChanged _
    p7.change observe propertyChanged _
    p8.change observe propertyChanged _
    p9.change observe propertyChanged _
    p10.change observe propertyChanged _
    p11.change observe propertyChanged _
    p12.change observe propertyChanged _
    p13.change observe propertyChanged _
    p14.change observe propertyChanged _
    p15.change observe propertyChanged _
    p16.change observe propertyChanged _
    p17.change observe propertyChanged _
    p18.change observe propertyChanged _
  }

  def onChanged(callback:Callback) = new PropertiesChangedHandler18(callback)
  def collect(callback:Callback) = new PropertiesChangedHandler18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](callback) with PropertiesChangedCollector
}

class PropertyGroup19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7], p8: ReadOnlyProperty[T8], p9: ReadOnlyProperty[T9], p10: ReadOnlyProperty[T10], p11: ReadOnlyProperty[T11], p12: ReadOnlyProperty[T12], p13: ReadOnlyProperty[T13], p14: ReadOnlyProperty[T14], p15: ReadOnlyProperty[T15], p16: ReadOnlyProperty[T16], p17: ReadOnlyProperty[T17], p18: ReadOnlyProperty[T18], p19: ReadOnlyProperty[T19]) extends PropertyGroup {
  type Callback = (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) => Unit
  type PropertiesChangedHandlerType = PropertiesChangedHandler19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]
  class PropertiesChangedHandler19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](callback:Callback) extends PropertiesChangedHandler {
  	def apply() = callback(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19)
  	
    p1.change observe propertyChanged _
    p2.change observe propertyChanged _
    p3.change observe propertyChanged _
    p4.change observe propertyChanged _
    p5.change observe propertyChanged _
    p6.change observe propertyChanged _
    p7.change observe propertyChanged _
    p8.change observe propertyChanged _
    p9.change observe propertyChanged _
    p10.change observe propertyChanged _
    p11.change observe propertyChanged _
    p12.change observe propertyChanged _
    p13.change observe propertyChanged _
    p14.change observe propertyChanged _
    p15.change observe propertyChanged _
    p16.change observe propertyChanged _
    p17.change observe propertyChanged _
    p18.change observe propertyChanged _
    p19.change observe propertyChanged _
  }

  def onChanged(callback:Callback) = new PropertiesChangedHandler19(callback)
  def collect(callback:Callback) = new PropertiesChangedHandler19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](callback) with PropertiesChangedCollector
}

class PropertyGroup20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7], p8: ReadOnlyProperty[T8], p9: ReadOnlyProperty[T9], p10: ReadOnlyProperty[T10], p11: ReadOnlyProperty[T11], p12: ReadOnlyProperty[T12], p13: ReadOnlyProperty[T13], p14: ReadOnlyProperty[T14], p15: ReadOnlyProperty[T15], p16: ReadOnlyProperty[T16], p17: ReadOnlyProperty[T17], p18: ReadOnlyProperty[T18], p19: ReadOnlyProperty[T19], p20: ReadOnlyProperty[T20]) extends PropertyGroup {
  type Callback = (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) => Unit
  type PropertiesChangedHandlerType = PropertiesChangedHandler20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]
  class PropertiesChangedHandler20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](callback:Callback) extends PropertiesChangedHandler {
  	def apply() = callback(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20)
  	
    p1.change observe propertyChanged _
    p2.change observe propertyChanged _
    p3.change observe propertyChanged _
    p4.change observe propertyChanged _
    p5.change observe propertyChanged _
    p6.change observe propertyChanged _
    p7.change observe propertyChanged _
    p8.change observe propertyChanged _
    p9.change observe propertyChanged _
    p10.change observe propertyChanged _
    p11.change observe propertyChanged _
    p12.change observe propertyChanged _
    p13.change observe propertyChanged _
    p14.change observe propertyChanged _
    p15.change observe propertyChanged _
    p16.change observe propertyChanged _
    p17.change observe propertyChanged _
    p18.change observe propertyChanged _
    p19.change observe propertyChanged _
    p20.change observe propertyChanged _
  }

  def onChanged(callback:Callback) = new PropertiesChangedHandler20(callback)
  def collect(callback:Callback) = new PropertiesChangedHandler20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](callback) with PropertiesChangedCollector
}

class PropertyGroup21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7], p8: ReadOnlyProperty[T8], p9: ReadOnlyProperty[T9], p10: ReadOnlyProperty[T10], p11: ReadOnlyProperty[T11], p12: ReadOnlyProperty[T12], p13: ReadOnlyProperty[T13], p14: ReadOnlyProperty[T14], p15: ReadOnlyProperty[T15], p16: ReadOnlyProperty[T16], p17: ReadOnlyProperty[T17], p18: ReadOnlyProperty[T18], p19: ReadOnlyProperty[T19], p20: ReadOnlyProperty[T20], p21: ReadOnlyProperty[T21]) extends PropertyGroup {
  type Callback = (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) => Unit
  type PropertiesChangedHandlerType = PropertiesChangedHandler21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]
  class PropertiesChangedHandler21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](callback:Callback) extends PropertiesChangedHandler {
  	def apply() = callback(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20, p21)
  	
    p1.change observe propertyChanged _
    p2.change observe propertyChanged _
    p3.change observe propertyChanged _
    p4.change observe propertyChanged _
    p5.change observe propertyChanged _
    p6.change observe propertyChanged _
    p7.change observe propertyChanged _
    p8.change observe propertyChanged _
    p9.change observe propertyChanged _
    p10.change observe propertyChanged _
    p11.change observe propertyChanged _
    p12.change observe propertyChanged _
    p13.change observe propertyChanged _
    p14.change observe propertyChanged _
    p15.change observe propertyChanged _
    p16.change observe propertyChanged _
    p17.change observe propertyChanged _
    p18.change observe propertyChanged _
    p19.change observe propertyChanged _
    p20.change observe propertyChanged _
    p21.change observe propertyChanged _
  }

  def onChanged(callback:Callback) = new PropertiesChangedHandler21(callback)
  def collect(callback:Callback) = new PropertiesChangedHandler21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](callback) with PropertiesChangedCollector
}

class PropertyGroup22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](p1: ReadOnlyProperty[T1], p2: ReadOnlyProperty[T2], p3: ReadOnlyProperty[T3], p4: ReadOnlyProperty[T4], p5: ReadOnlyProperty[T5], p6: ReadOnlyProperty[T6], p7: ReadOnlyProperty[T7], p8: ReadOnlyProperty[T8], p9: ReadOnlyProperty[T9], p10: ReadOnlyProperty[T10], p11: ReadOnlyProperty[T11], p12: ReadOnlyProperty[T12], p13: ReadOnlyProperty[T13], p14: ReadOnlyProperty[T14], p15: ReadOnlyProperty[T15], p16: ReadOnlyProperty[T16], p17: ReadOnlyProperty[T17], p18: ReadOnlyProperty[T18], p19: ReadOnlyProperty[T19], p20: ReadOnlyProperty[T20], p21: ReadOnlyProperty[T21], p22: ReadOnlyProperty[T22]) extends PropertyGroup {
  type Callback = (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) => Unit
  type PropertiesChangedHandlerType = PropertiesChangedHandler22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]
  class PropertiesChangedHandler22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](callback:Callback) extends PropertiesChangedHandler {
  	def apply() = callback(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20, p21, p22)
  	
    p1.change observe propertyChanged _
    p2.change observe propertyChanged _
    p3.change observe propertyChanged _
    p4.change observe propertyChanged _
    p5.change observe propertyChanged _
    p6.change observe propertyChanged _
    p7.change observe propertyChanged _
    p8.change observe propertyChanged _
    p9.change observe propertyChanged _
    p10.change observe propertyChanged _
    p11.change observe propertyChanged _
    p12.change observe propertyChanged _
    p13.change observe propertyChanged _
    p14.change observe propertyChanged _
    p15.change observe propertyChanged _
    p16.change observe propertyChanged _
    p17.change observe propertyChanged _
    p18.change observe propertyChanged _
    p19.change observe propertyChanged _
    p20.change observe propertyChanged _
    p21.change observe propertyChanged _
    p22.change observe propertyChanged _
  }

  def onChanged(callback:Callback) = new PropertiesChangedHandler22(callback)
  def collect(callback:Callback) = new PropertiesChangedHandler22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](callback) with PropertiesChangedCollector
  */
}

