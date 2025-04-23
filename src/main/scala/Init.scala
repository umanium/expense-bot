import db.{DbConfig, DbOperator, TableType}
import gsheet.GsheetConfig
import readsgsheet.spreadsheet.{Cell, DataCrawler}
import mainargs.{main, arg, ParserForMethods}

object Init:
  @main def init(@arg(name="config", short='c', doc="Path of the file to the config file")
                 configToml: String): Unit =
    // initialize configs
    val dbConfig: DbConfig = DbConfig.fromTomlFile(configToml)
    val gsheetConfig: GsheetConfig = GsheetConfig.fromTomlFile(configToml)

    // read columns from gsheet
    val dataCrawler: DataCrawler = DataCrawler(gsheetConfig.spreadsheet, gsheetConfig.serviceAccount)
    val header: Seq[String] = dataCrawler.getHeader(Cell(1, 1), 5)
    val tableHeader: Seq[(String, TableType)] = ("row", TableType.Int) +: header.map(h => (h.toLowerCase.replace(' ', '_'), TableType.String))

    // create table
    val dbOperator: DbOperator = DbOperator(dbConfig)
    dbOperator.dropTable(dbConfig.dataTableName)
    dbOperator.createTable(dbConfig.dataTableName, tableHeader)
    println(s"table ${dbConfig.dataTableName} had been created")

    // read initial data
    val data: Seq[Seq[String]] = dataCrawler.getData(Cell(1, 2), header, 20, includeRowNumber = true, continuous = true)
    dbOperator.insertDataToTable(dbConfig.dataTableName, data)
    println("data had been initialized")

  def main(args: Array[String]): Unit = ParserForMethods(this).runOrExit(args.toIndexedSeq)

