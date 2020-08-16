(ns calculator.pedmas-dsl
  (:require [clojure.walk :refer [postwalk]]))
"
Given a nested collection (vectors or lists) mimicking a PDMAS arithmetic
problem, parses the solution. The nesting of the data structures themselves
represent parenthesis. Supported operations (addition, multiplication, division,
subtraction, and negation) are represented as keywords.

(def negation
  {:applicable? (fn [item-1 item-2 item-3]
                  (boolean (and (not (number? item-1))
                                (= :subt item-2)
                                (number? item-3))))
   :apply (fn [item-1 item-2 item-3 coll]
            (concat (drop-last coll) [(* -1 item-3)]))})

(defn num-op-num->num [kw f]
  {:applicable? (fn [item-1 item-2 item-3]
                  (boolean (and (number? item-1)
                                (= kw item-2)
                                (number? item-3))))
   :apply (fn [item-1 item-2 item-3 coll]
            (concat (drop-last 2 coll) [(f item-1 item-3)]))})

(def addition (num-op-num->num :add +))
(def subtraction (num-op-num->num :subt -))
(def division (num-op-num->num :div /))
(def multiplication (num-op-num->num :mult *))

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
