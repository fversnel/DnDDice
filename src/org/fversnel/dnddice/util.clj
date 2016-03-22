(ns org.fversnel.dnddice.util)

(defn remove-first-occurrence
  "remove elem in coll"
  [coll element]
  (let [[n m] (split-with (partial not= element) coll)]
    (concat n (rest m))))
