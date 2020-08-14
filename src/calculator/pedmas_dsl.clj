(ns calculator.pedmas-dsl)
"
Given a nested collection (vectors or lists) mimicking a PDMAS arithmetic
problem, parses the solution. The nesting of the data structures themselves represent
the the parenthesis. Supported operations (addition, multiplication, etc) are
represented as keywords.
"

(defn num-op-num->num [kw f]
  {:applicable? (fn [item-1 item-2 item-3]
                  (boolean (and (number? item-1)
                                (= kw item-2)
                                (number? item-3))))
   :reducer (fn [item-1 item-2 item-3 coll]
              (concat (drop-last 2 coll) [(f item-1 item-3)]))})

(def addition (num-op-num->num :add +))
(def subtraction (num-op-num->num :subt -))
(def division (num-op-num->num :div /))
(def multiplication (num-op-num->num :div *))

; My goal here is to produce a reducer that goes over previous 3 in collection
;TODO make sure to use vec for appending
(defn reduce-by-threes [operator result input]
  (let [coll (if (seq? result) result (vector result))
        item-1 (last (butlast coll))
        item-2 (last coll)
        item-3 input]
    (if ((:applicable? operator) item-1 item-2 item-3)
      ((:reducer operator) item-1 item-2 item-3 coll)
      (concat coll [input]))))

(defn reduce-unnested [subproblem]
  (reduce (partial reduce-by-threes addition) subproblem))
