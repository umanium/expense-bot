package config

import toml.Value
import toml.Value.{Str, Tbl}

import java.util.NoSuchElementException
import scala.io.{BufferedSource, Source}

trait Config:
  def c: Map[String, String]
  def get(key: String): String =
    c.get(key) match
      case Some(s: String) => s
      case _ => throw NoSuchElementException(s"The key '$key' is not found in the config")

object Config:
  def getMapFromToml(fileName: String, section: String): Map[String, String] =
    val fileSource: BufferedSource = Source.fromFile(fileName)
    val fileContent: String = fileSource.mkString

    toml.Toml.parse(fileContent) match
      case Left(_) => throw RuntimeException(s"Failed parsing the $fileName TOML file.")
      case Right(value) =>
        val sectionMap: Map[String, Value] = value.values
        val sectionContent: Tbl = sectionMap(section).asInstanceOf[Tbl]
        sectionContent.values.map((s,v) => (s, v.asInstanceOf[Str].value))
