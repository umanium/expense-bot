package db

import config.Config

case class DbConfig(c: Map[String, String]) extends Config:
  def sqliteFilename: String = get("sqlite_filename")
  def dataTableName: String = get("data_table_name")

object DbConfig:
  def fromTomlFile(fileName: String): DbConfig =
    DbConfig(Config.getMapFromToml(fileName, "database"))
