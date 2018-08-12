package com.simplesearch

import scala.collection.immutable.HashMap
import org.scalatest._

class SearchServiceSpec
  extends FlatSpec
    with Matchers
    with SearchService
    with FileService {

  "SearchService.buildFilesWordIndexes" should
    "builds word indexes from files" in {
      buildFilesWordIndexes(
        HashMap(
          "file_a" -> List("foo bar", "bar"),
          "file_b" -> List("someString"),
          "file_c" -> List())) shouldBe
            HashMap(
              "file_a" -> HashMap("foo" -> 1, "bar" -> 2),
              "file_b" -> HashMap("someString" -> 1),
              "file_c" -> HashMap.empty[String, Int]
            )
  }

  it should
    "builds an empty word index if file is empty" in {
      buildFilesWordIndexes(
        HashMap("file_a" -> List())) shouldBe
          HashMap("file_a" -> HashMap.empty[String, Int])
  }

  "SearchService.searchFileWordIndexesForWords" should
    "no matches yields are omitted from the result" in {
      val input = List("foo", "bar")
      val fileWordIndexes = HashMap(
        "file_a" -> HashMap("foo" -> 1, "bar" -> 2),
        "file_b" -> HashMap("someString" -> 1, "bar" -> 10),
        "file_c" -> HashMap.empty[String, Int]
      )
      searchFileWordIndexesForWords(input, fileWordIndexes) shouldBe HashMap(
        "file_a" -> 100,
        "file_b" -> 50
      )
  }

  it should
    "empty input yields no results" in {
      val input = List()
      val fileWordIndexes = HashMap(
        "file_a" -> HashMap("foo" -> 1, "bar" -> 2),
        "file_b" -> HashMap("someString" -> 1, "bar" -> 10, "somethingElse" -> 5),
        "file_c" -> HashMap.empty[String, Int]
      )
      searchFileWordIndexesForWords(input, fileWordIndexes) shouldBe HashMap.empty[String, Int]
  }

  it should
    "all matches yields a confidence rating of 100" in {
      val input = List("foo", "bar")
      val fileWordIndexes = HashMap(
        "file_a" -> HashMap("foo" -> 1, "bar" -> 2),
        "file_b" -> HashMap("bar" -> 1, "foo" -> 10, "somethingElse" -> 5)
      )
      searchFileWordIndexesForWords(input, fileWordIndexes) shouldBe HashMap(
        "file_a" -> 100,
        "file_b" -> 100
      )
  }

  it should
    "partial matches returns a percentage of matches found" in {
      val input = List("foo", "bar")
      val fileWordIndexes = HashMap(
        "file_a" -> HashMap("foo" -> 1, "bar" -> 2),
        "file_b" -> HashMap("bar" -> 10, "somethingElse" -> 5)
      )
      searchFileWordIndexesForWords(input, fileWordIndexes) shouldBe HashMap(
        "file_a" -> 100,
        "file_b" -> 50
      )
  }

}
