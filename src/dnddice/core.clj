(ns dnddice.core
  (:require [dnddice.parser :as parser]))

(defn modifier [{:keys [modifier]}] (or modifier 0))
(defn die-count [{:keys [die-count]}] (or die-count 1))
(defn sides [{:keys [sides]}] sides)

(defn modifier-str [roll]
  (let [modifier (modifier roll)]
    (if (not= modifier 0)
      (let [operator (if (>= modifier 0) "+")]
        (str operator modifier)))))

(defn roll-die 
  "Rolls a die of n sides."
  [sides] 
  (+ (rand-int sides) 1))

(defn sum-rolls 
  "Sums all die rolls and the modifier."
  [roll-outcome modifier] 
  (+ (reduce + roll-outcome) modifier))

(defn perform-roll  
  "Performs a DnD roll. Returns a lazy seq of all die rolls."
  [roll]
  (for [_ (range (die-count roll))]
    (roll-die (sides roll))))

(defn parse-roll 
  "Creates a roll map (e.g. {:die-count 5 :sides 20 :modifier -1}) from a
  Dungeons and Dragons die roll string (e.g. '1d20'). If the input-str is not
  a valid DnD roll an IllegalArgumentException is thrown."
  [input-str] 
  (parser/parse-roll input-str))

(defn do-roll 
  "Performs a Dungeons and Dragons roll. Returns a map with the roll, the
  outcome of the roll and the sum of the outcome."
  [roll]
  (let [roll-outcome (perform-roll roll)
        summed-outcome (sum-rolls roll-outcome (modifier roll))]
    {:roll roll
     :outcome roll-outcome
     :sum summed-outcome}))

(defn pretty-roll-outcome-str
  "Creates a pretty string of the roll's outcome, e.g. '(12 10 7 17 20 (+5)) =
  71'. The 'die-rolls-max' variable determines how many die rolls are
  printed."
  [die-rolls-max roll-outcome]
  (let [{:keys [outcome sum roll]} roll-outcome]
    (let [modifier-str (modifier-str roll)
          rolls-str (clojure.string/join " " (take die-rolls-max outcome))]
      (str "(" rolls-str
           (if (> (count outcome) die-rolls-max) "...")
           (if-not (empty? modifier-str) (str " (" modifier-str ")")) ")"
           " = " sum))))
