(ns calculator.web-test
  (:require [clojure.test :refer [deftest is testing]]
            [calculator.web :refer [app]]
            [cheshire.core :as cheshire]
            [ring.mock.request :as mock]
            [calculator.utils :refer [encode-64 decode-64]]
            [clojure.math.numeric-tower :as math]))

(defn approx= [a b]
  (let [epsilon 0.000001]
    (> epsilon (math/abs (- a b)) (* -1 epsilon))))

(defn parse-json [body]
  (cheshire/parse-string body true))

(def sample-query-data
  [{:equation "- (-2 - -2) * 10 / 2 " :result 0 :testing-str "with negation"}
   {:equation ".5 + .5 + .5" :result 1.5 :testing-str "with a decimal without zero in front"}
   {:equation ".5+.5+.5+(.43-(1.2+1*.34))" :result 0.39 :testing-str "with a decimal without zero in front"}
   {:equation "2 / 4 + -3 * 4 - -10.2" :result -1.3 :testing-str "with order of operations"}
   {:equation "2 + (2) + (2 + 2 + (2 + 100 * 200 + 20 / (400/100 + 1)))" :result 20014 :testing-str "with parens"}
   {:equation "((1/999.0)*(1/999.0)*(1/999.0)*(1/999.0)*(1/999.0)*(1/999.0)*(1/999.0))*100000000000000000000000"
    :result 100.702808421 :testing-str "with long decimals"}
   {:equation "3/9" :result (/ 3 9) :testing-str "with repeating rationals"}
   {:equation "-       2 + 3 --4 *  8 -(4)  " :result 29 :testing-str "with odd whitespace"}
   {:equation "-3" :result -3 :testing-str "with single negative number"}
   {:equation "1" :result 1 :testing-str "with single number"}
   {:equation "[2 +(3 +[4 *[7+8]+[9+1.1]*4])*4 /3 ]" :result 139.86666667 :testing-str "with [] and ()"}
   {:equation "[3 + [2 + (1 +3)+3]]" :result 12 :testing-str "with [] and ()"}
   {:equation "(((1 + 2 - (2 + 3))))" :result -2 :testing-str "with multiple outer parens"}])

(def sample-bad-data
  [{:equation "- (-2 * -2)* 10 / 0 " :testing-str "with zero division"}
   {:equation "- (-2 * -2)* * 10 / 2 " :testing-str "with bad syntax"}
   {:equation "- (-2 * -2)* a 10 / 2 " :testing-str "with invalid letter"}
   {:equation "- (-2 * -2)* 10 / 2 3" :testing-str "with missing operator"}
   {:equation "- (-2 * -2)* [10/2]+[3" :testing-str "with invalid brackets"}
   {:equation "- (-2 * -2)+ (* 10 / 2 3" :testing-str "with invalid parens"}])

(defn GET-test [{:keys [equation result testing-str]}]
  (testing testing-str
    (let [endpoint (str "/calculus?query=" (encode-64 equation))
          response (app (mock/request :get  endpoint))
          body (-> response :body parse-json)
          content-type (get (:headers response) "Content-Type")]
      (is (= (:status response) 200))
      (is (= content-type "application/json"))
      (is (false? (:error body)))
      (is (approx= (:result body) result)))))

(defn GET-test-fail [{:keys [equation testing-str]}]
  (testing testing-str
    (let [endpoint (str "/calculus?query=" (encode-64 equation))
          response (app (mock/request :get  endpoint))
          body (-> response :body parse-json)
          content-type (get (:headers response) "Content-Type")]
      (prn (:message body))
      (is (= (:status response) 500))
      (is (true? (:error body)))
      (is (= content-type "application/json")))))

(deftest equations-test
  (testing "GET request works"
    (apply #(testing %&) (map GET-test sample-query-data))
    (testing "with hard-coded queries"
      (let [response (app (mock/request :get  "/calculus?query=KDQgKyA0IC0gMjAwIC8gMik="))
            body (-> response :body parse-json)
            content-type (get (:headers response) "Content-Type")]
        (is (= (:status response) 200))
        (is (= content-type "application/json"))
        (is (approx= (:result body) -92))))))

(deftest error-test
  (testing "GET request returns 500"
    (apply #(testing %&) (map GET-test-fail sample-bad-data)))
  (testing "GET request returns 400 with bad queries"
    (let [response (app (mock/request :get  "/bad-format"))
          body (-> response :body parse-json)
          content-type (get (:headers response) "Content-Type")]
      (is (= (:status response) 400))
      (is (= content-type "application/json"))
      (is (= (:error body) true)))))
