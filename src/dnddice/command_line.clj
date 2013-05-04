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
    (let [input-str (-> (read-line) 
                      (clojure.string/trim)
                      (clojure.string/lower-case))]
      (when (= input-str "exit") (System/exit 0))
      (when-not (empty? input-str)
        (let [roll-outcome (core/roll input-str)]
          (if (= roll-outcome :invalid-input-error)
            (println "Invalid input, try again.")
            (println (core/pretty-roll-outcome-str render-die-rolls-max roll-outcome))))
        (flush)))))
