(ns dnddice.command-line
  (:require [dnddice.core :as core])
  (:gen-class))

(def opening (str "Examples: 1d20 2d6+1 5d6-1\n"
                  "type 'exit' to stop."))
(def prompt "DnDDice> ")

(def render-die-rolls-max 20)

(defn -main
  [& args]
  (println opening)
  (while true
    (print prompt)
    (flush)
    (let [input-str (-> (read-line) (clojure.string/trim))]
      (when (#{"exit" "quit"} (clojure.string/lower-case input-str)) (System/exit 0))
      (when-not (empty? input-str)
        (try
          (let [parsed-roll (core/parse-roll input-str)
                roll-outcome (core/perform-roll parsed-roll)]
            (println (core/pretty-roll-outcome-str render-die-rolls-max
                                                   roll-outcome)))
          (catch IllegalArgumentException _
            (println "Invalid input, try again.")))
        (flush)))))
