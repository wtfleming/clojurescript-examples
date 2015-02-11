(ns force-directed-graph.core.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.response :refer [render]]
            [clojure.java.io :as io]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn home
  [req]
  (render (io/resource "index.html") req))

(defroutes app-routes
  (GET "/" [] home)
  (route/resources "/static")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
