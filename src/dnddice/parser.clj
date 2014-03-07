(ns dnddice.parser
  (:require clojure.edn
            [instaparse.core :as insta]))

(def roll-parser
  (insta/parser
    "<roll> = (post-fix-roll | pre-fix-roll)
     <post-fix-roll> = dice post-fix-modifier?
     <pre-fix-roll> = pre-fix-modifier? dice
     <dice> = die-count? sides

     die-count = integer
     sides = <('d'|'D')> postive-integer
     pre-fix-modifier = integer operator
     post-fix-modifier = operator integer
     <operator> = '+' | '-' | '/' | '*'

     postive-integer = #'[1-9]' integer?
     integer = #'[0-9]'+"))

(def roll-transform-options
  (letfn [(create-modifier [operator value]
            [:modifier {:operator operator :value value}])]
    {:postive-integer (comp clojure.edn/read-string str)
     :integer (comp clojure.edn/read-string str)
     :post-fix-modifier create-modifier
     :pre-fix-modifier #(create-modifier %2 %1)}))

(defn- to-roll [parse-tree] 
   (if (insta/failure? parse-tree)
     (throw (IllegalArgumentException. "Failed to parse roll."))
     (reduce (fn [roll part]
               (assoc roll (first part) (second part))) {} parse-tree)))

(defn parse-roll [input-str]
  (->> (roll-parser input-str)
    (insta/transform roll-transform-options)
    (to-roll)))
