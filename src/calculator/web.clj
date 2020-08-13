(ns  calculator.web
  (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.json :refer [wrap-json-response]]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn splash []
  {:status 500
   :headers {"Content-Type" "text/plain"}
   :body "Error!!~~"})

(defroutes handler
           (GET "/calculus" [query]
                {:status 200
                 :headers {"Content-Type" "text/plain"}
                 :body {:result (str query)
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
