package com.fmpwizard.model

import net.liftweb.mapper._
import net.liftweb.common.Loggable



class TextTable extends LongKeyedMapper[TextTable] {

  def getSingleton = TextTable
  def primaryKeyField = blogID

  object blogID   extends MappedLongIndex(this)
  object content  extends MappedText(this)
}

object TextTable extends TextTable with LongKeyedMetaMapper[TextTable] with Loggable {
  override def dbTableName = "blog_posts"
}
