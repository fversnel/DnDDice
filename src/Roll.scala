package org.frankversnel.dnddice

import scala.util.Random

class RollResult (val rolls: List[Int], val modifier: Modifier) {

	def dieCount: Int = rolls.size
	def total: Int = rolls.sum + modifier.value

	override def toString = {
		rolls.map(_.toString).reduce(_ + " " + _) +
		" (" + modifier + ")" +
		" [Total:" + total + "]"
	}
}

class Roll private(val dieCount: Int, val die: Die, val modifier: Modifier) {

	def perform: RollResult = {
		val rolls = for(i <- 1 to dieCount) yield die.roll
		return new RollResult(rolls.toList, modifier)
	}

	override def toString = dieCount.toString + die.toString + modifier.toString
}
object Roll extends RollParser {
	private val neutralModifier = Modifier(0)
	private val oneThrow = 1

	def apply(input: String): Option[Roll] = {
		val parsedRoll = parse(input)
		if (parsedRoll.successful) Some(parsedRoll.get) else None
	}
	def apply(die: Die) = new Roll(oneThrow, die, neutralModifier)
	def apply(dieCount: Int, die: Die) = new Roll(dieCount, die, neutralModifier)
	def apply(die: Die, modifier: Modifier) = new Roll(oneThrow, die, modifier)
	def apply(dieCount: Int, die: Die, modifier: Modifier) = new Roll(dieCount, die, modifier)
}

class Modifier private(val value: Int) {
	override def toString = if (value >= 0) "+" + value else value.toString
}
object Modifier {
	def apply(value: Int) = new Modifier(value)
}

class Die private(val sides: Int, val numberGenerator: Random) {
	private val SideOffset = 1

	def roll = numberGenerator.nextInt(sides) + SideOffset

	override def toString = "d" + sides
}
object Die {
	private val NumberGenerator = new Random(System.currentTimeMillis)

	def apply(sides: Int) = new Die(sides, NumberGenerator)
}
