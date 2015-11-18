(defproject om-hello-world "0.1.0"
  :description "ClojureScript om hello world web app"
    :url "https://github.com/wtfleming/clojurescript-examples"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [compojure "1.4.0"]
                 [ring/ring-defaults "0.1.5"]
                 [sablono "0.4.0"]
                 [org.omcljs/om "0.9.0"]
                 [org.clojure/clojurescript "1.7.170"]]
  :ring {:handler om-hello-world.core.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}}
  :source-paths ["src/clj"]
  :plugins [[lein-cljsbuild "1.1.1"]
            [lein-ring "0.9.7"]]
  :cljsbuild {:builds
              [{:id "app"
                :source-paths ["src/cljs"]
                :compiler {:output-to "resources/public/js/app.js"
                           :optimizations :whitespace
                           :pretty-print true}}]})


