(ns my-clojure-tetris.tetrominoes
  (:require [schema.core :as s]
            [my-clojure-tetris.schemas :as schemas]))

(def sk {:type :SKEW})
(def sq {:type :SQUARE})
(def el {:type :L})
(def st {:type :STRAIGHT})
(def bk {:type :BLANK})

(s/def SKEW :- schemas/Piece
  {:blocks         [[bk sk sk]
                    [sk sk bk]]
   :current-line   0
   :current-column 0})

(s/def SQUARE :- schemas/Piece
  {:blocks         [[sq sq]
                    [sq sq]]
   :current-line   0
   :current-column 0})

(s/def L :- schemas/Piece
  {:blocks         [[el bk]
                    [el bk]
                    [el el]]
   :current-line   0
   :current-column 0})

(s/def STRAIGHT :- schemas/Piece
  {:blocks         [[st]
                    [st]
                    [st]
                    [st]]
   :current-line   0
   :current-column 0})

(s/def all-pieces :- [schemas/Piece]
  [SKEW
   SQUARE
   L
   STRAIGHT])
