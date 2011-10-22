package org.frankversnel.dnddice

import scala.util.parsing.combinator._

object RollParser extends JavaTokenParsers {
	def parse(input: String) = parseAll(roll, input)

	//1d20+1
	private def roll: Parser[Roll] = opt(count)~die~opt(modifier) ^^ {
			case None~die~None => Roll(die)
			case Some(count)~die~None => Roll(count, die)
			case None~die~Some(modifier) => Roll(die, modifier)
			case Some(count)~die~Some(modifier) => Roll(count, die, modifier) }

	// 1
	private def count: Parser[Int] = decimalNumber ^^ {
			case decimalNumber => decimalNumber.toInt }

	// d20
	private def die: Parser[Die] = "d"~decimalNumber ^^ {
			case "d"~decimalNumber => Die(decimalNumber.toInt) }

	// +1
	private def modifier: Parser[Modifier] = ("+"~decimalNumber | "-"~decimalNumber) ^^ {
			case "+"~decimalNumber => new Modifier(decimalNumber.toInt)
			case "-"~decimalNumber => new Modifier(decimalNumber.toInt * -1) }
}
