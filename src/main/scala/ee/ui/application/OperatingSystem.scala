package ee.ui.application

sealed abstract class OperatingSystem(val name:String) 

object OperatingSystem {
  object WINDOWS extends OperatingSystem("Windows")
  object MAC extends OperatingSystem("Mac")
  object LINUX extends OperatingSystem("Linux")
  object SOLARIS extends OperatingSystem("SunOS")
  object UNKNOWN extends OperatingSystem("Unknown")
  
  lazy val current:OperatingSystem =
    if (isWindows) WINDOWS
    else if (isMac) MAC
    else if (isLinux) LINUX
    else if (isSolaris) SOLARIS
    else UNKNOWN
    
  
  lazy val fullName = System getProperty "os.name"

  lazy val isWindows = fullName startsWith WINDOWS.name
  lazy val isMac = fullName startsWith MAC.name
  lazy val isLinux = fullName startsWith LINUX.name
  lazy val isSolaris = fullName startsWith SOLARIS.name
}
