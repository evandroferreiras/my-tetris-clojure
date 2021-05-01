(ns my-clojure-tetris.tiles-test
  (:require [clojure.test :refer :all])
  (:require [my-clojure-tetris.tiles :refer [get-x get-y]]
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
         (get-x tile))))

(deftest get-y-test
  (testing "get-y should return 20 when without offset"
    (is (= 20
           (get-y tile 0))))

  (testing "get-y should return 50 when have 3 line as offset"
    (is (= 50
           (get-y tile 3)))))
