(ns dnddice.core
  (:require [dnddice.parser :as parser]))

(defn apply-modifier [{:keys [modifier]} roll-outcome]
  (if modifier
    (let [operator (case (:operator modifier)
                     "+" +
                     "-" -
                     "*" *
                     "/" /)]
      (operator roll-outcome (:value modifier)))
    roll-outcome))

(defn die-count [{:keys [die-count]}] (or die-count 1))

(defn parse-roll
  "Creates a roll map (e.g. {:die-count 5 :sides 20 :modifier {:operator '-'
  :value 1}}) from a Dungeons and Dragons die roll string (e.g. '1d20'). If
  the input-str is not a valid DnD roll an IllegalArgumentException is
  thrown."
  [input-str]
  (parser/parse-roll input-str))

(defn secure-random-int-gen 
  [max]
  (mod (.nextInt (java.security.SecureRandom.)) max))

(defn perform-roll
  "Performs a Dungeons and Dragons roll. Returns a map with the roll, the
  outcome of the roll and the sum of the outcome.
  
  Optionally takes a random integer generator as argument to use for rolling.
  This should be a function that takes one argument which is the maximum
  number to be generated (exclusive). If it isn't supplied
  java.security.SecureRandom is used."
  ([rand-int-gen roll]
   (let [roll-die #(inc (rand-int-gen (:sides roll)))
         roll-outcome (repeatedly (die-count roll) roll-die)]
     {:roll roll
      :outcome roll-outcome
      :total (apply-modifier roll (reduce + roll-outcome))}))
  ([roll] (perform-roll secure-random-int-gen roll)))

(defn modifier-str [{:keys [modifier]}]
  (if modifier (str (:operator modifier) (:value modifier))))

(defn pretty-roll-outcome-str
  "Creates a pretty string of the roll's outcome, e.g. '(12 10 7 17 20 (+5)) =
  71'. The 'die-rolls-max' variable determines how many die rolls are
  printed."
  [die-rolls-max {:keys [outcome total roll]}]
  (let [modifier-str (modifier-str roll)
        rolls-str (clojure.string/join " " (take die-rolls-max outcome))]
    (str "(" rolls-str
         (if (> (count outcome) die-rolls-max) "...")
         (if-not (empty? modifier-str) (str " (" modifier-str ")")) ")"
         " = " total)))
