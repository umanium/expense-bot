package gsheet

import readsgsheet.auth.GoogleServiceAccount
import config.Config
import readsgsheet.spreadsheet.Spreadsheet

case class GsheetConfig(c: Map[String, String]) extends Config:
  private def serviceAccountFile: String = get("service_account_file")
  def serviceAccount: GoogleServiceAccount = GoogleServiceAccount.fromJson(serviceAccountFile)

  private def googleSheetId: String = get("google_sheet_id")
  private def sheetName: String = get("sheet_name")
  def spreadsheet: Spreadsheet = Spreadsheet(googleSheetId, sheetName)

object GsheetConfig:
  def fromTomlFile(fileName: String): GsheetConfig =
    GsheetConfig(Config.getMapFromToml(fileName, "gsheet"))
