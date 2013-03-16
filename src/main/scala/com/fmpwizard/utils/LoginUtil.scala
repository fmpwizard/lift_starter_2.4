package com.fmpwizard.utils

import com.fmpwizard.model.LDAPUser
import net.liftweb.http._
import net.liftweb.common._


object LoginUtil {
  def isLogged = LDAPUser.loggedIn_?

  def hasAuthority_?(name: String) : Boolean = {
    LoginUtil.isLogged && (LDAPUser.getRoles.count((element: String) => {element == name}) > 0)
  }

  def redirectIfLogged(path: String) : () => Box[LiftResponse] = {
    if(!LoginUtil.isLogged) {
      () => Full(RedirectResponse("/user_mgt/login"))
    }
    else {
      () => Full(RedirectResponse(path))
    }
  }
}

