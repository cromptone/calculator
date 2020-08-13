(ns calculator.web-test
  (:require [clojure.test :refer :all]
            [calculator.web :refer :all]))

(deftest failing-test
  (is false true))
