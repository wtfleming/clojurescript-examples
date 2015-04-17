(ns core-async-examples.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [goog.events :as events]
            [goog.events.EventType :as EventType]
            [goog.dom :as dom]
            [cljs.core.async
             :as async
             :refer [>! <! chan dropping-buffer sliding-buffer onto-chan close!]]))


(defn set-inner-html!
  "Helper function to set contents of a DOM element."
  [elem value]
  (set! (.-innerHTML (dom/getElement elem)) value))


(defn handle-example-zero-button-click
  "Write to and read from an unbuffered channel."
  [_]
  (let [ch (chan)]
    ;; Writing to an ubuffered channel blocks until the message is
    ;; read by a consumer of the channel. So in this example we must
    ;; read and write from the channel in separate go blocks.
    (go
      (>! ch "Hello from an unbuffered channel!"))

    ;; Read from the channel
    (go
      (let [msg (<! ch)]
        (set-inner-html! "example-zero-output" msg)
        (close! ch)))))



(defn handle-example-one-button-click
  "Write to and read from a buffered channel."
  [_]
  (go
    (let [ch (chan 5)
          _ (>! ch "Hello from a buffered channel!")
          msg (<! ch)]
      (set-inner-html! "example-one-output" msg)
      (close! ch))))

(defn handle-example-two-button-click
  "Write to and read from a channel with a dropping buffer."
  [_]
  (go
    (let [ch (chan (dropping-buffer 5))
          _ (onto-chan ch (range 0 10))
          msg (<! (async/into [] ch))]
      (set-inner-html! "example-two-output" msg))))

(defn handle-example-three-button-click
  "Write to and read from to a channel with a sliding buffer."
  [_]
  (go
    (let [ch (chan (sliding-buffer 5))
          _ (onto-chan ch (range 0 10))
          msg (<! (async/into [] ch))]
      (set-inner-html! "example-three-output" msg))))

(defn set-click-handler
  "Helper function to setting click event handlers."
  [btn-element handler-fn]
  (let [btn (dom/getElement btn-element)]
    (events/listen btn EventType/CLICK (fn [e] (handler-fn e)))))


(defn main []
    (set-click-handler "example-zero-button" handle-example-zero-button-click)
    (set-click-handler "example-one-button" handle-example-one-button-click)
    (set-click-handler "example-two-button" handle-example-two-button-click)
    (set-click-handler "example-three-button" handle-example-three-button-click))


(main)
