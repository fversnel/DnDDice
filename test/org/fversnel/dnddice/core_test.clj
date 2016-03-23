(ns org.fversnel.dnddice.core-test
  (:use clojure.test
        org.fversnel.dnddice.core))

(defn test-rand-int-gen [n] n)

(deftest simple-roll
  (is (= {:roll {:sides 20}
          :die-rolls '(20)
          :total 20}
        (roll test-rand-int-gen {:sides 20}))))

(deftest complex-roll
  (let [outcome (roll test-rand-int-gen
                  {:sides 20
                   :die-count 5
                   :modifier {:operator '* :value 2}})]
    (is (= '(20 20 20 20 20) (:die-rolls outcome))
        (= 200 (:total outcome)))))
