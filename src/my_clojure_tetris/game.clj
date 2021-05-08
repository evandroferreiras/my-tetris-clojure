(ns my-clojure-tetris.game
  (:require [my-clojure-tetris.collision-detection :as collision-detection]
            [my-clojure-tetris.pieces :as pieces]
            [my-clojure-tetris.matrix :as matrix]))

(def CONFIG {:default-width    30
             :default-height   30
             :total-of-lines   20
             :total-of-columns 10})
(def WINDOW-WIDTH (* 10 (:default-width CONFIG)))
(def WINDOW-HEIGHT (* (:default-height CONFIG) (+ (:total-of-lines CONFIG) 4)))

(def _GAME_MTX_ (atom (matrix/get-matrix [] CONFIG)))
(def _MTX_ (atom (matrix/get-matrix [] CONFIG)))
(def _CURRENT_PIECE_ (atom (pieces/get-next-piece CONFIG)))

(def _LAST_TIME_PIECE_MOVED_ (atom (System/currentTimeMillis)))

(defn move-current-piece []
  (let [currentTimeMillis             (System/currentTimeMillis)
        milli-passed-since-last-moved (- currentTimeMillis @_LAST_TIME_PIECE_MOVED_)]
    (if (> milli-passed-since-last-moved 100)
      (do (reset! _LAST_TIME_PIECE_MOVED_ currentTimeMillis)
          (when (true? (pieces/advance-line? @_CURRENT_PIECE_ @_MTX_))
            (swap! _CURRENT_PIECE_ pieces/advance-line))))))

(defn setup-current-piece []
  (when (or (pieces/need-new-piece? @_CURRENT_PIECE_ @_MTX_)
            (collision-detection/collided? @_CURRENT_PIECE_ @_GAME_MTX_))
    (reset! _CURRENT_PIECE_ (pieces/get-next-piece CONFIG))))

(defn setup-matrix []
  (when (or (pieces/need-new-piece? @_CURRENT_PIECE_ @_MTX_)
            (collision-detection/collided? @_CURRENT_PIECE_ @_GAME_MTX_))
    (do
      (reset! _GAME_MTX_
              (pieces/insert-piece @_CURRENT_PIECE_ @_GAME_MTX_)))))

(defn run-game []
  (setup-matrix)
  (setup-current-piece)
  (move-current-piece)
  (reset! _MTX_
          (pieces/insert-piece @_CURRENT_PIECE_ @_GAME_MTX_)))