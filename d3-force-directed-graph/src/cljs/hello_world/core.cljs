(ns hello-world.core)

(defn- build-force-layout [width height]
  (.. js/d3
      -layout
      force
      (charge -140)
      (linkDistance 40)
      (size (array width height))))

(defn- setup-force-layout [force-layout graph]
  (.. force-layout
      (nodes (.-nodes graph))
      (links (.-links graph))
      start))

(defn- build-svg [width height]
  (.. js/d3
      (select ".app")
      (append "svg")
      (attr "width" width)
      (attr "height" height)))

(defn- build-links [svg graph]
  (.. svg
      (selectAll ".link")
      (data (.-links graph))
      enter
      (append "line")
      (attr "class" "link")
      (attr "stroke" "grey")
      (style "stroke-width" 1)))

(defn- build-nodes [svg graph force-layout]
  (.. svg
      (selectAll ".node")
      (data (.-nodes graph))
      enter
      (append "text")
      (attr "cx" 12)
      (attr "cy" ".35em")
      (text #(.-name %))
      (call (.-drag force-layout))))


(defn on-tick [link node]
  (fn []
    (.. link
        (attr "x1" #(.. % -source -x))
        (attr "y1" #(.. % -source -y))
        (attr "x2" #(.. % -target -x))
        (attr "y2" #(.. % -target -y)))
    (.. node
        (attr "transform" #(str "translate(" (.. % -x) "," (.. % -y) ")")))))


(defn ^:export main [json-file]
  (let [width 960
        height 600
        force-layout (build-force-layout width height)
        svg (build-svg width height)]
    (.json js/d3 json-file
           (fn [error json]
             (.. json
                 -links
                 (forEach #(do (aset %1 "weight" 1.0)
                               (aset %1 "index" %2))))
             (setup-force-layout force-layout json)
             (let [links (build-links svg json)
                   nodes (build-nodes svg json force-layout)]
               (.on force-layout "tick"
                    (on-tick links nodes)))))))

