(ns my-clojure-tetris.collision-detection-test
  (:require [clojure.test :refer :all])
  (:require [my-clojure-tetris.collision-detection :as collision-detection]
            [schema-generators.generators :as gen]
            [my-clojure-tetris.schemas :as schemas]))

(def tl (-> (gen/generate schemas/Tile)
            (assoc :line 999)
            (dissoc :block)))

(def sk {:type :SKEW})
(def bk {:type :BLANK})

(def t1 (assoc tl :line 100))

(def first-line [t1 t1 t1 t1])

(def t2 (assoc tl :line 200))
(def ts2 (assoc t2 :block sk))
(def second-line [t2 t2 t2 t2])

(def t3 (assoc tl :line 300))
(def ts3 (assoc t3 :block sk))
(def third-line [t3 t3 t3 t3])

(def t4 (assoc tl :line 400))
(def ts4 (assoc t4 :block sk))
(def forth-line [t4 t4 t4 t4])

(def empty-matrix [first-line
                   second-line
                   third-line
                   forth-line])

(def skew-piece-blocks [[bk sk sk]
                        [sk sk bk]])

(def square-piece-blocks [[sk sk]
                          [sk sk]])

(deftest collided?-test
  (testing "Should not collide"
    (let [piece {:blocks         skew-piece-blocks
                 :current-line   0
                 :current-column 0}]
      (is (false? (collision-detection/collided? piece
                                                 empty-matrix))))

    (let [piece {:blocks         skew-piece-blocks
                 :current-line   3
                 :current-column 3}]
      (is (false? (collision-detection/collided? piece
                                                 empty-matrix)))))

  (testing "Should collide"
    (let [filled-matrix [first-line
                         second-line
                         [t3 ts3 ts3 t3]
                         [ts4 ts4 t4 t4]]
          piece         {:blocks         skew-piece-blocks
                         :current-line   0
                         :current-column 0}]
      (is (true? (collision-detection/collided? piece
                                                filled-matrix))))
    (let [filled-matrix [first-line
                         second-line
                         [t3 t3 t3 ts3]
                         [t4 ts4 ts4 ts4]]
          piece         {:blocks         skew-piece-blocks
                         :current-line   1
                         :current-column 1}]
      (is (true? (collision-detection/collided? piece
                                                filled-matrix))))
    (let [filled-matrix [first-line
                         [t2 t2 ts2 t2]
                         [t3 t3 ts3 t3]
                         [t4 t4 ts4 t4]]
          piece         {:blocks         skew-piece-blocks
                         :current-line   0
                         :current-column 0}]
      (is (true? (collision-detection/collided? piece
                                                filled-matrix))))

    (let [filled-matrix [first-line
                         [t2 t2 t2 t2]
                         [ts3 t3 t3 t3]
                         [ts4 ts4 t4 t4]]
          piece         {:blocks         square-piece-blocks
                         :current-line   1
                         :current-column 1}]
      (is (true? (collision-detection/collided? piece
                                                filled-matrix))))))
