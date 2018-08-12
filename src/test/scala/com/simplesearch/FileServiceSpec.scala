package com.simplesearch

import org.scalatest._

class FileServiceSpec
  extends FlatSpec
    with Matchers
    with FileService {

  "FileService.getFileNames" should
    "get the filenames in a directory" in {
      getFileNames("./src/test/resources").get should contain only (
        "./src/test/resources/file_a.txt",
        "./src/test/resources/file_b.txt",
        "./src/test/resources/file_c.txt"
      )
  }

  it should
    "return an empty list if a directory is empty" in {
      getFileNames("./src/test/resources/empty").get shouldBe empty
  }

  it should
    "return a Failure if the directory doesn't exist" in {
      getFileNames("./src/test/resources/some-non-existant-dir").isFailure should be (true)
  }

  "FileService.getFilesLns" should
    "get the lines in a file" in {
      getFileLns("./src/test/resources/file_a.txt").get should contain only (
        "foo bar",
        "fandango and tango",
        "jibber jabber"
      )
  }

  it should
    "get an empty list if there are no lines" in {
      getFileLns("./src/test/resources/file_c.txt").get shouldBe empty
  }

  it should
    "return Failure if the file doesn't exist" in {
      getFileLns("./src/test/resources/non_existant_file").isFailure should be (true)
  }

}