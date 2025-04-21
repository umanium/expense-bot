package bot

object BotMessages:
  def expenseMessage(data: Seq[Seq[String]]): String =
    def turnRowToMessage(row: Seq[String]): String =
      s" - on date ${row(0)} you spend ${row(1)} ${row(2)} for ${row(3)}"

    s"""
      |Hi! We found a new expense entry:
      |${data.map(turnRowToMessage).mkString("\n")}
      |""".stripMargin
