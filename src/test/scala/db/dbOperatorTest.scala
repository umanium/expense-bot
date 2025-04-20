package db

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should

class dbOperatorTest extends AnyFunSpec with should.Matchers:
  val dbLocation: String = getClass.getResource("/test.db").getPath
  val dbConfig: DbConfig = DbConfig(Map("sqlite_filename" -> dbLocation))
  val op: DbOperator = DbOperator(dbConfig)
  val tableCols: Seq[(String, TableType)] = Seq(("id", TableType.Int), ("name", TableType.String))
  describe("createTable"):
    val tableName: String = "test_table"
    op.deleteAllTables()

    it("should create a table"):
      op.createTable(tableName, tableCols)

      op.getTablesInDb should contain (tableName)
      op.deleteAllTables()

  describe("insertSingleDataToTable"):
    val tableName: String = "test_insert_single_table"
    op.deleteAllTables()

    it("should insert one row of data to the table"):
      op.createTable(tableName, tableCols)

      op.insertSingleDataToTable(tableName, Seq("1", "Agus"))
      op.insertSingleDataToTable(tableName, Seq("2", "Dadang"))
      val tableData: Seq[Seq[String]] = op.getDataInTable(tableName)

      tableData.length shouldEqual 2
      tableData.head shouldEqual Seq("1", "Agus")
      tableData(1) shouldEqual Seq("2", "Dadang")
      op.deleteAllTables()

  describe("insertDataToTable"):
    val tableName: String = "test_insert_table"
    op.deleteAllTables()

    it("should insert multiple row of data to the table"):
      op.createTable(tableName, tableCols)

      op.insertDataToTable(tableName, Seq(Seq("1", "Agus"), Seq("2", "Dadang")))
      val tableData: Seq[Seq[String]] = op.getDataInTable(tableName)

      tableData.length shouldEqual 2
      tableData.head shouldEqual Seq("1", "Agus")
      tableData(1) shouldEqual Seq("2", "Dadang")
      op.deleteAllTables()
