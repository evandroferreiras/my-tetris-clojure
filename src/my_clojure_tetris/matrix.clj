(ns my-clojure-tetris.matrix
  (:require [schema.core :as s]
            [my-clojure-tetris.schemas :as schemas]))

(s/defn empty-tile :- schemas/Tile
  [config :- schemas/Config]
  {:width  (:default-width config)
   :height (:default-height config)
   :line 0
   :column 0})

(s/defn ^:private set-line-number
  [lines :- [schemas/Tile]
   line-number :- s/Int]
  (map #(assoc % :line line-number) lines))

(s/defn ^:private get-empty-matrix :- schemas/Matrix
  [config :- schemas/Config]
  (let [total-of-lines (:total-of-lines config)
        total-of-columns (:total-of-columns config)
        empty-line (take total-of-columns (repeat (empty-tile config)))
        empty-line-with-number-column (map #(assoc %1 :column %2) empty-line (range (count empty-line)))
        all-lines (into [] (take total-of-lines
                                 (repeat empty-line-with-number-column)))
        all-lines-numbered (mapv set-line-number all-lines (range (count all-lines))) ]
    all-lines-numbered))

(s/defn ^:private block-color :- schemas/Color
  [block :- schemas/Block]
  (let [type (:type block)]
    (cond
      (= type :STRAIGHT) {:r 0 :g 75 :b 255}
      (= type :L) {:r 255 :g 17 :b 0}
      (= type :SQUARE) {:r 254 :g 226 :b 62}
      (= type :T) {:r 99 :g 200 :b 62}
      (= type :SKEW) {:r 219 :g 48 :b 130}
      (= type :INVERTED-SKEW) {:r 27 :g 161 :b 266}
      (= type :INVERTED-L) {:r 255 :g 129 :b 0}
      :else {:r 40 :g 40 :b 40})))

(s/defn get-matrix :- schemas/Matrix
  [matrix :- schemas/Matrix
   config :- schemas/Config]
  (if (empty? matrix)
    (get-empty-matrix config)
    matrix))
