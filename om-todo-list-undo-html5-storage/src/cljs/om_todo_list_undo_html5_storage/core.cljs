(ns om-todo-list-undo-html5-storage.core
  (:require [goog.dom :as gdom]
            [hodgepodge.core :refer [local-storage]]
            [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]))

(enable-console-print!)

(def tasklist-state (atom {:entries []}))
(def undo-state (atom {:entries [@tasklist-state]
                           :index nil}))

(add-watch tasklist-state :history
  (fn [_ _ _ n]
    (println "Event:" n)
    (let [entries (:entries @undo-state)]
      (when-not (= (last entries) n)
        (swap! undo-state
               (fn [state]
                 (update-in state [:entries] conj n)))))))

;; Get the persisted state, and if it exists
;; restore it on tasklist and undo states.
(when-let [state (:taskliststate local-storage)]
  (reset! tasklist-state state)
  (reset! undo-state {:entries [state]}))

;; Watch tasklist-state changes and
;; persists them in local storege.
(add-watch tasklist-state :persistece
  (fn [_ _ _ n]
    (println "Storage Event:" n)
    (assoc! local-storage :taskliststate n)))

(defn task-item
  [task owner]
  (reify
    om/IDisplayName
    (display-name [_]
      "task-item")

    om/IRender
    (render [_]
      (let [subject (:subject task)
            completed? (:completed task)]
        (html
         [:li {:on-click (fn [_] (om/transact! task :completed #(not %)))}
          (if completed?
           [:span {:style {:text-decoration "line-through"}} subject]
           [:span subject])])))))


(defn tasklist
  [app owner]
  (reify
    om/IDisplayName
    (display-name [_]
      "tasklist")

    om/IInitState
    (init-state [_]
      {:counter (atom 1)})

    om/IRenderState
    (render-state [_ {:keys [counter]}]
      (let [entries (:entries app)]
        (html
         [:section
          [:section.title
           [:strong "Task list:"]]
          [:section.input
           [:form {:on-submit (fn [e]
                                (.preventDefault e)
                                (let [input (-> (.-target e)
                                                (.querySelector "[name=subject]"))
                                      task  {:subject (.-value input)
                                             :id (swap! counter inc)
                                             :completed false}]

                                  ;; Set the input to empty value
                                  (set! (.-value input) "")

                                  ;; Append the previously defined task
                                  ;; to the task list entries on global
                                  ;; state atom.
                                  (om/transact! app :entries #(conj % task))))}
            [:input {:type "text"
                     :name "subject"
                     :placeholder "Enter task name..."}]
            [:input {:type "submit"
                     :defaultValue "Submit"}]]]

          [:section.list
           (if (empty? entries)
             [:span "No items in the task list..."]
             [:ul (for [item entries]
                    (om/build task-item item {:key :id}))])]])))))

(defn do-undo
  [app]
  (when (> (count (:entries @app)) 1)
    ;; Remove the last snapshot from the undo list.
    (om/transact! app :entries pop)

    ;; Restore the last snapshot into tasklist
    ;; application state
    (reset! tasklist-state (last (:entries @undo-state)))))

(defn undo-button
  [app owner]
  (reify
    om/IRender
    (render [_]
      (html
       [:section.undo
        [:section.buttons
         [:input {:type "button" :default-value "Undo"
                  :on-click (fn [_] (do-undo app))}]]
        [:hr]]))))

(let [undo-el (gdom/getElement "undo")
      tasklist-el (gdom/getElement "tasklist")]
  (om/root undo-button undo-state {:target undo-el})
  (om/root tasklist tasklist-state {:target tasklist-el}))

