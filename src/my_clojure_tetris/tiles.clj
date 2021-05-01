(ns my-clojure-tetris.tiles
  (:require [schema.core :as s]
            [my-clojure-tetris.schemas :as schemas]))

(s/defn get-x :- s/Int
        [block :- schemas/Tile]
        (let [col    (:column block)
              width  (:width block)]
          (* col width)))

(s/defn get-y
        [block :- schemas/Tile
         offset-lines :- s/Int]
        (* (+ offset-lines (:line block)) (:height block)))
