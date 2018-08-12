package com.simplesearch

import scala.util.{Failure, Success}

object Main
  extends App
    with SearchService
    with FileService
    with TerminalService {

  if (args.length != 1) throw new Exception("Expected one argument.")

  val directory = args(0)

  getFileNames(directory) match {
    case Failure(e) => println(s"Unable to read directory. Exception: ${e.getMessage}")
    case Success(fileNames) =>
      if (fileNames.isEmpty) println("No files existin directory")
      else {
        println(s"${fileNames.length} files read in directory $directory")
        getFilesLns(fileNames) match {
          case Failure(e) => println(s"Unable to read file lines. Exception: ${e.getMessage}")
          case Success(filesLns) => runSimpleSearchPrompt(buildFilesWordIndexes(filesLns))
        }
      }
  }

}
