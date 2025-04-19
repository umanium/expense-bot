package bot

import config.Config

case class BotConfig(c: Map[String, String]) extends Config

object BotConfig:
  def fromTomlFile(fileName: String): BotConfig =
    BotConfig(Config.getMapFromToml(fileName, "bot"))
