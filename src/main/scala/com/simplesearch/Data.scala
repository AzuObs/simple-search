package com.simplesearch

import scala.util.Try
import scala.collection.immutable.HashMap

object Data {

  // make it obvious we have side effects
  type IO[T] = Try[T]

  type FileName = String

  type FileLn = String

  type Word = String

  type WordCount = Int

  type WordIndex = HashMap[Word, WordCount]

  type ConfidencePercent = Int

}