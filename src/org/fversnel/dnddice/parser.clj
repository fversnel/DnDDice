(ns org.fversnel.dnddice.parser
  (:require [clojure.edn :as edn]
            [instaparse.core :as insta]))

(def roll-parser
  (insta/parser
    "<dice-expression> = (dice | value) (operator dice-expression)* drop-roll?

     dice = die-count? sides
     die-count = integer-above-zero
     sides = <('d'|'D')> (integer-above-zero | '%')

     operator = '+' | '-' | '/' | 'x'
     value = integer
     drop-roll = <'-'> ('L' | 'H')

     integer-above-zero = #'[1-9][0-9]*'
     integer = #'[0-9]+'"))

; [[:dice {:sides x :die-count y}]
;  operator
;  [:dice {:sides x :die-count y}]
;  operator
;  [:value value]
;  [:drop type]

(def roll-transform-options
  {:dice (fn [& parts]
           [:dice (apply hash-map (flatten parts))])
   :integer-above-zero edn/read-string
   :integer edn/read-string
   :operator #(case % "+" '+
                      "-" '-
                      "x" '*
                      "/" '/)
   :drop-roll #(case % "L" [:drop :lowest]
                       "H" [:drop :highest])})

(defn parse [^String input-str]
  (->> (roll-parser input-str)
    (insta/transform roll-transform-options)))