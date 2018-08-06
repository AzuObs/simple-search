package com.orderbook.builder

import Models.IO

import scala.io.Source
import scala.util.Try

trait FileServiceInterface {

  def getFileLns(filename: String): IO[Array[String]]

}

trait FileService extends FileServiceInterface {

  def getFileLns(filename: String): IO[Array[String]] = Try {
    Source
      .fromFile(filename)
      .getLines()
      .toArray
  }

}
