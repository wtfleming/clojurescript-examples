(ns ajax-closure.core
  (:require [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.dom :as dom])
  (:import goog.Uri
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

(defn do-jsonp [uri callback]
  (let [req (Jsonp. (Uri. uri))]
    (.send req nil callback)))

(defn on-search-response [results]
  (let [html (render-results results)]
    (set-html! (dom/getElement "results") html)))

(defn on-search-btn-click [_]
  (let [query (.-value (dom/getElement "query"))
        search-uri (str search-url query)]
    (do-jsonp search-uri on-search-response)))


(defn main []
  (set-html! (dom/getElement "app") home-html)
  (events/listen (dom/getElement "searchbutton") EventType/CLICK on-search-btn-click))

(main)

