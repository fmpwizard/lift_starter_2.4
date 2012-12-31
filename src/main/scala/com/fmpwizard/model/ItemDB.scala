package com.fmpwizard.model

import java.util.concurrent.ConcurrentHashMap

/**
 * Simulates a Database :)
 */
object ItemDB {
  val items: ConcurrentHashMap [String, BigDecimal] = new ConcurrentHashMap()
}
