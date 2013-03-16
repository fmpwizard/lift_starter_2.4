package com.fmpwizard.model

import net.liftweb.ldap._
import net.liftweb.common._


/**
 * The singleton that has methods for accessing the database
 */
object LDAPUser extends LDAPUser with MetaLDAPProtoUser[LDAPUser] {

  override def screenWrap = Full(<lift:surround with="default" at="content">
      <lift:bind /></lift:surround>)


  override def loginErrorMessage  = "'%s' is not a valid user or password does not match"
  override def ldapUserSearch     = "(&(objectClass=inetOrgPerson)(uid=%s))"

  override def rolesNameRegex     = ".*cn=(.[^,]*),.*"
  override def rolesSearchFilter  = "(&(objectclass=groupofnames)(!(cancellationdate=*))(member=%s))"
  //override so that you can read values from props files
  override val ldapVendor = new LDAPVendor()


}

class LDAPUser extends LDAPProtoUser[LDAPUser] {
  def getSingleton = LDAPUser
}
