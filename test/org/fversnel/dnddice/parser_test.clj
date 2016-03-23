(ns org.fversnel.dnddice.parser-test
  (:use clojure.test
        org.fversnel.dnddice.parser))

(deftest d20
  (is (= {:sides 20}
         (parse "d20"))))

(deftest d20+5
  (is (= {:sides 20
          :modifier {:operator '+ :value 5}}
         (parse "d20+5"))))

(deftest _5d6
  (is (= {:die-count 5
          :sides 6}
         (parse "5d6"))))

(deftest _5d6x60
  (is (= {:die-count 5
          :sides 6
          :modifier {:operator '* :value 60}}
         (parse "5d6x60"))))

(deftest d20-L
  (is (= {:sides 20
          :drop :lowest}
         (parse "d20-L"))))

(deftest _10d15-5-H
  (is (= {:die-count 10
          :sides 15
          :modifier {:operator '- :value 5}
          :drop :highest}
         (parse "10d15-5-H"))))