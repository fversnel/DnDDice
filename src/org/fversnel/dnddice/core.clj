(ns org.fversnel.dnddice.core
  (:require [org.fversnel.dnddice.parser :as parser])
  (:import (java.util Random)
           (java.security SecureRandom)))

(defn apply-modifier [{:keys [modifier]} roll-outcome]
  (if modifier
    (let [operator (case (:operator modifier)
                     "+" +
                     "-" -
                     "x" *
                     "/" /)]
      (operator roll-outcome (:value modifier)))
    roll-outcome))

(defn die-count [{:keys [die-count]}] (or die-count 1))

(defn parse-roll
  "Creates a roll map (e.g. {:die-count 5 :sides 20 :modifier {:operator '-'
   :value 1}}) from a Dungeons and Dragons die roll string (e.g. '1d20'). If
   the input-str is not a valid DnD roll an IllegalArgumentException is
   thrown."
  [^String input-str]
  (parser/parse-roll input-str))

(defn java-random-int-gen [^Random random max]
  (-> random .nextInt (mod max) inc))

(def secure-random-int-gen (partial java-random-int-gen (SecureRandom.)))

(defn perform-roll
  "Performs a Dungeons and Dragons roll. Returns a map with the roll, the
  outcome of the roll and the sum of the outcome.
  
  Optionally takes a random integer generator as argument to use for rolling.
  This should be a function that takes one argument which is the sides of the
  die to be rolled. The generator should return a number between 1 (inclusive) and
  the total number of sides on the die (inclusive).
  If it isn't supplied java.security.SecureRandom is used."
  ([rand-int-gen roll]
   (let [roll-die (partial rand-int-gen (:sides roll))
         roll-outcome (repeatedly (die-count roll) roll-die)]
     {:roll roll
      :outcome roll-outcome
      :total (apply-modifier roll (reduce + roll-outcome))}))
  ([roll] (perform-roll secure-random-int-gen roll)))

(defn roll
  "Rolls Dungeons and Dragons dice"
  ([rand-int-gen ^String input-str]
   (->> input-str parse-roll (perform-roll rand-int-gen)))
  ([^String input-str]
   (roll secure-random-int-gen input-str)))

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
