package ee.ui.propertiesAndEvents

import org.specs2.Specification
import ee.ui.events.Event
import scala.collection.mutable.ListBuffer
import ee.ui.events.ReadOnlyEvent
import ee.ui.TestUtils
import scala.tools.reflect.ToolBoxError
import ee.ui.properties.Property
import scala.language.reflectiveCalls
import ee.ui.observables.ObservableValue

object BindingsSpecification extends Specification {

  def is = "Bindings specification".title ^
    hide ^ end
    //show ^ end

  def hide = args(xonly=true) ^ show ^ end

  def show =
    """ Introduction
    
    	A binding is a connection between a bindable variable and an observable value
      """ ^
      "" ^
      br ^
      { // Simple binding
        val property1 = Property(0)
        val property2 = Property(1)

        property1 <== property2

        val initialValueSet = 1 ==== property1

        property2.value = 2

        initialValueSet and (2 ==== property1)
      } ^
      { // Mapped binding
        val property1 = Property("")
        val property2 = Property(1)

        property1 <== property2 map (_.toString)

        val initialValueSet = "1" ==== property1

        property2.value = 2

        initialValueSet and ("2" ==== property1)
      } ^
      { // Event binding
        val event = Event[Int]
        val property = Property[Option[Int]](Some(1))

        property <== event

        val initialValueSet = property.value ==== None

        event fire 2

        initialValueSet and (property.value ==== Some(2))
      } ^
      { // Mapped content binding using Option value

        val property1 = Property[Option[String]](None)
        val property2 = Property[Option[Int]](Some(1))

        import ObservableValue.mapContents._

        property1 <== property2 map (_.toString)

        val initialValueSet = property1.value ==== Some("1")
        
        property2.value = Some(2)
        
        initialValueSet and (property1.value ==== Some("2"))
        
      } ^
      { // Mapped content binding using FilterMonadic value

        val property1 = Property(Seq.empty[String])
        val property2 = Property(Seq(1, 2))

        import ObservableValue.mapContents._

        property1 <== property2 map (_.toString)

        val initialValueSet = property1.value ==== Seq("1", "2")
        
        property2.value = Seq(3, 4)
        
        initialValueSet and (property1.value ==== Seq("3", "4"))
      } ^
      { // Combined binding |
        
        val result = Property(-1)
        val input1 = Property(0)
        val input2 = Property(0)
        
        result <== input1 | input2 map {
          case (i1, i2) => i1 + i2
        }
        val t0 = 0 ==== result
        
        input1.value = 1
        val t1 = 1 ==== result
        
        input2.value = 1
        val t2 = 2 ==== result

        t0 and t1 and t2
      } ^ 
      { // Different types
        
        val result = Property(-1)
        val input1 = Property(0)
        val input2 = Property("")
        val input3 = Property('0')
        
        result <== input1 | input2 | input3 map {
          case (i1, i2, i3) => i1 + i2.length + i3
        }
        
        val t0 = 48 ==== result
        
        input1.value = 1
        val t1 = 49 ==== result
        
        input2.value = "1"
        val t2 = 50 ==== result

        input3.value = '1'
        val t3 = 51 ==== result
        
        t0 and t1 and t2 and t3
      } ^
      end
}