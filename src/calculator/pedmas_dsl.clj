(ns calculator.pedmas-dsl
  (:require [clojure.walk :refer [postwalk]]))
"
Given a nested collection (vectors or lists) mimicking a PDMAS arithmetic
problem, parses the solution. The nesting of the data structures themselves
represent parenthesis. Supported operations (addition, multiplication, division,
subtraction, and negation) are represented as keywords.

Note that :subt represents both negation and subtraction.

Sample input: [:subt 2 :add 3 [3 :div 4 :add 4 :subt 20.2] add 8 :subt :subt 2)]
"
(defn precision [f]
  #(with-precision 100 (apply f %&)))

(defn arithmetic [operator-kw f]
  "Constructs reduction helper where a [num op num] sub-coll reduces to a num"
  {:applicable? (fn [item-1 item-2 item-3]
                  (boolean (and (number? item-1)
                                (= operator-kw item-2)
                                (number? item-3))))
   :apply (fn [item-1 _ item-3 coll]
            (concat (drop-last 2 coll) [((precision f) item-1 item-3)]))})

(def addition (arithmetic :add +))
(def subtraction (arithmetic :subt -))
(def division (arithmetic :div /))
(def multiplication (arithmetic :mult *))
(def negation {:applicable? (fn [item-1 item-2 item-3]
                              (boolean (and (not (number? item-1))
                                            (= :subt item-2)
                                            (number? item-3))))
               :apply (fn [_ _ item-3 coll]
                        (concat (drop-last coll) [(* -1 item-3)]))})

;TODO make sure to use vec for appending
(defn reduce-by-threes [operators result input]
  (let [coll (if (seq? result) result (vector result))
        item-1 (last (butlast coll))
        item-2 (last coll)
        item-3 input]
    (if-let [operator (first (filter #((:applicable? %) item-1 item-2 item-3) operators))]
      ((:apply operator) item-1 item-2 item-3 coll)
      (concat coll [input]))))

(defn reduce' [f coll]
  "When a coll has a single item, clojure.core/reduce short-circuits and returns
   the item. For our threaded implemenation, we want the collection returned"
  (if (-> coll count (= 1))
    coll
    (reduce f coll)))

(defn reduce-unnested [subproblem]
  (->> subproblem
       (reduce' (partial reduce-by-threes [negation]))
       (reduce' (partial reduce-by-threes [multiplication division]))
       (reduce' (partial reduce-by-threes [addition subtraction]))
       first))

(defn evaluate [node]
  (if (coll? node)
    (reduce-unnested node)
    node))

(defn reduce-nested [problem]
  (postwalk evaluate problem))
