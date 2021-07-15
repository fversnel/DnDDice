(ns org.fversnel.dnddice.core
  (:require [org.fversnel.dnddice.parser :as parser]
            [org.fversnel.dnddice.util :as util]
            [clojure.string]))

#?(:clj
   (defn java-random-int-gen
     [^java.util.Random random max]
     (inc (mod (.nextInt random) max))))

#?(:clj
   (defn secure-random-int-gen
     [max]
     (java-random-int-gen (java.security.SecureRandom.) max)))

(defn default-rand-int-gen
  [max]
  (inc (rand-int max)))

(defn parse
  "Creates a roll map (e.g. {:die-count 5 :sides 20 :modifier {:operator '-
   :value 1}}) from a Dungeons and Dragons dice expression (e.g. '5d20-1'). If
   the input-str is not a valid DnD roll an instaparse failure object is returned."
  [input-str]
  (parser/parse input-str))

(def parse-failure? parser/failure?)

(defn modifier-to-str
  [{:keys [operator value]}]
  (str
   (cond
     (= operator '*) "x"
     :else (str operator))
   value))

(defn dice-expression
  "Creates a dice expression from a roll map"
  [{:keys [die-count sides modifier drop]}]
  (str die-count
       "d" sides
       (modifier-to-str modifier)
       (case drop
         :highest "-H"
         :lowest "-L"
         "")))

(defn die-count
  [{:keys [die-count]}]
  (or die-count 1))

(defn sides
  [{:keys [sides]}]
  (cond
    (integer? sides) sides
    (= sides "%") 100))

(defn create-modifier-fn [{:keys [modifier]}]
  (if modifier
    (partial (resolve (:operator modifier)) (:value modifier))
    identity))

(defn roll
  "Performs a Dungeons and Dragons roll. Returns a map with the roll, the
  outcome of the roll and the sum of the outcome.

  'r' can be either a parsed dice-expression or an unparsed dice-expression.

  Optionally takes a random integer generator as argument to use for rolling.
  This should be a function that takes one argument which is the sides of the
  die to be rolled. The generator should return a number between 1 (inclusive) and
  the total number of sides on the die (inclusive).
  If it isn't supplied Clojure's rand-int function is used."
  ([rand-int-gen r]
   (let [roll-map (if (string? r) (parse r) r)]
     (if-not (parse-failure? roll-map)
       (let [roll-die (partial rand-int-gen (sides roll-map))
             die-rolls (repeatedly (die-count roll-map) roll-die)
             die-rolls (case (:drop roll-map)
                         :highest (util/remove-first (comp reverse sort) die-rolls)
                         :lowest (util/remove-first sort die-rolls)
                         die-rolls)
             apply-modifier (create-modifier-fn roll-map)]
         {:roll roll-map
          :die-rolls die-rolls
          :total (apply-modifier (reduce + die-rolls))})
       roll-map)))
  ([r]
   (roll default-rand-int-gen r)))

(defn die-rolls-to-str
  "Creates a pretty string of the roll's outcome, e.g. '(12 10 7 17 20 (+5)) =
  71'. The 'max-die-rolls' variable determines how many die rolls are
  printed."
  [max-die-rolls {:keys [die-rolls total roll]}]
  (let [modifier-str (modifier-to-str (:modifier roll))
        rolls-str (clojure.string/join " " (take max-die-rolls die-rolls))]
    (str "(" rolls-str
         (when (> (count die-rolls) max-die-rolls) "...")
         (if-not (empty? modifier-str) (str " (" modifier-str ")")) ")"
         " = " total)))
