package com.simplesearch

import scala.util.{Try, Success}
import scala.collection.immutable.HashMap
import scala.io.StdIn.readLine
import Data._

trait TerminalServiceInterface {

  def runSimpleSearchPrompt(filesWordIndexes: HashMap[FileName, WordIndex]): IO[Unit]

}

trait TerminalService
  extends TerminalServiceInterface
    with SearchService {

  def runSimpleSearchPrompt(filesWordIndexes: HashMap[FileName, WordIndex]): IO[Unit] = Try {
    val input = readLine("Please input a search: ").split(" ").toList

    if (input == List("quit")) return Success()

    val confidenceMap = searchFileWordIndexesForWords(input, filesWordIndexes)

    if (confidenceMap.isEmpty)
      println(s"${input.mkString(" ")} does not match anything")
    else
      confidenceMap.toSeq.sortBy(_._1).foreach {
        case (fileName, confidence) => println(s"$fileName: contains $confidence% of your words")
      }
    runSimpleSearchPrompt(filesWordIndexes)
  }

}
