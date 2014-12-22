(defproject ajax-closure "0.1.0"
  :description "Example of using Google Closure AJAX"
    :url "https://github.com/wtfleming/clojurescript-examples"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.3.1"]
                 [ring/ring-defaults "0.1.2"]
                 [org.clojure/clojurescript "0.0-2505"]]
  :ring {:handler ajax-closure.core.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}}
  :source-paths ["src/clj"]
  :plugins [[lein-cljsbuild "1.0.3"]
            [lein-ring "0.8.13"]]
  :cljsbuild {:builds
              [{:id "app"
                :source-paths ["src/cljs"]
                :compiler {:output-to "resources/public/js/app.js"
                           :optimizations :whitespace
                           :pretty-print true}}]})


