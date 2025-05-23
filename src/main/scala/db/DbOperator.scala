package db

import java.sql.{Connection, DriverManager, ResultSet}
import scala.util.{Failure, Success, Using}

enum TableType:
  case String
  case Int
  case Double
  case Timestamp
  case Boolean

case class DbOperator(dbConfig: DbConfig):
  private val sqliteFilename: String = dbConfig.sqliteFilename
  private lazy val connection: Connection =
    Class.forName("org.sqlite.JDBC")
    DriverManager.getConnection(s"jdbc:sqlite:$sqliteFilename")

  def createTable(tableName: String, fields: Seq[(String, TableType)]): Unit =
    def getTypeString(tableType: TableType): String =
      tableType match
        case TableType.String => "TEXT"
        case TableType.Int => "INT"
        case TableType.Double => "NUMERIC"
        case TableType.Timestamp => "INT"
        case TableType.Boolean => "INT"

    Using(connection.createStatement()): stmt =>
      val fieldSql = fields.map((c, t) => s"${c.replace(' ', '_')} ${getTypeString(t)}")
      val createSql =
        s"CREATE TABLE IF NOT EXISTS $tableName (${fieldSql.mkString(",")})"

      stmt.execute(createSql)

  def insertSingleDataToTable(tableName: String, data: Seq[String]): Unit =
    insertDataToTable(tableName, Seq(data))

  def insertDataToTable(tableName: String, data: Seq[Seq[String]]): Unit =
    Using(connection.createStatement()): stmt =>
      val dataTuples: Seq[String] = data.map(d => s"(${d.map(v => s"\"$v\"").mkString(",")})")
      val insertSql =
        s"INSERT INTO $tableName VALUES ${dataTuples.mkString(",")}"

      stmt.execute(insertSql)

  def getDataInTable(tableName: String, limit: Int = 0): Seq[Seq[String]] =
    val limitStr: String = if limit > 0 then s"LIMIT ${limit.toString}" else ""
    val selectSql: String = s"SELECT * FROM $tableName $limitStr"

    getDataUsingQuery(selectSql)

  def getDataUsingQuery(query: String): Seq[Seq[String]] =
    val dbResult = Using(connection.createStatement()): stmt =>
      val resultSet: ResultSet = stmt.executeQuery(query)
      val columnsNumber: Int = resultSet.getMetaData.getColumnCount

      Iterator.from(0).takeWhile(_ => resultSet.next()).map(_ => Range.inclusive(1, columnsNumber).map(k => resultSet.getString(k))).toList

    dbResult match
      case Failure(exception) => throw exception
      case Success(value) => value

  def getTablesInDb: Seq[String] =
    val dbResult = Using(connection.createStatement()): stmt =>
      val selectSql: String = "SELECT name FROM sqlite_master WHERE type='table'"

      val resultSet: ResultSet = stmt.executeQuery(selectSql)
      Iterator.from(0).takeWhile(_ => resultSet.next()).map(_ => resultSet.getString(1)).toList

    dbResult match
      case Failure(exception) => throw exception
      case Success(value) => value

  def dropTable(tableName: String): Unit =
    Using(connection.createStatement()): stmt =>
      val dropSql: String = s"DROP TABLE $tableName"

      stmt.execute(dropSql)

  def deleteAllTables(): Unit =
    val tables: Seq[String] = getTablesInDb
    tables.foreach: table =>
      dropTable(table)
