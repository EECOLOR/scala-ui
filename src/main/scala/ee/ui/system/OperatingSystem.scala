package ee.ui.system

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

  private lazy val isWindows = fullName startsWith WINDOWS.name
  private lazy val isMac = fullName startsWith MAC.name
  private lazy val isLinux = fullName startsWith LINUX.name
  private lazy val isSolaris = fullName startsWith SOLARIS.name
}
