(ns calculator.utils-test
  (:require [calculator.utils :refer [encode-64 decode-64]]
            [clojure.test :refer [deftest is are testing]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]))

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

(defspec encode-decode 50
         (prop/for-all [string gen/string]
                       (= string
                          (-> string encode-64 decode-64)
                          (-> string encode-64 decode-64 encode-64 decode-64))))
