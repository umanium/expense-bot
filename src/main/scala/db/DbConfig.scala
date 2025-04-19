package db

import config.Config

case class DbConfig(c: Map[String, String]) extends Config

object DbConfig:
  def fromTomlFile(fileName: String): DbConfig =
    DbConfig(Config.getMapFromToml(fileName, "database"))
