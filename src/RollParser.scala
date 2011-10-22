package org.frankversnel.dnddice

import scala.util.parsing.combinator._

trait RollParser extends JavaTokenParsers {
	protected def parse(input: String) = parseAll(roll, input)

	// 1d20+1
	private def roll: Parser[Roll] = opt(dieCount)~die~opt(modifier) ^^ {
			case None~die~None => Roll(die)
			case Some(dieCount)~die~None => Roll(dieCount, die)
			case None~die~Some(modifier) => Roll(die, modifier)
			case Some(dieCount)~die~Some(modifier) => Roll(dieCount, die, modifier) }

	// 1
	private def dieCount: Parser[Int] = decimalNumber ^^ {
			case decimalNumber => decimalNumber.toInt }

	// d20
	private def die: Parser[Die] = "d"~decimalNumber ^^ {
			case "d"~decimalNumber => Die(decimalNumber.toInt) }

	// +1
	private def modifier: Parser[Modifier] = ("+"~decimalNumber | "-"~decimalNumber) ^^ {
			case "+"~decimalNumber => Modifier(decimalNumber.toInt)
			case "-"~decimalNumber => Modifier(decimalNumber.toInt * -1) }
}
