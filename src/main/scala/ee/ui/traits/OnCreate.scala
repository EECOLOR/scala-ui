package ee.ui.traits

trait OnCreate extends DelayedInit {
  def onCreate:Unit
  
  def delayedInit(body: => Unit) = {
    body
    
    if ((body _).getClass.getDeclaringClass == this.getClass)
    {
      onCreate
    }
  }
}