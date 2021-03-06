# DnDDice

Clojure(script) library that parses Dungeons & Dragons dice rolls (e.g. `1d20`)
and rolls them.

## Features

* Parsing dice notation, e.g. `d20`, `2d6+1`, `2x5d5`.
	* Most stuff of the [dice notation](http://en.wikipedia.org/wiki/Dice_notation) is supported in
	  the parser.
	* Parsed rolls are [edn](https://github.com/edn-format/edn) data
	  structures, so they can be serialized.
* Rolling (parsed) dice.

## To be done

* Support for dice arithmetic (e.g. `1d20x1d20`)
* Support for fudge dice syntax
* Support for exploding dice syntax

## Installation

DnDDice is available as a Maven artifact from Clojars.

With deps.edn:

```clojure
{org.fversnel/dnddice {:mvn/version "4.1.0"}}
```

With Leiningen/Boot:

```clojure
[org.fversnel/dnddice "4.1.0"]
```

## Usage

Require the following namespace:

```clojure
(ns example.core
  (:require [org.fversnel.dnddice.core :as dnddice]))
```

You can use the library as follows:

```clojure
(def d20 (dnddice/parse "d20")) ; {:sides 20}

=> (dnddice/roll d20)
{:roll {:sides 20},
 :die-rolls (5),
 :total 5}

=> (dnddice/roll (dnddice/parse "1d20"))
{:roll {:sides 20, :die-count 1},
 :die-rolls (7),
 :total 7}

=> (dnddice/roll (dnddice/parse "2d5+1"))
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

; Roll percentile dice
=> (dnddice/roll "d%")
{:roll {:sides "%"}, :die-rolls (79), :total 79}

; Parse and then re-construct
=> (dnddice/dice-expression (dnddice/parse "5d20+10"))
"5d20+10"
```

## License

Copyright © 2013-2021 Frank Versnel

Distributed under the Eclipse Public License, the same as Clojure.
