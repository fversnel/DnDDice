(ns org.fversnel.dnddice.commandline
  (:require [org.fversnel.dnddice.core :as core]
            [clojure.string])
  (:gen-class))

(def ^:const opening (str "Examples: 1d20 2d6+1 5d6-1\n"
                          "type 'exit' or 'quit' to stop."))
(def ^:const prompt "DnDDice> ")
(def ^:const max-renderable-die-rolls 20)

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
          (let [dice-roll (core/roll input-str)]
            (println (core/die-rolls-to-str max-renderable-die-rolls
                                            dice-roll)))
          (catch IllegalArgumentException _
            (println "Invalid input, try again.")))
        (flush)))))