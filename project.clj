(defproject calculator "0.1.0"
            :description "Calculator"
            :license {:name "Eclipse Public License v1.0"
                      :url "http://www.eclipse.org/legal/epl-v10.html"}
            :dependencies [[org.clojure/clojure "1.10.1"]
                           [compojure "1.6.0"]
                           [ring/ring-json "0.5.0"]
                           [instaparse "1.4.10"]
                           [environ "1.1.0"]
                           [nrepl "0.8.0"]
                           [clojure-complete "0.2.4"]
                           [ring/ring-defaults "0.3.2"]
                           [ring/ring-mock "0.4.0"]
                           [cheshire "5.10.0"]
                           [ring/ring-jetty-adapter "1.7.1"]
                           [org.clojure/math.numeric-tower "0.0.4"]
                           [org.clojure/test.check "1.1.0"]]
            :min-lein-version "2.0.0"
            :uberjar-name "calculator-standalone.jar"
            :main calculator.web
            :aot [calculator.web]
            :plugins [[environ/environ.lein "0.3.1"]]
            :hooks [environ.leiningen.hooks]
            :profiles {:production {:env {:production true}}})
