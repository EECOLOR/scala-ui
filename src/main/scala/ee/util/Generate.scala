package ee.util

object Generate {

  def generateTypeClasses(name:String, i: Int) = 
    (1 to i) map (i => s"$name[T$i]") mkString ", "
    
  def generateProperties(name:String, propertyType:String, i: Int) = 
    (1 to i) map (i => s"$name$i: $propertyType[T$i]") mkString ", "
    
  def generateTypes(i: Int) = 
    (1 to i) map (i => s"T$i") mkString ", "
  
  def generateStrings(i: Int, prefix: String, suffix:String = "", separator:String = ", ") = 
    (1 to i) map (i => s"$prefix$i$suffix") mkString separator

}