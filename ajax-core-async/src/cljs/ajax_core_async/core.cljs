(ns ajax-core-async.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [goog.events :as events]
            [goog.dom :as dom]
            [cljs.core.async :refer [<! put! chan]])
  (:import goog.History
           goog.Uri
           goog.net.Jsonp))


(def search-url "http://en.wikipedia.org/w/api.php?action=opensearch&format=json&search=")

(def home-html
  (str "<h1>Search Wikipedia:</h1>"
       "<section>"
       "  <input id=\"query\" placeholder=\"Type your search...\" />"
       "  <button id=\"searchbutton\">Search</button>"
       "  <ul id=\"results\"></ul>"
       "</section>"))

(defn set-html! [el content]
  (set! (.-innerHTML el) content))

(defn build-html-list [items]
  (apply str (map #(str "<li>" % "</li>") items)))

(defn render-results [results]
  (let [article-names (second results)]
    (build-html-list article-names)))


(defn listen
  "Returns a core.async channel for events on an element."
  [el type]
  (let [out (chan)]
    (events/listen el type (fn [e] (put! out e)))
    out))

(defn do-jsonp [uri]
  (let [out (chan)
        req (Jsonp. (Uri. uri))]
    (.send req nil (fn [res] (put! out res)))
    out))


(defn handle-search-button-click [clicks]
  (go (while true
        (<! clicks)
        (let [uri     (str search-url (.-value (dom/getElement "query")))
              results (<! (do-jsonp uri))]
          (set-html! (dom/getElement "results")
                     (render-results results))))))

(defn main []
  (set-html! (dom/getElement "app") home-html)

  (let [clicks (listen (dom/getElement "searchbutton") (.-CLICK events/EventType))]
    (handle-search-button-click clicks)))


(main)
