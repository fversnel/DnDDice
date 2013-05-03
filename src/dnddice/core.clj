(ns dnddice.core
  (:require [dnddice.parser :as parser]))

(defn modifier [{:keys [modifier]}] (or modifier 0))
(defn die-count [{:keys [die-count]}] (or die-count 1))
(defn sides [{:keys [sides]}] sides)

(defn modifier-str [roll]
  (let [modifier (modifier roll)]
    (let [operator (if (>= modifier 0) "+")]
      (str operator modifier))))

(defn roll-die [sides] (+ (rand-int sides) 1))

(defn sum-rolls [roll-outcome modifier] 
  (+ (reduce + roll-outcome) modifier))

(defn perform-roll [roll] 
  (for [_ (range (die-count roll))]
    (roll-die (sides roll))))

(defn roll [input-str]
  (let [parsed-roll (parser/parse-roll input-str)]
    (if (= parsed-roll :invalid-input-error)
      :invalid-input-error
      (let [roll-outcome (perform-roll parsed-roll)
            summed-outcome (sum-rolls roll-outcome (modifier parsed-roll))]
        {:roll parsed-roll
         :outcome roll-outcome
         :sum summed-outcome}))))
