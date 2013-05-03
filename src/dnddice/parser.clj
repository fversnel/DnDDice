(ns dnddice.parser
  (:require [instaparse.core :as insta]))

(def ^:private roll-parser
  (insta/parser
    "<roll> = die-count? sides modifier?
     die-count = number
     sides = <'d'> number
     modifier = ('+' | '-') number
     number = #'[0-9]+'"))

(def ^:private roll-transform-options
  {:number read-string
   :modifier (fn [operator modifier]
               (->> (str operator modifier) 
                 (read-string)
                 (vector :modifier)))})

(defn- to-roll 
  ([parse-tree] 
   (if (insta/failure? parse-tree)
     :invalid-input-error 
     (to-roll parse-tree {})))
  ([parse-tree roll]
   (if (empty? parse-tree) 
     roll
     (let [part (first parse-tree)
           part-type (first part)
           part-content (second part)]
       (recur (rest parse-tree)
              (assoc roll part-type part-content))))))

(defn parse-roll 
  "Creates a roll map (e.g. {:die-count 5 :sides 20 :modifier -1}) from an
  input string, unless the input string is not a valid DnD roll in that case
  an :invalid-input-error is returned."
  [input-str]
  (->> (roll-parser input-str)
    (insta/transform roll-transform-options)
    (to-roll)))
