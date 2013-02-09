package fr.simply.util

trait ContentType

object ContentType {
    def apply(contentType: String): ContentType = ContentTypeBuilder(contentType)
}

sealed case class ContentTypeBuilder(contentType: String) extends ContentType { override def toString = contentType }
case object Text_Plain extends ContentType { override def toString = "text/plain" }
case object Text_Json extends ContentType { override def toString = "text/json" }
case object Text_Html extends ContentType { override def toString = "text/html" }
case object Image_Jpeg extends ContentType { override def toString = "image/jpeg" }