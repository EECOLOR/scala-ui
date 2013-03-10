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

object PropertySpecification extends Specification {

  def is = "Property specification".title ^
    //hide ^ end
    show ^ end

  def hide = "Specification is hidden" ^ end

  def show =
    """ Introduction
    
    	A property is a simple object that represents a value that can change
      """ ^
      "" ^
      br ^
      "To improve usability properties have an implicit conversion for their value" ^
      { // Property value
        1 ==== Property(1)
      } ^
      "A property is usually defined like this for easy access" ^
      { // Defining a simple property
        val myObj = new {
          private val _property = Property(0)
          def property = _property
          def property_=(value: Int) = _property.value = value
        }

        myObj.property = 1

        1 ==== myObj.property
      } ^
      { // Defining an optional property
        val myObj = new {
          private val _property = Property[Option[Int]](None)
          def property = _property
          def property_=(value: Int) = _property.value = Some(value)
          def property_=(value: Option[Int]) = _property.value = value
        }

        myObj.property = 1
        val value1 = myObj.property.getOrElse(0)

        myObj.property = None
        val value2 = myObj.property.getOrElse(0)

        (value1 === 1) and (value2 === 0)
      } ^
      p ^
      """ Working with properties """ ^
      { // isDefault
        val property = Property(1)

        val wasDefault = property.isDefault === true

        property.value = 2

        wasDefault and (property.isDefault === false)
      } ^
      { // isChanged
        val property = Property(1)

        val wasChanged = property.isChanged === false

        property.value = 2

        wasChanged and (property.isChanged === true)
      } ^
      { // reset
        val property = Property(1)
        property.value = 2

        property.reset()

        property.value === 1
      } ^
      p ^
      " Change events " ^
      { // only new value
        var eventResult = -1

        val property = Property(0)
        property.change { newValue =>
          eventResult = newValue
        }
        property.value = 1

        eventResult === 1
      } ^
      { // old and new value
        var eventResult = (-1, -1)

        val property = Property(0)
        property.valueChange { valueChange =>
          val (oldValue, newValue) = valueChange
          eventResult = oldValue -> newValue
        }
        property.value = 1

        eventResult === (0 -> 1)
      } ^
      p ^ """ Bindings
      
        A property is a bindable variable. It can be bound to an observable value.
        
      """ ^
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
      end
}