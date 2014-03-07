# DnDDice

Clojure library that parses Dungeons & Dragons dice rolls (e.g. '1d20' etc.)
and rolls them.

## Features

* Parsing dice notation, e.g. 'd20', '2d6+1', '2*5d5' etc. 
	* Most stuff of the [dice
	  notation](http://en.wikipedia.org/wiki/Dice_notation) is supported in
	  the parser.
	* Parsed rolls are [edn](https://github.com/edn-format/edn) data
	  structures, so they can be easily serialized.
* Rolling (parsed) dice. 

## Usage

Require the following namespace:

	(ns example.core
	  (:require [dnddice.core :as dnddice]))

You can use the library as follows:

	(def dice-d20 (dnddice/parse-roll "d20")) ; {:sides 20}

	=> (dnddice/do-roll dice-d20)
	{:roll {:sides 20}, 
   :outcome (5), 
   :total 5}

	=> (dnddice/do-roll (dnddice/parse-roll "1d20"))
	{:roll {:sides 20, :die-count 1} 
   :outcome (7) 
   :total 7}

	=> (dnddice/do-roll (dnddice/parse-roll "2d5+1"))
	{:roll {:modifier {:operator "+", :value 1}, :sides 5, :die-count 2}
	 :outcome (1 2)
   :total 4}

## License

Copyright © 2013 Frank Versnel

Distributed under the Eclipse Public License, the same as Clojure.
