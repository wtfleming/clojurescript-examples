(ns closure-dom-events.core
  (:require [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.dom :as dom]))

(defn main []
  (let [counter (atom 0)
        button (dom/getElement "button")
        display (dom/getElement "numclicks")]
    (set! (.-innerHTML display) @counter)
    (events/listen button EventType/CLICK
                   (fn [event]
                     (swap! counter inc)
                     (set! (.-innerHTML display) @counter)))))

(main)
