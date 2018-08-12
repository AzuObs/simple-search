package com.simplesearch

import java.io.File

import scala.io.Source
import scala.collection.immutable.HashMap
import scala.util.{Try, Success, Failure}

import Data._

trait FileServiceInterface {

  def getFileNames(directory: String): IO[List[FileName]]

  def getFilesLns(filenames: List[FileName]): IO[HashMap[FileName, List[FileLn]]]

}

trait FileService extends FileServiceInterface {

  def getFileNames(directory: String): IO[List[FileName]] = Try {
    new File(directory)
      .listFiles
      .filter(_.isFile)
      .map(_.toString)
      .toList
  }

  def getFilesLns(filenames: List[FileName]): IO[HashMap[FileName, List[FileLn]]] = Try {
    filenames.foldLeft(HashMap.empty[FileName, List[FileLn]]) {
      (map, filename) => {
        val lns: List[FileLn] = getFileLns(filename) match {
          case Success(lns) => lns
          case Failure(_) => throw new Exception(s"Unable to read file line for $filename")
        }
        map + (filename -> lns)
      }
    }
  }

  protected def getFileLns(filename: FileName): IO[List[FileLn]] = Try {
    Source
      .fromFile(filename)
      .getLines()
      .toList
  }

}
