(ns my-clojure-tetris.tiles
  (:require [schema.core :as s]
            [my-clojure-tetris.schemas :as schemas]))

(s/defn get-x :- s/Int
  [block :- schemas/Tile]
  (let [col   (:column block)
        width (:width block)]
    (* col width)))

(s/defn get-y
  [block :- schemas/Tile
   offset-lines :- s/Int]
  (* (+ offset-lines (:line block)) (:height block)))

(s/defn calculated-column :- s/Int
  [total-of-columns :- s/Int
   line-of-piece :- [schemas/Block]
   column :- s/Int]
  (if (> (+ (count line-of-piece) column) total-of-columns)
    (- total-of-columns (count line-of-piece))
    column))

(s/defn tiles-before-piece :- schemas/Tiles
  [line-of-tile :- schemas/Tiles
   line-of-piece :- [schemas/Block]
   column :- s/Int]
  (let [calculated-column (calculated-column (count line-of-tile) line-of-piece column)]
    (subvec (into [] line-of-tile) 0 calculated-column)))

(s/defn tiles-after-piece :- schemas/Tiles
  [line-of-tile :- schemas/Tiles
   line-of-piece :- [schemas/Block]
   column :- s/Int]
  (let [tiles-before-piece (tiles-before-piece line-of-tile line-of-piece column)]
    (subvec line-of-tile (+ (count tiles-before-piece) (count line-of-piece))
            (count line-of-tile))))

(s/defn tiles-of-piece :- schemas/Tiles
  [line-of-tile :- schemas/Tiles
   line-of-piece :- [schemas/Block]
   column :- s/Int]
  (let [line-of-tile      (into [] line-of-tile)
        line-of-piece     (into [] line-of-piece)
        calculated-column (calculated-column (count line-of-tile) line-of-piece column)]
    (subvec line-of-tile calculated-column (+ (count line-of-piece) calculated-column))))

(s/defn assoc-block-to-tile :- schemas/Tile
  [tile :- schemas/Tile
   block :- schemas/Block]
  (if (not (= (:type block) :BLANK))
    (assoc tile :block block)
    tile))

(s/defn inject-piece-in-column :- schemas/Tiles
  [line-of-tile :- schemas/Tiles
   line-of-piece :- [schemas/Block]
   column :- s/Int]
  (let [line-of-tile         (into [] line-of-tile)
        tiles-before-piece   (tiles-before-piece line-of-tile line-of-piece column)
        tiles-after-piece    (tiles-after-piece line-of-tile line-of-piece column)
        tiles-of-piece       (tiles-of-piece line-of-tile line-of-piece column)
        tiles-with-the-block (mapv #(assoc-block-to-tile %1 %2) tiles-of-piece line-of-piece)]
    (into [] (concat tiles-before-piece tiles-with-the-block tiles-after-piece))))
