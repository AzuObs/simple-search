package com.orderbook.builder

import com.orderbook.builder.Models._
import scala.util.Try

trait MarketEventsServiceInterface {

  def fromLinesToMarketEvents(lines: Array[String], bookDepth: Int)
    : Try[Array[MarketEvent]]

}

trait MarketEventsService extends MarketEventsServiceInterface {

  def fromLinesToMarketEvents(lines: Array[String], depth: Int): Try[Array[MarketEvent]] =
    Try {
      lines.foldLeft(Array.empty[MarketEvent])((events, line) => {
        val splits: Array[String] = line.split(" ")
        val instruction: Instruction = splits(0) match {
          case "N" => New()
          case "U" => Update()
          case "D" => Delete()
          case _ => throw new Exception("Unable to parse event's Instruction.")
        }
        val side: Side = splits(1) match {
          case "B" => Bid()
          case "A" => Ask()
          case _ => throw new Exception("Unable to parse event's Side.")
        }
        val priceLevelIndex: Int = splits(2).toInt match {
          case neg if neg < 1 =>
            throw new Exception("Event's price level index cannot be less than 1.")
          case index => index
        }
        val price: Ticks = splits(3).toInt match {
          case neg if neg < 0 => throw new Exception("Event's ticks cannot be less than 0.")
          case i => i
        }
        val quantity: Int = splits(4).toInt match {
          case neg if neg < 0 => throw new Exception("Event's quantity cannot be less than 0.")
          case q => q
        }

        if (priceLevelIndex > depth)
          events
        else
          events :+ MarketEvent(
            instruction,
            side,
            priceLevelIndex,
            price,
            quantity
          )
      })
    }

}
