object DnDDice extends App {
  val Prompt = "DnDDice> "

  Console.println("example: \"d20\", \"4d8\"")
  var isRunning = true
  while(isRunning) {
    Console.print(Prompt)

    val input = Console.readLine.toLowerCase

    if(input.equals("exit")) {
      isRunning = false
    } else {
      try {
        val dIndex = input.lastIndexOf("d")

        val diceCount = if(dIndex > 0) {
          input.substring(0, dIndex).toInt
        } else {
          1
        }
        val diceSides = input.substring(dIndex + 1, input.size).toInt

        val rolls = Dice(diceSides).roll(diceCount)

        Console.print(rolls.toString + " [Total: " + rolls.total + "]\n")
      } catch {
        case e: NumberFormatException => Console.print("Wrong input, try again...\n")
        case es: StringIndexOutOfBoundsException => // Do nothing
      }
    }
  }
}
