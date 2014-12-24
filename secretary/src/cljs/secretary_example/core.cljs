(ns secretary-example.core
  (:require-macros [secretary.core :refer [defroute]])
  (:require [goog.events :as events]
            [goog.History.EventType :as HistoryEventType]
            [goog.dom :as dom]
            [secretary.core :as secretary])
  (:import goog.History))


(def app (dom/getElement "app"))

(defn set-html! [el content]
  (set! (.-innerHTML el) content))

(defroute home-path "/" []
  (let [message "<h1>Home page</h1><br><a href=\"/#/user/123\">User Link</a>"]
    (set-html! app message)))

(defroute user-path "/user/:param" [param]
  (let [message (str "<h1>User id: <small>" param "</small></h1>")]
    (set-html! app message)))

(defroute "*" []
  (set-html! app "<h1>Not Found</h1>"))

(defn main
  []
  (secretary/set-config! :prefix "#")

  ;; Configure history
  (let [history (History.)]
    (events/listen history HistoryEventType/NAVIGATE
                   (fn [event]
                     (secretary/dispatch! (.-token event))))
    (.setEnabled history true)))

(main)
