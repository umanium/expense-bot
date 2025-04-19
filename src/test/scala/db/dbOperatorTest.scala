package db

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should

class dbOperatorTest extends AnyFunSpec with should.Matchers:
  val dbLocation: String = getClass.getResource("/test.db").getPath
  val op: dbOperator = dbOperator(dbLocation)
  val tableCols: Map[String, TableType] = Map("id" -> TableType.Int, "name" -> TableType.String)
  describe("createTable"):
    val tableName: String = "test_table"
    op.deleteAllTables()

    it("should create a table"):
      op.createTable(tableName, tableCols)

      op.getTablesInDb should contain (tableName)
      op.deleteAllTables()

  describe("insertDataToTable"):
    val tableName: String = "test_insert_table"
    op.deleteAllTables()

    it("should insert one row of data to the table"):
      op.createTable(tableName, tableCols)

      op.insertDataToTable(tableName, Seq("1", "Agus"))
      op.insertDataToTable(tableName, Seq("2", "Dadang"))
      val tableData: Seq[Seq[String]] = op.getDataInTable(tableName)

      tableData.length shouldEqual 2
      tableData.head shouldEqual Seq("1", "Agus")
      tableData(1) shouldEqual Seq("2", "Dadang")
      op.deleteAllTables()
