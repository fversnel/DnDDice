import scala.util.Random

class RollResult private(val rolls: List[Int]) {

  def count: Int = rolls.size
  def total: Int = rolls.reduce(_+_)

  override def toString = rolls.map(_.toString).reduce(_ + " " + _)
}
object RollResult {
  def apply(roll: Int) = new RollResult(List(roll))
  def apply(rolls: List[Int]) = new RollResult(rolls)
}


class Dice private(val sides: Int, val numberGenerator: Random) {
  private val SideOffset = 1

  def rollOnce: RollResult = RollResult(roll)

  def roll(times: Int): RollResult = {
    val rolls = for(i <- 1 to times) yield roll
    return RollResult(rolls.toList)
  }

  private def roll = numberGenerator.nextInt(sides) + SideOffset

  override def toString = "d" + sides
}
object Dice {
  private val NumberGenerator = new Random(System.currentTimeMillis)

  def apply(sides: Int) = createDice(sides)

  def d20 = createDice(20)
  def d12 = createDice(12)
  def d10 = createDice(10)
  def d8 = createDice(8)
  def d6 = createDice(6)
  def d4 = createDice(4)

  private def createDice(sides: Int) = new Dice(sides, NumberGenerator)
}
