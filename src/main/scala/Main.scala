import mainargs.{main, arg, ParserForMethods}

object Main:
  @main def hello(@arg(name="name", short='n', doc="Name to say hello")
                name: Option[String]): Unit =
    name match
      case Some(value: String) => println(s"Hello $value!")
      case None => println(s"No one to say hello :(")

  def main(args: Array[String]): Unit = ParserForMethods(this).runOrExit(args)
