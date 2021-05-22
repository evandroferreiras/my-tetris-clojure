(ns my-clojure-tetris.blocks
  (:require [my-clojure-tetris.schemas :as schemas]
            [schema.core :as s]))

(def dark-orange {:r 255 :g 140 :b 0})
(def lime-green {:r 0 :g 150 :b 0})
(def crimson {:r 220 :g 20 :b 60})
(def dark-turquoise {:r 0 :g 206 :b 209})
(def yellow {:r 254 :g 226 :b 62})

(s/defn get-color :- schemas/Color
  [block :- (s/maybe schemas/Block)]
  (let [type (:type block)]
    (cond
      (= type :STRAIGHT) dark-turquoise
      (= type :L) dark-orange
      (= type :SQUARE) yellow
      (= type :SKEW) lime-green
      (= type :INVERTED-SKEW) crimson
      (= type :INVERTED-L) {:r 255 :g 129 :b 0}
      :else {:r 60 :g 60 :b 60})))
