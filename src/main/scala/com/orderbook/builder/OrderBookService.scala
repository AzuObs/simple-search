package com.orderbook.builder

import com.orderbook.builder.Models._
import scala.collection.immutable.HashMap
import scala.util.Try

trait OrderBookServiceInterface {

  def fromMarketEventsToOrderBook(events: Array[MarketEvent], tickPrice: USDPence)
    : OrderBook

  def printOrderBook(book: OrderBook, bookDepth: Int)
    : IO[Unit]

}

trait OrderBookService extends OrderBookServiceInterface {

  def fromMarketEventsToOrderBook(events: Array[MarketEvent], price: USDPence) =
    OrderBook(
      events.foldLeft(HashMap.empty[LevelIndex, OrderBookEntry])((entries, event) =>
        event match {
          case MarketEvent(_: New, side, index, ticks, qty) =>
            val entry = entries.getOrElse(index, OrderBookEntry(None, None))
            val updated = entry.copy(
              bid = side match {
                case _: Bid => Some(Order(ticks * price, qty))
                case _: Ask => entry.bid
              },
              ask = side match {
                case _: Bid => entry.ask
                case _: Ask => Some(Order(ticks * price, qty))
              }
            )
            entries + (index -> updated)
          case MarketEvent(_: Update, side, index, ticks, qty) =>
            val entry = entries(index)
            val updated = entry.copy(
              bid = side match {
                case _: Bid => Some(Order(ticks * price, qty))
                case _: Ask => entry.bid
              },
              ask = side match {
                case _: Bid => entry.ask
                case _: Ask => Some(Order(ticks * price, qty))
              }
            )
            entries + (index -> updated)
          case MarketEvent(_: Delete, side, index, _, _) =>
            val entry = entries(index)
            val deleted = entry.copy(
              bid = side match {
                case _: Bid => None
                case _: Ask => entry.bid
              },
              ask = side match {
                case _: Bid => entry.ask
                case _: Ask => None
              }
            )
            entries + (index -> deleted)
        }))

  def printOrderBook(book: OrderBook, depth: Int): IO[Unit] =
    Try {
      (1 to depth).foreach(n =>
        book.entries.get(n) match {
          case None => println("0.0,0,0.0,0")
          case Some(entry) =>
            val (bidPrice, bidQty) = entry.bid match {
              case Some(order) => (f"${order.price.toFloat / 100}%.1f", order.quantity.toString)
              case None => ("0.0", "0") }
            val (askPrice, askQty) = entry.ask match {
              case Some(order) => (f"${order.price.toFloat / 100}%.1f", order.quantity.toString)
              case None => ("0.0", "0") }

            println(s"$bidPrice,$bidQty,$askPrice,$askQty")
          })
    }

}
