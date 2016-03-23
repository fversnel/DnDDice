(ns org.fversnel.dnddice.core
  (:require [org.fversnel.dnddice.parser :as parser]
            [org.fversnel.dnddice.util :as util])
  (:import (java.util Random)
           (java.security SecureRandom)))

(defn parse
  "Creates a roll map (e.g. {:die-count 5 :sides 20 :modifier {:operator '-'
   :value 1}}) from a Dungeons and Dragons die roll string (e.g. '5d20-1'). If
   the input-str is not a valid DnD roll an IllegalArgumentException is
   thrown."
  [^String input-str]
  (parser/parse input-str))

(declare modifier-to-str)

(defn dice-expression
  "Creates a dice expression from a roll map"
  [roll]
  (let [die-count (:die-count roll)]
    (str (when die-count die-count)
         "d" (:sides roll)
         (modifier-to-str (:modifier roll))
         (case (:drop roll)
           :highest "-H"
           :lowest "-L"
           ""))))

(defn java-random-int-gen [^Random random max]
  (-> random .nextInt (mod max) inc))
(def secure-random-int-gen (partial java-random-int-gen (SecureRandom.)))

(defn die-count [{:keys [die-count]}] (or die-count 1))

(defn create-modifier-fn [{:keys [modifier]}]
  (if modifier
    (partial (eval (symbol (:operator modifier))) (:value modifier))
    identity))

(defn modifier-to-str [modifier]
  (when modifier
    (str (case (:operator modifier)
           '+ "+"
           '- "-"
           '* "x"
           '/ "/")
         (:value modifier))))

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
         die-rolls (repeatedly (die-count roll) roll-die)
         die-rolls (case (:drop roll)
                     :highest (util/remove-first (comp reverse sort) die-rolls)
                     :lowest (util/remove-first sort die-rolls)
                     die-rolls)
         apply-modifier (create-modifier-fn roll)]
     {:roll roll
      :die-rolls die-rolls
      :total (apply-modifier (reduce + die-rolls))}))
  ([roll] (perform-roll secure-random-int-gen roll)))

(defn roll
  "Rolls Dungeons and Dragons dice"
  ([rand-int-gen ^String input-str]
   (->> input-str parse (perform-roll rand-int-gen)))
  ([^String input-str]
   (roll secure-random-int-gen input-str)))

(defn die-rolls-to-str
  "Creates a pretty string of the roll's outcome, e.g. '(12 10 7 17 20 (+5)) =
  71'. The 'max-die-rolls' variable determines how many die rolls are
  printed."
  [max-die-rolls {:keys [die-rolls total roll]}]
  (let [modifier-str (modifier-to-str (:modifier roll))
        rolls-str (clojure.string/join " " (take max-die-rolls die-rolls))]
    (str "(" rolls-str
         (if (> (count die-rolls) max-die-rolls) "...")
         (if-not (empty? modifier-str) (str " (" modifier-str ")")) ")"
         " = " total)))
