(ns my-clojure-tetris.matrix
  (:require [schema.core :as s]
            [my-clojure-tetris.schemas :as schemas]))

(s/defn empty-tile :- schemas/Tile
  [config :- schemas/Config]
  {:width  (:default-width config)
   :height (:default-height config)
   :line   0
   :column 0})

(s/defn ^:private set-line-number
  [lines :- [schemas/Tile]
   line-number :- s/Int]
  (map #(assoc % :line line-number) lines))

(s/defn empty-line
  [config :- schemas/Config]
  (let [total-of-columns (:total-of-columns config)]
    (take total-of-columns (repeat (empty-tile config)))))

(s/defn ^:private get-empty-matrix :- schemas/Matrix
  [config :- schemas/Config]
  (let [total-of-lines                (:total-of-lines config)
        empty-line                    (empty-line config)
        empty-line-with-number-column (map #(assoc %1 :column %2) empty-line (range (count empty-line)))
        all-lines                     (into [] (take total-of-lines
                                                     (repeat empty-line-with-number-column)))
        all-lines-numbered            (mapv set-line-number all-lines (range (count all-lines)))]
    all-lines-numbered))

(s/defn get-matrix :- schemas/Matrix
  [matrix :- schemas/Matrix
   config :- schemas/Config]
  (if (empty? matrix)
    (get-empty-matrix config)
    matrix))
