(ns my-clojure-tetris.schemas
  (:require [schema.core :as s]))

(s/defschema Color
  {:r s/Int
   :g s/Int
   :b s/Int})

(s/defschema Config
  {:total-of-columns s/Int
   :total-of-lines   s/Int
   :default-width    s/Int
   :default-height   s/Int})

(s/defschema Block
  {:type s/Keyword})

(s/defschema Tile
  {:column                 s/Int
   :line                   s/Int
   :width                  s/Int
   :height                 s/Int
   (s/optional-key :block) Block})

(s/defschema Matrix [[Tile]])

(s/defschema Piece
  {:current-column s/Int
   :current-line   s/Int
   :blocks         [[Block]]})
