(ns my-clojure-tetris.pieces-test
  (:require [clojure.test :refer :all])
  (:require [my-clojure-tetris.pieces :as pieces]
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

(deftest insert-piece-test
  (testing "Should insert the :SKEW piece in the column and line 1"
    (let [matrix-with-skew-piece [first-line
                                  [t2 t2 ts2 ts2]
                                  [t3 ts3 ts3 t3]
                                  forth-line]]
      (is (= matrix-with-skew-piece
             (pieces/insert-piece {:blocks         skew-piece-blocks
                                   :current-line   1
                                   :current-column 1}
                                  empty-matrix)))))
  (testing "Should insert the :SKEW piece in the line 1 column 3"
    (let [matrix-with-skew-piece [first-line
                                  [t2 t2 ts2 ts2]
                                  [t3 ts3 ts3 t3]
                                  forth-line]]
      (is (= matrix-with-skew-piece
             (pieces/insert-piece {:blocks         skew-piece-blocks
                                   :current-line   1
                                   :current-column 3}
                                  empty-matrix)))))

  (testing "Should insert the :SKEW piece in the line 1 and column 0"
    (let [matrix-with-skew-piece [first-line
                                  [t2 ts2 ts2 t2]
                                  [ts3 ts3 t3 t3]
                                  forth-line]]
      (is (= matrix-with-skew-piece
             (pieces/insert-piece {:blocks         skew-piece-blocks
                                   :current-line   1
                                   :current-column 0}
                                  empty-matrix)))))

  (testing "Should insert the :SKEW piece in the line 3 and column 0"
    (let [matrix-with-skew-piece [first-line
                                  second-line
                                  [t3 ts3 ts3 t3]
                                  [ts4 ts4 t4 t4]]]
      (is (= matrix-with-skew-piece
             (pieces/insert-piece {:blocks         skew-piece-blocks
                                   :current-line   3
                                   :current-column 0}
                                  empty-matrix)))))

  (testing "Should insert the :SKEW piece in the line 4 and column 0"
    (let [matrix-with-skew-piece [first-line
                                  second-line
                                  [t3 ts3 ts3 t3]
                                  [ts4 ts4 t4 t4]]]
      (is (= matrix-with-skew-piece
             (pieces/insert-piece {:blocks         skew-piece-blocks
                                   :current-line   4
                                   :current-column 0}
                                  empty-matrix))))))

(deftest advance-line-test
  (testing "Should increment current line"
    (let [piece {:blocks         skew-piece-blocks
                 :current-line   1
                 :current-column 1}]
      (is (= (assoc piece :current-line 2)
             (pieces/advance-line piece))))))

(deftest advance-line?-test
  (testing "Should advance when the piece is in the beginning of matrix"
    (let [piece {:blocks         skew-piece-blocks
                 :current-line   0
                 :current-column 1}]
      (is (true? (pieces/advance-line? piece empty-matrix)))))
  (testing "Should not advance if the piece is at the end of matrix"
    (let [piece {:blocks         skew-piece-blocks
                 :current-line   2
                 :current-column 0}]
      (is false? (pieces/advance-line? piece empty-matrix))))

  (testing "Should not advance if the piece is passed the end of matrix"
    (let [piece {:blocks         skew-piece-blocks
                 :current-line   3
                 :current-column 0}]
      (is false? (pieces/advance-line? piece empty-matrix)))))
