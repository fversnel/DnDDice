# DnDDice

Clojure library that parses Dungeons & Dragons dice rolls (e.g. '1d20' etc.)
and rolls them.

## Quickstart

Require the following namespace 

	(ns example.core
	  (:require [dnddice.core :as dnddice]))

You can use the library as follows:

	=> (dnddice/roll "1d20")
	{:roll {:sides 20, :die-count 1}, :outcome (7), :sum 7}

	=> (dnddice/roll "2d5+1")
	{:roll {:modifier 1, :sides 5, :die-count 2}, :outcome (1 4), :sum 6}

(Note that both the die count and the modifier are optional.)
