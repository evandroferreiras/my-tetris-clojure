(ns my-clojure-tetris.core
  (:require [quil.core :as q :include-macros true]
            [my-clojure-tetris.view :as view]
            [my-clojure-tetris.pieces :as pieces]
            [my-clojure-tetris.matrix :as matrix]
            [schema.core :as s]))

(def CONFIG {:default-width    30
             :default-height   30
             :total-of-lines   20
             :total-of-columns 10})

(def empty-matrix (matrix/get-matrix [] CONFIG))
(def _MTX_ (atom empty-matrix))

;TODO: Move this to other file
(def sk {:type :SKEW})
(def bk {:type :BLANK})
(def _CURRENT_PIECE_ (atom {:blocks [[bk sk sk]
                                     [sk sk bk]]
                    :current-line   0
                    :current-column 0}))

(s/def _LAST_TIME_PIECE_MOVED_ (atom (System/currentTimeMillis)))


(def WINDOW-WIDTH (* 10 (:default-width CONFIG)))
(def WINDOW-HEIGHT (* (:default-height CONFIG) (+ (:total-of-lines CONFIG) 4)))


(defn move-current-piece []
  (let [currentTimeMillis             (System/currentTimeMillis)
        milli-passed-since-last-moved (- currentTimeMillis @_LAST_TIME_PIECE_MOVED_)]
    (if (> milli-passed-since-last-moved 1000)
      (do (reset! _LAST_TIME_PIECE_MOVED_ currentTimeMillis)
          (when (true? (pieces/advance-line? @_CURRENT_PIECE_ empty-matrix))
            (swap! _CURRENT_PIECE_ pieces/advance-line))))))

(defn run-game []
  ;TODO: Create a new piece when is needed
  (move-current-piece)
  (reset! _MTX_
          (pieces/insert-piece @_CURRENT_PIECE_ empty-matrix)))

(defn draw []
  (run-game)
  (view/print-blocks! @_MTX_ 2))

(q/defsketch my
             :host "host"
             :size [WINDOW-WIDTH WINDOW-HEIGHT]
             :draw (var draw))

(s/set-fn-validation! true)
