package fr.simply

trait RestVerb

case object get extends RestVerb { override def toString = "GET" }
case object post extends RestVerb { override def toString = "POST" }