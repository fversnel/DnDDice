(ns dnddice.core
  (:require [dnddice.parser :as parser]))

(defn modifier [{:keys [modifier]}] (or modifier 0))
(defn die-count [{:keys [die-count]}] (or die-count 1))
(defn sides [{:keys [sides]}] sides)

(defn modifier-str [roll]
  (let [modifier (modifier roll)]
    (let [operator (if (>= modifier 0) "+")]
      (str operator modifier))))

(defn roll-die 
  "Rolls an individual dice."
  [sides] 
  (+ (rand-int sides) 1))

(defn sum-rolls 
  "Sums all individual dice rolls plus the modifier."
  [roll-outcome modifier] 
  (+ (reduce + roll-outcome) modifier))

(defn perform-roll [roll] 
  "Performs a DnD roll. Returns a lazy seq of all die rolls."
  (for [_ (range (die-count roll))]
    (roll-die (sides roll))))

(defn roll [input-str]
  "Parses a string into a DnD roll and rolls it. Returns a map with the roll,
  the outcome of the roll and the sum of the outcome, unless the input-str is
  not a valid DnDRoll in that case an :invalid-input-error is returned."
  (let [parsed-roll (parser/parse-roll input-str)]
    (if (= parsed-roll :invalid-input-error)
      :invalid-input-error
      (let [roll-outcome (perform-roll parsed-roll)
            summed-outcome (sum-rolls roll-outcome (modifier parsed-roll))]
        {:roll parsed-roll
         :outcome roll-outcome
         :sum summed-outcome}))))

(defn pretty-roll-outcome-str
  "Creates a pretty string of the roll's outcome, e.g. '(12 10 7 17 20 (+5)) =
  71'. The 'die-rolls-max' variable determines how many individual die rolls
  are displayed."
  [die-rolls-max roll-outcome]
  (let [{:keys [outcome sum roll]} roll-outcome]
    (str "(" (clojure.string/join " " (take die-rolls-max outcome))
         (if (> (count outcome) die-rolls-max) "...")
         " (" (modifier-str roll) ")" ")"
         " = " sum)))
