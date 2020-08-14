(ns calculator.web
  (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.json :refer [wrap-json-response]]
            [compojure.route :as route]
            [clojure.edn :as edn]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [calculator.utils :as utils]))

(def ops {"/" :div
          "*" :mult
          "+" :add
          "-" :subt})

(defn clean-query [encoded-query]
  "TODO: error handling"
  (-> encoded-query
      (utils/decode-64)
      (#(str "[" % "]"))
      (clojure.string/replace #"[*/+-]" #(str " " (get ops %) " "))
      (clojure.string/replace #"\(|\)" {"(" "[" ")" "]"})
      edn/read-string))

(defn go [] (clean-query "MiAqICgyMy8oMyozKSktIDIzICogKDIqMyk"))

(defroutes handler
           (GET "/calculus" [query]
                {:status 200
                 :headers {"Content-Type" "text/plain"}
                 :body {:result (clean-query query)
                        :error false}})
           (ANY "*" [] {:status 500
                        :headers {"Content-Type" "text/plain"}
                        :body {:message "Invalid query format"
                               :error true}}))

(defn -main [& [port]]
  (let [port (Integer. (or port 3000))]
    (jetty/run-jetty (-> handler
                         wrap-json-response
                         (wrap-defaults site-defaults))
                     {:port port :join? false})))
