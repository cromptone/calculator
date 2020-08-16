(ns calculator.exceptions)

(defn error [status msg]
  {:status status
   :headers {"Content-Type" "application/json"}
   :body {:message msg
          :error true}})

(defn ex [explanation] (throw (Exception. explanation)))

(defn check-decoded-query [query]
  (let [invalid-chars (clojure.string/replace query #"[0-9\s\*\+\-\/\(\)\.]" "")
        freq (frequencies query)]
    (cond
      (clojure.string/blank? query) (ex "Query value is blank")
      (not-empty invalid-chars) (ex (str "Invalid characters in decoded query: " invalid-chars))
      (not= (get freq \() (get freq \))) (ex "Parenthesis characters don't match in decoded query")
      :else query)))

(defn check-reduced-problem [coll]
  (if (and (= 1 (count coll))
           (number? (first coll)))
    coll
    (ex (str "Equation did not reduce properly. "
             "Operators, numbers, or parens might be in an invalid order"))))
