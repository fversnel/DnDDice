object DnDDice extends App {
  val Prompt = "DnDDice> "

  Console.println("example: \"d20 5\"")
  var isRunning = true
  while(isRunning) {
    Console.print(Prompt)

    val input = Console.readLine.toLowerCase

    if(input.equals("exit")) {
      isRunning = false
    } else {
      try {
        val rolls = if(input.size > 3) {
          val splitter = input.lastIndexOf(" ");
          val diceSides = input.substring(1, splitter).toInt

          val diceCount = input.substring(splitter + 1, input.size).toInt
          Dice(diceSides).roll(diceCount)
        } else {
          val diceSides = input.substring(1, input.size).toInt

          Dice(diceSides).rollOnce
        }
        Console.print(rolls.toString + " [Total: " + rolls.total + "]\n")
      } catch {
        case e: NumberFormatException => Console.print("Wrong input, try again...\n")
      }
    }
  }
}
