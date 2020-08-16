(ns calculator.utils-test
  (:require [clojure.test :refer [deftest is testing]]
            [calculator.utils :refer [decode-64]]))

(deftest utils
  (testing "Decoder works"
    (testing "with = filler"
      (is (= (decode-64 "MiAqICgyMy8oMyozKSktIDIzICogKDIqMyk=")
             "2 * (23/(3*3))- 23 * (2*3)"))
      (is (= (decode-64 "OTAwICsgMSsyKzEqKDMvNCo2KS0gNDAwMDA=")
             "900 + 1+2+1*(3/4*6)- 40000")))
    (testing "without = filler"
      (is (= (decode-64 "MiAqICgyMy8oMyozKSktIDIzICogKDIqMyk")
             "2 * (23/(3*3))- 23 * (2*3)"))))
  (testing "Encoder works"
    (is (= (encode-64 "2 * (23/(3*3))- 23 * (2*3)")
           "MiAqICgyMy8oMyozKSktIDIzICogKDIqMyk="))
    (is (= (encode-64 "900 + 1+2+1*(3/4*6)- 40000")
           "OTAwICsgMSsyKzEqKDMvNCo2KS0gNDAwMDA="))))

