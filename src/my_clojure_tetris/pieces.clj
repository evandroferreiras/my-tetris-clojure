(ns my-clojure-tetris.pieces
  (:require [schema.core :as s]
            [my-clojure-tetris.schemas :as schemas]
            [my-clojure-tetris.tetrominoes :as tetrominoes]))

(s/defn assoc-block-to-tile
  [tile :- schemas/Tile
   block :- schemas/Block]
  (if (not (= (:type block) :BLANK))
    (assoc tile :block block)
    tile))

(s/defn calculated-column
  [total-of-columns :- s/Int
   line-of-piece :- [schemas/Block]
   column :- s/Int]
  (if (> (+ (count line-of-piece) column) total-of-columns)
    (- total-of-columns (count line-of-piece))
    column))

(s/defn tiles-before-piece
  [line-of-tile :- [schemas/Tile]
   line-of-piece :- [schemas/Block]
   column :- s/Int]
  (let [calculated-column (calculated-column (count line-of-tile) line-of-piece column)]
    (subvec (into [] line-of-tile) 0 calculated-column)))

(s/defn tiles-after-piece
  [line-of-tile :- [schemas/Tile]
   line-of-piece :- [schemas/Block]
   column :- s/Int]
  (let [tiles-before-piece (tiles-before-piece line-of-tile line-of-piece column)]
    (subvec line-of-tile (+ (count tiles-before-piece) (count line-of-piece))
            (count line-of-tile))))

(s/defn tiles-of-piece
  [line-of-tile :- [schemas/Tile]
   line-of-piece :- [schemas/Block]
   column :- s/Int]
  (let [line-of-tile      (into [] line-of-tile)
        line-of-piece     (into [] line-of-piece)
        calculated-column (calculated-column (count line-of-tile) line-of-piece column)]
    (subvec line-of-tile calculated-column (+ (count line-of-piece) calculated-column))))

(s/defn inject-piece-in-column
  [line-of-tile :- [schemas/Tile]
   line-of-piece :- [schemas/Block]
   column :- s/Int]
  (let [line-of-tile         (into [] line-of-tile)
        tiles-before-piece   (tiles-before-piece line-of-tile line-of-piece column)
        tiles-after-piece    (tiles-after-piece line-of-tile line-of-piece column)
        tiles-of-piece       (tiles-of-piece line-of-tile line-of-piece column)
        tiles-with-the-block (mapv #(assoc-block-to-tile %1 %2) tiles-of-piece line-of-piece)]
    (into [] (concat tiles-before-piece tiles-with-the-block tiles-after-piece))))

(s/defn advance-line?
  [piece :- schemas/Piece
   matrix :- schemas/Matrix]
  (let [last-line-of-piece (+ (count (:blocks piece)) (:current-line piece))]
    (not (> last-line-of-piece (count matrix)))))

(s/defn need-new-piece?
  [piece :- schemas/Piece
   matrix :- schemas/Matrix]
  (false? (advance-line? piece matrix)))

(s/defn last-possible-line :- s/Int
  [piece :- schemas/Piece
   matrix :- schemas/Matrix]
  (- (count matrix) (count (:blocks piece))))

(s/defn calculated-line :- s/Int
  [piece :- schemas/Piece
   matrix :- schemas/Matrix]
  (let [last-possible-line (last-possible-line piece matrix)]
    (if (not (advance-line? piece matrix))
      last-possible-line
      (:current-line piece))))

(s/defn lines-before-piece
  [piece :- schemas/Piece
   matrix :- schemas/Matrix]
  (let [calculated-line (calculated-line piece matrix)]
    (subvec matrix 0 calculated-line)))

(s/defn lines-after-piece
  [piece :- schemas/Piece
   matrix :- schemas/Matrix]
  (let [piece-blocks       (:blocks piece)
        lines-before-piece (lines-before-piece piece matrix)]
    (subvec matrix (+ (count lines-before-piece) (count piece-blocks))
            (count matrix))))

(s/defn insert-piece
  [piece :- schemas/Piece
   matrix :- schemas/Matrix]
  (let [column             (:current-column piece)
        piece-blocks       (:blocks piece)
        calculated-line    (calculated-line piece matrix)
        lines-before-piece (lines-before-piece piece matrix)
        lines-after-piece  (lines-after-piece piece matrix)
        lines-of-the-piece (mapv #(inject-piece-in-column %1 %2 column)
                                 (subvec matrix calculated-line (+ calculated-line (count piece-blocks)))
                                 piece-blocks)]
    (into [] (concat lines-before-piece lines-of-the-piece lines-after-piece))))

(s/defn advance-line
  [piece :- schemas/Piece]
  (update piece :current-line inc))

(s/defn tile-is-colliding? :- s/Bool
  [tile :- schemas/Tile
   block :- schemas/Block]
  (and (true? (contains? tile :block)) (not (= :BLANK (:type block)))))

(s/defn get-tiles-of-piece-from-line :- schemas/Matrix
  [piece :- schemas/Piece
   matrix :- schemas/Matrix
   line :- s/Int]
  (let [matrix-last-line      (count matrix)
        start                 (min matrix-last-line line)
        piece-number-of-lines (min matrix-last-line (+ start (count (:blocks piece))))]
    (subvec matrix start piece-number-of-lines)))

(s/defn line-is-colliding? :- s/Bool
  [line-of-piece :- [schemas/Block]
   line-of-matrix :- [schemas/Tile]
   column :- s/Int]
  (let [tiles-of-piece (tiles-of-piece line-of-matrix line-of-piece column)]
    (reduce #(or %1 %2) (mapv tile-is-colliding? tiles-of-piece line-of-piece))))

(s/defn collided? :- s/Bool
  [piece :- schemas/Piece
   matrix :- schemas/Matrix]
  (let [matrix          (into [] matrix)
        column          (:current-column piece)
        tiles-of-piece  (get-tiles-of-piece-from-line piece matrix (inc (:current-line piece)))
        blocks-of-piece (take-last (count tiles-of-piece) (:blocks piece))
        collided-lines  (mapv #(line-is-colliding? %1 %2 column) blocks-of-piece tiles-of-piece)]
    (if (empty? collided-lines)
      false
      (reduce #(or %1 %2) collided-lines))))

(s/defn get-next-piece :- schemas/Piece

  [config :- schemas/Config]
  (-> (rand-nth tetrominoes/all-pieces)
      (assoc :current-column (rand-int (:total-of-columns config)))))

;TODO: Define return schema for every function
;TODO: Try to separate in other files
