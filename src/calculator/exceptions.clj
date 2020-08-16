(ns calculator.exceptions)

(defn ex [explanation]
  (throw (Exception. explanation)))

(defn error-500 [e]
  {:status 500
   :headers {"Content-Type" "application/json"}
   :body {:message (.getMessage e)
          :error true}})

(defn check-decoded-query [query]
  (let [invalid-chars (clojure.string/replace query #"[0-9\s\*\+\-\/\(\)\[\]\.]" "")
        freq (frequencies query)]
    (cond
      (not-empty invalid-chars) (ex (str "Invalid characters in decoded query: " invalid-chars))
      (not= (get freq \[) (get freq \])) (ex "Bracket characters don't match in decoded query")
      (not= (get freq \() (get freq \))) (ex "Parenthesis characters don't match in decoded query")
      :else query)))

(defn check-reduced-problem [coll]
  (if (and (= 1 (count coll))
           (number? (first coll)))
    coll
    (ex "Equation did not reduce properly. Operators, numbers, or parens might be in an invalid order")))
