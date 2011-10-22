package org.frankversnel.dnddice

import scala.util.Random

class RollResult (val rolls: List[Int], val modifier: Modifier) {

	def count: Int = rolls.size
	def total: Int = rolls.sum + modifier.value

	override def toString = {
		rolls.map(_.toString).reduce(_ + " " + _) +
		" (" + modifier + ")" +
		" [Total:" + total + "]"
	}
}

class Roll private(times: Int, die: Die, modifier: Modifier) {

	def perform: RollResult = {
		val rolls = for(i <- 1 to times) yield die.roll
		return new RollResult(rolls.toList, modifier)
	}

	override def toString = times.toString + die.toString + modifier.toString
}
object Roll {
	private val neutralModifier = new Modifier(0)
	private val oneThrow = 1

	def apply(input: String): Option[Roll] = {
		val parsedRoll = RollParser.parse(input)
		if (parsedRoll.successful) Some(parsedRoll.get) else None
	}
	def apply(die: Die) = new Roll(oneThrow, die, neutralModifier)
	def apply(times: Int, die: Die) = new Roll(times, die, neutralModifier)
	def apply(die: Die, modifier: Modifier) = new Roll(oneThrow, die, modifier)
	def apply(times: Int, die: Die, modifier: Modifier) = new Roll(times, die, modifier)
}

class Modifier(val value: Int) {
	override def toString = if (value >= 0) "+" + value else value.toString
}

class Die private(val sides: Int, val numberGenerator: Random) {
	private val SideOffset = 1

	def roll = numberGenerator.nextInt(sides) + SideOffset
	override def toString = "d" + sides
}
object Die {
	private val NumberGenerator = new Random(System.currentTimeMillis)

	def apply(sides: Int) = createDie(sides)
	private def createDie(sides: Int) = new Die(sides, NumberGenerator)
}
