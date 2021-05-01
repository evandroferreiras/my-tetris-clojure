(ns my-clojure-tetris.pieces
  (:require [schema.core :as s]
            [my-clojure-tetris.schemas :as schemas]))

(s/defn assoc-block-to-tile
  [tile :- schemas/Tile
   block :- schemas/Block]
  (if (not (= (:type block) :BLANK))
    (assoc tile :block block)
    tile))

(s/defn inject-piece-in-column
  [line-of-tile :- [schemas/Tile]
   line-of-piece :- [schemas/Block]
   column :- s/Int]
  (let [calculated-column    (if (> (+ (count line-of-piece) column) (count line-of-tile))
                               (- (count line-of-tile) (count line-of-piece))
                               column)
        line-of-tile (into [] line-of-tile)
        tiles-before-piece   (subvec (into [] line-of-tile) 0 calculated-column)
        tiles-after-piece    (subvec line-of-tile (+ (count tiles-before-piece) (count line-of-piece))
                                     (count line-of-tile))
        tiles-of-piece       (subvec line-of-tile calculated-column (+ (count line-of-piece) calculated-column))
        tiles-with-the-block (mapv #(assoc-block-to-tile %1 %2) tiles-of-piece line-of-piece)]
    (into [] (concat tiles-before-piece tiles-with-the-block tiles-after-piece))))

(s/defn advance-line?
  [piece :- schemas/Piece
   matrix :- schemas/Matrix]
  (let [last-line-of-piece (+ (count (:blocks piece)) (:current-line piece))]
    (not (> last-line-of-piece (count matrix)))))

(s/defn get-calculated-line :- s/Int
  [piece :- schemas/Piece
   matrix :- schemas/Matrix]
  (let [last-possible-line (- (count matrix) (count (:blocks piece)))]
    (if (not (advance-line? piece matrix))
      last-possible-line
      (:current-line piece))))

(s/defn insert-piece
  [piece :- schemas/Piece
   matrix :- schemas/Matrix]
  (let [column (:current-column piece)
        piece-blocks (:blocks piece)
        calculated-line (get-calculated-line piece matrix)
        lines-before-piece (subvec matrix 0 calculated-line)
        lines-after-piece  (subvec matrix (+ (count lines-before-piece) (count piece-blocks))
                                   (count matrix))
        lines-of-the-piece (mapv #(inject-piece-in-column %1 %2 column)
                                 (subvec matrix calculated-line (+ calculated-line (count piece-blocks)))
                                 piece-blocks)]
    (into [] (concat lines-before-piece lines-of-the-piece lines-after-piece))))

(s/defn advance-line
  [piece :- schemas/Piece]
  (update piece :current-line inc))
