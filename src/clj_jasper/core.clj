(ns clj-jasper.core
  (:require [clojure.java.io :as io]
            [clojure.walk :as walk]
            [clojure.string :as string])

  (:import (java.util HashMap Map)
           (net.sf.jasperreports.engine JasperCompileManager
                                        JasperFillManager
                                        JasperExportManager
                                        JRDataSource
                                        JRRewindableDataSource
                                        JRField
                                        JasperReport)))

(defn- data->jr [coll]
  (let [initial (cons nil coll)
        data    (atom initial)]
    (reify JRRewindableDataSource
      (moveFirst [_]
        (reset! data initial))
      (getFieldValue [_ ^JRField jrField]
        (get (first @data) (keyword (.getName jrField))))
      (next [_]
        (not (empty? (swap! data next)))))))

(def ^:private mime-types
  {:pdf "application/pdf"})

(def ^:dynamic *jr-templates-path* "reports")

(defn- read-template [t-path]
  (if
    (.isAbsolute (io/file t-path))
    (-> t-path io/file)
    (-> t-path io/resource)))

(defn template->object [name]
  (let [template (some->
                   (format "%s/%s.jrxml" *jr-templates-path* name)
                   read-template)]
    (some-> template io/input-stream JasperCompileManager/compileReport)))

(defn data->report [{:keys [name data mtype filename ops report]
                     :or   {mtype :pdf}}]

  (let [report (or report (template->object name))
        j-data (data->jr (or data {:empty true}))
        filled (JasperFillManager/fillReport
                 ^JasperReport report
                 ^Map (HashMap. ^Map (walk/stringify-keys (or ops {})))
                 ^JRDataSource j-data)
        bytes  (case mtype
                 :pdf (JasperExportManager/exportReportToPdf filled))]

    {:name (or filename (format "%s.%s" (last (string/split name #"/")) (clojure.core/name mtype)))
     :type (get mime-types mtype)
     :file bytes}))
