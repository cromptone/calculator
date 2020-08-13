(defproject calculator "0.1.0"
            :description "Calculator"
            :license {:name "Eclipse Public License v1.0"
                      :url "http://www.eclipse.org/legal/epl-v10.html"}
            :dependencies [[org.clojure/clojure "1.10.1"]
                           [compojure "1.6.2"]
                           [ring/ring-json "0.5.0"]
                           [ring/ring-defaults "0.3.2"]
                           [ring/ring-jetty-adapter "1.7.1"]]
            :min-lein-version "2.0.0"
            :plugins [[environ/environ.lein "0.3.1"]]
            :hooks [environ.leiningen.hooks]
            :uberjar-name "calculator-standalone.jar"
            :main calculator.web
            :profiles {:production {:env {:production true}}})
