(ns calculator.web-test
  (:require [clojure.test :refer [deftest is testing]]
            [calculator.web :refer [app]]
            [cheshire.core :as cheshire]
            [ring.mock.request :as mock]))

(deftest failing-test
  (is false true))
(deftest equations-test
  (let [response (app (mock/request :get  "/calculus?query=KDQgKyA0IC0gMjAwIC8gMik="))
        body (-> response :body parse-json)
        content-type (get (:headers response) "Content-Type")]
    (is (= (:status response) 200))
    (is (= content-type "application/json"))
    (is (= (:result body) -92))))
