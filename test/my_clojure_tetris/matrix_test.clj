(ns my-clojure-tetris.matrix-test
  (:require [clojure.test :refer :all])
  (:require [my-clojure-tetris.matrix :refer [get-matrix]]
            [schema.core :as s]))

(s/set-fn-validation! true)

(def empty-tile {:width  10
                 :height 10})

(def empty-matrix [[(assoc empty-tile :column 0 :line 0) (assoc empty-tile :column 1 :line 0)]
                   [(assoc empty-tile :column 0 :line 1) (assoc empty-tile :column 1 :line 1)]])

(def config {:default-width 10 :default-height 10 :total-of-lines 2 :total-of-columns 2})

(deftest get-matrix-test
  (testing "should return a empty-matrix"
    (is (= empty-matrix
           (get-matrix [] config))))

  (testing "should return the current-matrix"
    (is (= empty-matrix
           (get-matrix empty-matrix config)))))
