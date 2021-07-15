(ns org.fversnel.dnddice.parser-test
  (:require [clojure.test :refer [deftest is]]
            [org.fversnel.dnddice.parser :as parser]))

(deftest d20
  (is (= {:sides 20}
         (parser/parse "d20"))))

(deftest d20+5
  (is (= {:sides 20
          :modifier {:operator '+ :value 5}}
         (parser/parse "d20+5"))))

(deftest _5d6
  (is (= {:die-count 5
          :sides 6}
         (parser/parse "5d6"))))

(deftest _5d6x60
  (is (= {:die-count 5
          :sides 6
          :modifier {:operator '* :value 60}}
         (parser/parse "5d6x60"))))

(deftest d20-L
  (is (= {:sides 20
          :drop :lowest}
         (parser/parse "d20-L"))))

(deftest _10d15-5-H
  (is (= {:die-count 10
          :sides 15
          :modifier {:operator '- :value 5}
          :drop :highest}
         (parser/parse "10d15-5-H"))))

(deftest d%
  (is (= {:sides "%"} (parser/parse "d%"))))