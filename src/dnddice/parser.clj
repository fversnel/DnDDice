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

(defn- to-roll ([parse-tree] 
   (if (insta/failure? parse-tree)
     (throw (IllegalArgumentException. (str "Failed to parse roll: "
                                            parse-tree)))
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
