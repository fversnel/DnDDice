(ns org.fversnel.dnddice.parser
  (:require [clojure.edn :as edn]
            [instaparse.core :as insta]))

(def roll-parser
  (insta/parser
    "<roll> = (post-fix-roll | pre-fix-roll)
     <post-fix-roll> = dice post-fix-modifier? drop-roll?
     <pre-fix-roll> = pre-fix-modifier? dice drop-roll?
     <dice> = die-count? sides

     die-count = integer-above-zero
     sides = <('d'|'D')> (integer-above-zero | '%')
     drop-roll = <'-'> ('L' | 'H')
     pre-fix-modifier = integer operator
     post-fix-modifier = operator integer
     <operator> = '+' | '-' | '/' | 'x'

     integer-above-zero = #'[1-9][0-9]*'
     integer = #'[0-9]+'"))

(def roll-transform-options
  (letfn [(create-modifier [operator value]
            [:modifier {:operator (case operator
                                    "+" '+
                                    "-" '-
                                    "x" '*
                                    "/" '/)
                        :value value}])]
    {:integer-above-zero edn/read-string
     :integer edn/read-string
     :post-fix-modifier create-modifier
     :pre-fix-modifier #(create-modifier %2 %1)
     :drop-roll #(case %
                   "L" [:drop :lowest]
                   "H" [:drop :highest])}))

(defn- to-roll [parse-tree] 
   (if (insta/failure? parse-tree)
     (throw (IllegalArgumentException. "Failed to parse roll."))
     (reduce (fn [roll part]
               (assoc roll (first part) (second part))) {} parse-tree)))

(defn parse [^String input-str]
  (->> (roll-parser input-str)
    (insta/transform roll-transform-options)
    (to-roll)))