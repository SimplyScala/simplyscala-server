package fr.simply.util

case class ContentType(contentType: String) {
    override def toString = contentType
}

object Text_Plain extends ContentType("text/plain")
object Text_Json extends ContentType("text/json")
object Text_Html extends ContentType("text/html")
object Image_Jpeg extends ContentType("image/jpeg")