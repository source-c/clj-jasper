(ns basic
  (:require [clj-jasper.core :as jc]
            [clojure.test :refer :all])
  (:import (java.io ByteArrayOutputStream)
           (net.sf.jasperreports.engine JRException)))

(defn- compile-report
  ([name] (compile-report name "samples"))
  ([name t-path]
   (binding [jc/*jr-templates-path* t-path]
     (jc/template->object name))))

(defn- render [reports]
  (try
    (let [output (ByteArrayOutputStream.)]
      (jc/render-multiple output
                          reports
                          {:compile           compile-report
                           :mtype             :pdf})
      true)
    (catch JRException e false)))

(deftest sample
  (testing "Integrity test: basic"
    (is (render [{:name       "basic"
                  :data       [{:code "1234-FOO-4321"}]
                  :parameters {}}]))))
