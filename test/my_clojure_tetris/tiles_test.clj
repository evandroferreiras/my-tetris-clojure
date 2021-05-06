(ns my-clojure-tetris.tiles-test
  (:require [clojure.test :refer :all])
  (:require [my-clojure-tetris.tiles :as tiles]
            [schema-generators.generators :as gen]
            [my-clojure-tetris.schemas :as schemas]))

(def tile (-> (gen/generate schemas/Tile)
              (assoc
                 :column 0
                 :line 2
                 :width 10
                 :height 10)))

(deftest get-x-test
  (is (= 0
         (tiles/get-x tile))))

(deftest get-y-test
  (testing "get-y should return 20 when without offset"
    (is (= 20
           (tiles/get-y tile 0))))

  (testing "get-y should return 50 when have 3 line as offset"
    (is (= 50
           (tiles/get-y tile 3)))))

(def tl (-> (gen/generate schemas/Tile)
            (assoc :line 999)
            (dissoc :block)))

(def sk {:type :SKEW})
(def bk {:type :BLANK})

(def ts (assoc tl :block sk))

(deftest inject-piece-in-column-test
  (testing ""
    (let []
      (is (= [tl ts ts tl]
             (tiles/inject-piece-in-column [tl tl tl tl]
                                            [bk sk sk bk]
                                            0)))
      (is (= [tl ts ts tl]
             (tiles/inject-piece-in-column [tl tl tl tl]
                                            [bk sk sk]
                                            0))))))
