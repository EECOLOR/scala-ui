package ee.ui.system

import scala.reflect.ClassTag
import scala.Some.apply

trait ClipBoard extends ClipBoardHelperMethods {

  def set(key:DataFormat, value:AnyRef): Boolean

  def get(key: DataFormat): Option[AnyRef]
  def getAs[T](key: DataFormat)(implicit m: ClassTag[T]): Option[T] =
    get(key)
      .flatMap { item =>
        if (m.runtimeClass isAssignableFrom item.getClass) Some(item.asInstanceOf[T])
        else None
      }

  def contains(key:DataFormat):Boolean
}

sealed case class DataFormat(ids: String*)

object DataFormat {
  object PLAIN_TEXT extends DataFormat("text/plain")
  object HTML extends DataFormat("text/html")
  object RTF extends DataFormat("text/rtf")
  object URL extends DataFormat("text/uri-list")
  object IMAGE extends DataFormat("application/x-java-rawimage")
  object FILES extends DataFormat("application/x-java-file-list", "java.file-list")
}

trait ClipBoardHelperMethods { self: ClipBoard =>

  	def containsString = contains(DataFormat.PLAIN_TEXT)
    def string_=(s:String) = set(DataFormat.PLAIN_TEXT, s)
    def string = getAs[String](DataFormat.PLAIN_TEXT)

    def containsUrl = contains(DataFormat.URL)
    def url_=(s:String) = set(DataFormat.URL, s)
    def url = getAs[String](DataFormat.URL)

    def containsHtml = contains(DataFormat.HTML)
    def html_=(s:String) = set(DataFormat.HTML, s)
    def html = getAs[String](DataFormat.HTML)

    def containsRtf = contains(DataFormat.RTF)
    def rtf_=(s:String) = set(DataFormat.RTF, s)
    def rtf = getAs[String](DataFormat.RTF)
}