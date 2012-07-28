package com.fmpwizard.utils

import net.liftweb.sitemap.{SiteMap, Menu}
import com.fmpwizard.model.TextTable
import net.liftweb.common.Full


/**
 * We define this PAths object to hold all of our sitemap
 * entries.
 */
object Paths{
  val post = Menu.param[TextTable]("post",
    "post",
    s => {
      s match {
        case "new" => Full(TextTable.create)
        case x  => TextTable.find(s)
      }

    } ,
    post => post.content) / "post"

  val index = Menu.i("index")/ "index"


  def siteMap = SiteMap(
    index,
    post
  )
}
