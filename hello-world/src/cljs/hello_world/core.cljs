(ns hello-world.core)

(defn set-html! [el content]
  (set! (.-innerHTML el) content))

(defn main
  []
  (let [content "ClojureScript Hello World"
        element (aget (js/document.getElementsByTagName "main") 0)]
    (set-html! element content)))

(main)
