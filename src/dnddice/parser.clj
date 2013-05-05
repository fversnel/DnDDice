(ns dnddice.parser
  (:require [instaparse.core :as insta]
            [clojure.edn :as clojure.edn]))

(def roll-parser
  (insta/parser
    "<roll> = (post-fix-roll | pre-fix-roll)
     <post-fix-roll> = dice post-fix-modifier?
     <pre-fix-roll> = pre-fix-modifier? dice
     <dice> = die-count? sides

     die-count = integer
     sides = <('d'|'D')> non-zero-integer
     pre-fix-modifier = integer operator
     post-fix-modifier = operator integer
     operator = '+' | '-' | '/' | '*'

     non-zero-integer = non-zero-digit integer?
     <non-zero-digit> = #'[1-9]'
     integer = digit+
     <digit> = #'[0-9]'"))

(def roll-transform-options
  (letfn [(create-modifier [operator value]
            (vector :modifier {:operator operator :value value}))]
    {:non-zero-integer (comp clojure.edn/read-string str)
     :integer clojure.edn/read-string
     :operator identity
     :post-fix-modifier #(create-modifier %1 %2)
     :pre-fix-modifier #(create-modifier %2 %1)}))

(defn- to-roll ([parse-tree] 
   (if (insta/failure? parse-tree)
     (throw (IllegalArgumentException. "Failed to parse roll."))
     (to-roll parse-tree {})))
  ([parse-tree roll]
   (if (empty? parse-tree) 
     roll
     (let [part (first parse-tree)
           part-type (first part)
           part-content (second part)]
       (recur (rest parse-tree)
              (assoc roll part-type part-content))))))

(defn parse-roll [input-str]
  (->> (roll-parser input-str)
    (insta/transform roll-transform-options)
    (to-roll)))
