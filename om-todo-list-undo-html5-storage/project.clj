(defproject om-todo-list-undo-html5-storage "0.1.0"
  :description "ClojureScript om undo with HTML5 storage web app"
    :url "https://github.com/wtfleming/clojurescript-examples"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.3.1"]
                 [ring/ring-defaults "0.1.2"]
                 [sablono "0.2.22"]
                 [org.om/om "0.8.0"]
                 [hodgepodge "0.1.0"]
                 [org.clojure/clojurescript "0.0-2511"]]
  :ring {:handler om-todo-list-undo-html5-storage.core.handler/app}
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


