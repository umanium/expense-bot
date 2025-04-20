package bot

import config.Config

case class BotConfig(c: Map[String, String]) extends Config:
  def token: String = get("token")
  def chatId: String = get("chat_id")

object BotConfig:
  def fromTomlFile(fileName: String): BotConfig =
    BotConfig(Config.getMapFromToml(fileName, "bot"))
