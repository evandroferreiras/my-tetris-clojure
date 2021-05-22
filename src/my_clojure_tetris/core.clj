(ns my-clojure-tetris.core
  (:require [quil.core :as q :include-macros true]
            [my-clojure-tetris.view :as view]
            [my-clojure-tetris.pieces :as pieces]
            [my-clojure-tetris.matrix :as matrix]
            [my-clojure-tetris.collision-detection :as collision-detection]
            [my-clojure-tetris.game :as game]
            [schema.core :as s]))

(defn draw []
  (game/run-game)
  (view/print-blocks! @game/_MTX_ 2))

(defn key-pressed []
  (game/manage-key-pressed (q/key-as-keyword)))

(q/defsketch my-tetris-game
             :host "host"
             :size [game/WINDOW-WIDTH game/WINDOW-HEIGHT]
             :key-pressed (var key-pressed)
             :draw (var draw))

(s/set-fn-validation! true)

;TODO: Implement more tests
;TODO: Use generative tests
;TODO: Implement rotation
;TODO: Implement score
