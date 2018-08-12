package com.simplesearch

import scala.collection.immutable.HashMap
import Data._

import scala.annotation.tailrec

trait SearchServiceInterface {

  def buildFilesWordIndexes(filesLns: HashMap[FileName, List[FileLn]]):
    HashMap[FileName, WordIndex]

  def searchFileWordIndexesForWords(words: List[Word], filesWordIndexes: HashMap[FileName, WordIndex]):
    HashMap[FileName, ConfidencePercent]

}

trait SearchService extends SearchServiceInterface {

  def buildFilesWordIndexes(filesLns: HashMap[FileName, List[FileLn]]):
    HashMap[FileName, WordIndex] =
      filesLns map {
        case (fileName, lns) =>
          val fileWords =
            if (lns.isEmpty) List.empty[String]
            else lns.mkString(" ").split(" ").toList
          val fileWordIndex = fileWords.foldLeft(HashMap.empty[Word, WordCount]) {
            (wordIndex, fileWord) => {
              val indexedWord = wordIndex.getOrElse(fileWord, 0)

              wordIndex + (fileWord -> (indexedWord + 1))
            }
          }

          fileName -> fileWordIndex
      }

  def searchFileWordIndexesForWords(words: List[Word], filesWordIndexes: HashMap[FileName, WordIndex]):
    HashMap[FileName, ConfidencePercent] =
      filesWordIndexes.foldLeft(HashMap.empty[FileName, ConfidencePercent]){
        (confidenceMap, fileWordIndex) =>
          fileWordIndex match {
            case (fileName, wordIndex) =>
              val confidence = rateConfidence(words, wordIndex)

              if (confidence > 0) confidenceMap + (fileName -> confidence) else confidenceMap
          }
      }

  private def rateConfidence(words: List[Word], wordIndex: HashMap[Word, WordCount]):
    ConfidencePercent = {
      @tailrec
      def go(innerWords: List[Word], hit: Int, miss: Int): (Int, Int) =
        innerWords match {
          case Nil => (hit, miss)
          case word :: t =>
            val hitMiss = if (wordIndex.keys.exists(_ == word)) (1, 0) else (0, 1)

            go(t, hit + hitMiss._1, miss + hitMiss._2)
        }

      val (hit, miss) = go(words, 0, 0)

      if (hit > 0 && miss == 0) 100
      else if (hit == 0 && miss > 0) 0
      else (hit.toFloat / (hit + miss).toFloat * 100).toInt
    }

}
