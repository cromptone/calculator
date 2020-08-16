(ns calculator.web-test
  (:require [clojure.test :refer [deftest is testing]]
            [calculator.web :refer [app]]
            [cheshire.core :as cheshire]
            [ring.mock.request :as mock]
            [calculator.utils :refer [encode-64 decode-64]]
            [clojure.math.numeric-tower :as math]))
(defn GET-test [{:keys [equation result testing-str]}]
  (testing testing-str
    (let [endpoint (str "/calculus?query=" (encode-64 equation))
          response (app (mock/request :get  endpoint))
          body (-> response :body parse-json)
          content-type (get (:headers response) "Content-Type")]
      (is (= (:status response) 200))
      (is (= content-type "application/json"))
      (is (approx= (:result body) result)))))

(deftest equations-test
  (let [response (app (mock/request :get  "/calculus?query=KDQgKyA0IC0gMjAwIC8gMik="))
        body (-> response :body parse-json)
        content-type (get (:headers response) "Content-Type")]
    (is (= (:status response) 200))
    (is (= content-type "application/json"))
    (is (= (:result body) -92))))
