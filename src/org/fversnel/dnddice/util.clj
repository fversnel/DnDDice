(ns org.fversnel.dnddice.util)

(defn remove-first-occurrence
  "removes the first occurrence of the given element in coll"
  [coll element]
  (let [[n m] (split-with (partial not= element) coll)]
    (concat n (rest m))))

(defn remove-first [projection coll]
  (let [first-occurrence (first (projection coll))]
    (remove-first-occurrence coll first-occurrence)))
