# DnDDice

Clojure library that parses Dungeons & Dragons dice rolls (e.g. `1d20`)
and rolls them.

## Features

* Parsing dice notation, e.g. `d20`, `2d6+1`, `2x5d5`.
	* Most stuff of the [dice
	  notation](http://en.wikipedia.org/wiki/Dice_notation) is supported in
	  the parser.
	* Parsed rolls are [edn](https://github.com/edn-format/edn) data
	  structures, so they can be easily serialized.
* Rolling (parsed) dice.

## To be done

* Support for dice arithmetic (e.g. `1d20x1d20`)
* Support for percentile dice

## Installation

DnDDice is available as a Maven artifact from Clojars.

With Leiningen/Boot:

```clojure
[org.fversnel/dnddice "3.0.2"]
```

## Usage

Require the following namespace:

	(ns example.core
	  (:require [org.fversnel.dnddice.core :as dnddice]))

You can use the library as follows:

	(def dice-d20 (dnddice/parse "d20")) ; {:sides 20}

	=> (dnddice/perform-roll dice-d20)
	{:roll {:sides 20},
	 :die-rolls (5),
	 :total 5}

	=> (dnddice/perform-roll (dnddice/parse-roll "1d20"))
	{:roll {:sides 20, :die-count 1},
	 :die-rolls (7),
	 :total 7}

	=> (dnddice/perform-roll (dnddice/parse-roll "2d5+1"))
	{:roll {:modifier {:operator '+, :value 1}, :sides 5, :die-count 2},
	 :die-rolls (1 2),
	 :total 4}

	; Or roll directly
	=> (dnddice/roll "1d20")
	{:roll {:sides 20, :die-count 1},
	 :die-rolls (16),
	 :total 16}

	; Drop highest result
	(dnddice/roll "5d10-H") ; Use -L for drop lowest
	{:roll {:die-count 5, :sides 10, :drop :highest},
	 :die-rolls (7 5 9 9),
	 :total 30}

	; Parse and then re-construct
	=> (dnddice/dice-expression (dnddice/parse "5d20+10"))
	"5d20+10"

## License

Copyright © 2013-2016 Frank Versnel

Distributed under the Eclipse Public License, the same as Clojure.
