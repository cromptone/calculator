(ns calculator.web
  (:require [compojure.core :refer [defroutes GET ANY]]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.json :refer [wrap-json-response]]
            [compojure.route :as route]
            [clojure.edn :as edn]
            [environ.core :refer [env]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [calculator.utils :as utils]
            [calculator.pedmas-dsl :refer [reduce-nested]]
            [calculator.exceptions :refer [error-500 check-decoded-query]])
  (:gen-class))

(def ops {"/" :div
          "*" :mult
          "+" :add
          "-" :subt})

(defn clean-query [encoded-query]
  "TODO: error handling"
  (-> encoded-query
      (utils/decode-64)
      (check-decoded-query)
      (#(str "[" % "]"))
      (clojure.string/replace #"[*/+-]" #(str " " (get ops %) " "))
      (clojure.string/replace #"\(|\)" {"(" "[" ")" "]"})
      (clojure.string/replace #"(?<![0-9])\." "0.") ;replace e.g. .5 with 0.5
      edn/read-string))

(defroutes handler
           (GET "/calculus" [query]
                (try
                  {:status 200
                   :headers {"Content-Type" "application/json"}
                   :body {:result (-> query clean-query reduce-nested)
                          :error false}}
                  (catch Exception e (error-500 e))))
           (ANY "*" [] {:status 400
                        :headers {"Content-Type" "application/json"}
                        :body {:message "Invalid query format. Check endpoint format"
                               :error true}}))

(def app
  (-> handler wrap-json-response (wrap-defaults site-defaults)))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 3000))]
    (jetty/run-jetty app {:port port :join? false})))
