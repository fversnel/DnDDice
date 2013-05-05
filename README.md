# DnDDice

Clojure library that parses Dungeons & Dragons dice rolls (e.g. '1d20' etc.)
and rolls them.

## Features

* Parsing dice notation, e.g. 'd20' etc.
* Rolling (parsed) dice.
* Most stuff of the ([dice notation](http://en.wikipedia.org/wiki/Dice_notation)) is
  supported in the parser.
* Parsed rolls are valid edn so they can be easily serialized.

## Usage

Require the following namespace:

	(ns example.core
	  (:require [dnddice.core :as dnddice]))

You can use the library as follows:

	(def dice-d20 (dnddice/parse-roll "d20"))

	=> (dnddice/do-roll dice-d20)
	{:roll {:sides 20}, :outcome (5), :sum 5}

	=> (dnddice/do-roll (dnddice/parse-roll "1d20"))
	{:roll {:sides 20, :die-count 1}, :outcome (7), :sum 7}

	=> (dnddice/do-roll (dnddice/parse-roll "2d5+1"))
	{:roll {:modifier {:operator "+", :value 1}, :sides 5, :die-count 2},
	 :outcome (1 2), :sum 4}

(Note that both the die count and the modifier are optional.)

## License

Copyright © 2013 Frank Versnel

Distributed under the Eclipse Public License, the same as Clojure.
