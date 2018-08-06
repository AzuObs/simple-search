package com.orderbook.builder

import scala.collection.immutable.HashMap
import scala.util.Try

object Models {

  // IO is declared to make it clear we are performing an operation with side effects
  type IO[T] = Try[T]

  sealed trait Side
  final case class Bid() extends Side
  final case class Ask() extends Side

  sealed trait Instruction
  final case class New() extends Instruction
  final case class Update() extends Instruction
  final case class Delete() extends Instruction

  type Ticks = Int
  type USDPence = Int
  type LevelIndex = Int

  case class MarketEvent(
    instruction: Instruction,
    side: Side,
    priceLevelIndex: LevelIndex,
    price: Ticks,
    quantity: Int
  )

  case class Order(
    price: USDPence,
    quantity: Int
  )
  case class OrderBookEntry(
    bid: Option[Order],
    ask: Option[Order]
  )
  case class OrderBook(
    entries: HashMap[LevelIndex, OrderBookEntry]
  )

}
