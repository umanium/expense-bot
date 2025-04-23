import bot.{BotConfig, BotMessages, TelegramBot}
import db.{DbConfig, DbOperator}
import gsheet.GsheetConfig
import mainargs.{ParserForMethods, arg, main}
import org.joda.time.DateTime
import readsgsheet.spreadsheet.{Cell, DataCrawler}

object Main:
  private def printWithTime(s: String): Unit =
    val time: String = DateTime.now().toString("YYYY-MM-dd HH:mm:ss")
    println(s"[$time] $s")

  @main def botMain(@arg(name="config", short='c', doc="Path of the file to the config file")
                  configToml: String): Unit =
    // initialize configs
    val dbConfig: DbConfig = DbConfig.fromTomlFile(configToml)
    val botConfig: BotConfig = BotConfig.fromTomlFile(configToml)
    val gsheetConfig: GsheetConfig = GsheetConfig.fromTomlFile(configToml)

    def run(): Unit =
      try
        // get the latest row from the table
        val rowSql: String = s"SELECT MAX(row) FROM ${dbConfig.dataTableName}"
        val dbOperator: DbOperator = DbOperator(dbConfig)
        val maxRow: Int = dbOperator.getDataUsingQuery(rowSql).head.head.toInt

        // read data from the maxRow
        val dataCrawler: DataCrawler = DataCrawler(gsheetConfig.spreadsheet, gsheetConfig.serviceAccount)
        val header: Seq[String] = dataCrawler.getHeader(Cell(1, 1), 5)
        val data: Seq[Seq[String]] = dataCrawler.getData(Cell(1, maxRow+1), header, 5, includeRowNumber = true, continuous = true)

        if data.nonEmpty then
          printWithTime(s"${data.length} row of data extracted, will send message")
          // insert to table, then send to telegram
          dbOperator.insertDataToTable(dbConfig.dataTableName, data)

          // send to telegram
          val selectSql: String = s"SELECT tanggal, jumlah, mata_uang, untuk_apa FROM ${dbConfig.dataTableName} WHERE row > $maxRow"
          val dataToSend: Seq[Seq[String]] = dbOperator.getDataUsingQuery(selectSql)
          val messageToSend: String = BotMessages.expenseMessage(dataToSend)
          val teleBot = TelegramBot(botConfig)
          teleBot.sendMessage(messageToSend)

        else
          printWithTime("no data extracted")
      catch
        case e: Throwable =>
          printWithTime("Got an error")
          e.printStackTrace()

      Thread.sleep(30000)

    val iter: Iterator[Unit] = Iterator.continually(run())
    iter.takeWhile(_ => true).foreach(_ => ())

  def main(args: Array[String]): Unit = ParserForMethods(this).runOrExit(args.toIndexedSeq)
