(ns calculator.pedmas-dsl)
"
Given a nested collection (vectors or lists) mimicking a PDMAS arithmetic
problem, parses the solution. The nesting of the data structures themselves represent
the the parenthesis. Supported operations (addition, multiplication, etc) are
represented as keywords.
"

; My goal here is to produce a reducer that goes over previous 3 in collection
;TODO make sure to use vec for appending
(defn add-reducer [result input]
  (let [coll (if (seq? result) result (vector result))
        item-1 (last (butlast coll))
        item-2 (last coll)
        item-3 input]
    (if (and (number? item-1)
             (= :add item-2)
             (number? item-3))
      (concat (drop-last 2 coll) [(+ item-1 item-3)])
      (concat coll [input]))))

(defn reduce-unnested [subproblem]
  (reduce add-reducer subproblem))
