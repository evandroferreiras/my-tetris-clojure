(ns my-clojure-tetris.collision-detection
  (:require [schema.core :as s]
            [my-clojure-tetris.schemas :as schemas]
            [my-clojure-tetris.tiles :as tiles]
            [my-clojure-tetris.pieces :as pieces]))

(s/defn tile-is-colliding? :- s/Bool
        [tile :- schemas/Tile
         block :- schemas/Block]
        (and (true? (contains? tile :block)) (not (= :BLANK (:type block)))))

(s/defn line-is-colliding? :- s/Bool
        [line-of-piece :- [schemas/Block]
         line-of-matrix :- schemas/Tiles
         column :- s/Int]
        (let [tiles-of-piece (tiles/tiles-of-piece line-of-matrix line-of-piece column)]
          (reduce #(or %1 %2) (mapv tile-is-colliding? tiles-of-piece line-of-piece))))

(s/defn collided? :- s/Bool
        [piece :- schemas/Piece
         matrix :- schemas/Matrix]
        (let [matrix          (into [] matrix)
              column          (:current-column piece)
              tiles-of-piece  (pieces/get-lines-of-piece-from-matrix piece matrix (inc (:current-line piece)))
              blocks-of-piece (take-last (count tiles-of-piece) (:blocks piece))
              collided-lines  (mapv #(line-is-colliding? %1 %2 column) blocks-of-piece tiles-of-piece)]
          (if (empty? collided-lines)
            false
            (reduce #(or %1 %2) collided-lines))))
