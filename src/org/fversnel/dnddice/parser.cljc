(ns org.fversnel.dnddice.parser
  (:require #?(:clj  [instaparse.core :as insta :refer [defparser]]
               :cljs [instaparse.core :as insta :refer-macros [defparser]])))

(defn- parse-int [s]
  #?(:clj (Integer/parseInt s)
     :cljs (js/parseInt s)))

(defparser roll-parser
  "<roll> = (post-fix-roll | pre-fix-roll)
   <post-fix-roll> = dice post-fix-modifier? drop-roll?
   <pre-fix-roll> = pre-fix-modifier? dice drop-roll?
   <dice> = die-count? sides

   die-count = integer-above-zero
   sides = <('d'|'D')> (integer-above-zero | '%')
   drop-roll = <'-'> ('L' | 'H')
   pre-fix-modifier = integer operator
   post-fix-modifier = operator integer
   operator = '+' | '-' | '/' | 'x'

   integer-above-zero = #'[1-9][0-9]*'
   integer = #'[0-9]+'")

(def roll-transform-options
  (let [create-modifier (fn [operator value]
                          [:modifier {:operator operator
                                      :value value}])]
    {:integer-above-zero parse-int
     :integer parse-int
     :operator #(case %
                  "+" '+
                  "-" '-
                  "x" '*
                  "/" '/)
     :post-fix-modifier create-modifier
     :pre-fix-modifier #(create-modifier %2 %1)
     :drop-roll #(case %
                   "L" [:drop :lowest]
                   "H" [:drop :highest])}))

(def failure? insta/failure?)

(defn- to-roll
  [parse-tree]
  (if-not (failure? parse-tree)
    (into {} parse-tree)
    parse-tree))

(defn parse
  [input-str]
  (->> input-str
       (roll-parser)
       (insta/transform roll-transform-options)
       (to-roll)))
