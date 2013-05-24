(ns dnddice.core
  (:require [dnddice.parser :as parser]))

(defn modifier-fn [{:keys [modifier]}] 
  (if modifier 
    (let [operator (case (:operator modifier)
                     "+" +
                     "-" -
                     "*" *
                     "/" /)
          value (:value modifier)]
      #(operator % value))
    identity))
(defn die-count [{:keys [die-count]}] (or die-count 1))
(defn sides [{:keys [sides]}] sides)

(defn modifier-str [{:keys [modifier]}]
  (if modifier (str (:operator modifier) (:value modifier))))

(defn roll-die 
  "Rolls a die of n sides."
  [sides] 
  (+ (rand-int sides) 1))

(defn parse-roll 
  "Creates a roll map (e.g. {:die-count 5 :sides 20 :modifier {:operator '-'
  :value 1}}) from a Dungeons and Dragons die roll string (e.g. '1d20'). If
  the input-str is not a valid DnD roll an IllegalArgumentException is
  thrown."
  [input-str] 
  (parser/parse-roll input-str))

(defn do-roll 
  "Performs a Dungeons and Dragons roll. Returns a map with the roll, the
  outcome of the roll and the sum of the outcome."
  [roll]
  (let [roll-outcome (for [_ (range (die-count roll))]
                       (roll-die (sides roll)))
        summed-outcome ((modifier-fn roll) (reduce + roll-outcome))]
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
