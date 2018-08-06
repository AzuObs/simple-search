package com.orderbook

import scala.util.{Failure, Success}
import com.orderbook.builder._
import com.orderbook.builder.Models.USDPence

object Main
  extends App
    with FileService
    with MarketEventsService
    with OrderBookService {

  if (args.length != 3)
    throw new Exception("Arguments are expected to be filename book_depth tick_price")

  val filename = args(0)
  val bookDepth = args(1).toInt
  val tickPrice: USDPence = (args(2).toFloat * 100).toInt

  getFileLns(filename) match {
    case Failure(e) => println(s"Input file exception. ${e.getMessage}")
    case Success(lines) =>
      fromLinesToMarketEvents(lines, bookDepth) match {
        case Failure(e) => println(s"Input file exception. ${e.getMessage}")
        case Success(events) =>
          val book = fromMarketEventsToOrderBook(events, tickPrice)
          printOrderBook(book, bookDepth)
        }}

}
