(ns my-clojure-tetris.blocks
  (:require [my-clojure-tetris.schemas :as schemas]
            [schema.core :as s]))

(s/defn get-color :- schemas/Color
  [block :- (s/maybe schemas/Block)]
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
