package fr.simply.internal.api

trait RestVerb { def toString: String }

case object Get     extends RestVerb { override def toString = "GET"     }
case object Post    extends RestVerb { override def toString = "POST"    }
case object Put     extends RestVerb { override def toString = "PUT"     }
case object Delete  extends RestVerb { override def toString = "DELETE"  }
case object Head    extends RestVerb { override def toString = "HEAD"    }
case object Options extends RestVerb { override def toString = "OPTIONS" }
case object Trace   extends RestVerb { override def toString = "TRACE"   }
case object Patch   extends RestVerb { override def toString = "PATCH"   }