package bot

import sttp.client4.quick.*
import sttp.client4.Response
import sttp.model.StatusCode

case class TelegramBot(config: BotConfig):
  def sendMessage(message: String): Unit =
    val token = config.token
    val chat_id = config.chatId
    val response: Response[String] = quickRequest
      .post(uri"https://api.telegram.org/bot$token/sendMessage")
      .body(Map("chat_id" -> chat_id, "text" -> message))
      .send()

    response.code match
      case StatusCode.Ok => println(s"Message has been sent successfully")
      case _ => throw RuntimeException(s"Send message failed, code: ${response.code}, body: ${response.body}")
