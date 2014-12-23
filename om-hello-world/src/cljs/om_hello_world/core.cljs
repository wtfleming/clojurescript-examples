(ns om-hello-world.core
  (:require [goog.dom :as gdom]
            [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]))

(enable-console-print!)

(def app-state (atom {:message "Message from global state."}))

(defn my-component
  [app owner]
  (reify
    om/IDisplayName
    (display-name [_]
      "my-component")

    om/IInitState
    (init-state [_]
      {:message "Message from local state."})

    om/IRenderState
    (render-state [_ {:keys [message]}]
      (html [:section
             [:div message]
             [:div (:message app)]]))))

(om/root my-component app-state {:target (gdom/getElement "app")})
