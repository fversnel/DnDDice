(ns org.fversnel.dnddice.core-test
  (:require [clojure.test :refer [deftest is]]
            [org.fversnel.dnddice.core :as dnddice]))

(defn test-rand-int-gen [n] n)

(deftest simple-roll
  (is (= {:roll {:sides 20}
          :die-rolls '(20)
          :total 20}
        (dnddice/roll test-rand-int-gen {:sides 20}))))

(deftest complex-roll
  (let [outcome (dnddice/roll test-rand-int-gen
                  {:sides 20
                   :die-count 5
                   :modifier {:operator '* :value 2}})]
    (is (= '(20 20 20 20 20) (:die-rolls outcome))
        (= 200 (:total outcome)))))

(deftest percentile-roll
  (is (= 100 (:total (dnddice/roll test-rand-int-gen "d%")))))
