(ns my-clojure-tetris.pieces
  (:require [schema.core :as s]
            [my-clojure-tetris.schemas :as schemas]
            [my-clojure-tetris.tetrominoes :as tetrominoes]
            [my-clojure-tetris.tiles :as tiles]))

(s/defn get-next-piece :- schemas/Piece
  [config :- schemas/Config]
  (-> (rand-nth tetrominoes/all-pieces)
      (assoc :current-column (rand-int (:total-of-columns config)))))

(s/defn advance-line? :- s/Bool
  [piece :- schemas/Piece
   matrix :- schemas/Matrix]
  (let [last-line-of-piece (+ (count (:blocks piece)) (:current-line piece))]
    (not (> last-line-of-piece (count matrix)))))

(s/defn advance-line :- schemas/Piece
  [piece :- schemas/Piece]
  (update piece :current-line inc))

(s/defn need-new-piece? :- s/Bool
  [piece :- schemas/Piece
   matrix :- schemas/Matrix]
  (false? (advance-line? piece matrix)))

(s/defn insert-piece :- schemas/Matrix
  [piece :- schemas/Piece
   matrix :- schemas/Matrix]
  (let [column             (:current-column piece)
        piece-blocks       (:blocks piece)
        last-possible-line (- (count matrix) (count (:blocks piece)))
        calculated-line    (if (need-new-piece? piece matrix)
                             last-possible-line
                             (:current-line piece))
        lines-before-piece (subvec matrix 0 calculated-line)
        lines-after-piece  (subvec matrix (+ (count lines-before-piece) (count piece-blocks))
                                   (count matrix))
        lines-of-the-piece (mapv #(tiles/inject-piece-in-column %1 %2 column)
                                 (subvec matrix calculated-line (+ calculated-line (count piece-blocks)))
                                 piece-blocks)]
    (into [] (concat lines-before-piece lines-of-the-piece lines-after-piece))))

(s/defn get-lines-of-piece-from-matrix :- schemas/Matrix
  [piece :- schemas/Piece
   matrix :- schemas/Matrix
   line :- s/Int]
  (let [matrix-last-line      (count matrix)
        start                 (min matrix-last-line line)
        piece-number-of-lines (min matrix-last-line (+ start (count (:blocks piece))))]
    (subvec matrix start piece-number-of-lines)))

;TODO: Define return schema for every function
;TODO: Try to separate in other files
