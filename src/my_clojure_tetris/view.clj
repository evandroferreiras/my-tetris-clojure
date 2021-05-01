(ns my-clojure-tetris.view
  (:require [quil.core :as q]
            [schema.core :as s]
            [my-clojure-tetris.tiles :as tiles]
            [my-clojure-tetris.blocks :as blocks]
            [my-clojure-tetris.schemas :as schemas]))

(s/defn apply-color
  [color :- schemas/Color]
  (let [r (:r color)
        g (:g color)
        b (:b color)]
    (q/fill r g b)))

(s/defn apply-rect
  [tile :- schemas/Tile
   offset-lines :- s/Int]
  (let [x      (tiles/get-x tile)
        y      (tiles/get-y tile offset-lines)
        width  (:width tile)
        height (:height tile)]
    (apply-color (blocks/get-color (:block tile)))
    (q/rect x y width height)))

(s/defn print-blocks!
  [matrix :- schemas/Matrix
   offset-lines :- s/Int]
  (doseq [line-of-blocks matrix]
    (doseq [block line-of-blocks] (apply-rect block offset-lines))))
