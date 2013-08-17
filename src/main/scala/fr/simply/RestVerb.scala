package fr.simply

class RestVerb(restVerb: String) { override def toString = restVerb }

case object Get extends RestVerb("GET")
case object Post extends RestVerb("POST")
case object Put extends RestVerb("PUT")
case object Delete extends RestVerb("DELETE")
case object Head extends RestVerb("HEAD")
case object Options extends RestVerb("OPTIONS")
case object Trace extends RestVerb("TRACE")
case object Patch extends RestVerb("PATCH")

/*class Method(method: String) {
  def unapply[T](req: HttpRequest[T]) =
    if (req.method.equalsIgnoreCase(method)) Some(req)
    else None
}

object GET extends Method("GET")
object POST extends Method("POST")
object PUT extends Method("PUT")
object DELETE extends Method("DELETE")
object HEAD extends Method("HEAD")
object CONNECT extends Method("CONNECT")
object OPTIONS extends Method("OPTIONS")
object TRACE extends Method("TRACE")
object PATCH extends Method("PATCH")
object LINK extends Method("LINK")
object UNLINK extends Method("UNLINK")*/