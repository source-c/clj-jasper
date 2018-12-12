(ns clj-jasper.core
  (:require [clojure.java.io :as io]
            [clojure.walk :as walk])
  (:import (java.util HashMap Map)
           (net.sf.jasperreports.engine JasperCompileManager
                                        JasperFillManager
                                        JasperExportManager
                                        JRDataSource
                                        JasperReport)))

(defn- data->jr [coll]
  (let [seen (atom false)
        data (atom coll)]
    (reify JRDataSource
      (getFieldValue [_ jrField]
        (get (first @data) (keyword (.getName jrField))))
      (next [_]
        (if @seen
          (not (empty? (swap! data next)))
          (do
            (swap! seen (constantly true))
            (not (empty? @data))))))))

(def ^:private mime-types
  {:pdf "application/pdf"})

(def ^:dynamic *jr-templates-path* "reports")

(defn template->object [name]
  (let [template (some-> (format "%s/%s.jrxml" *jr-templates-path* name)
                         io/resource
                         io/file)]
    (some-> template io/input-stream JasperCompileManager/compileReport)))

(defn data->report [{:keys [name data mtype filename ops report]
                     :or   {mtype :pdf}}]

  (let [report (or report (template->object name))
        j-data (data->jr (or data {:empty true}))
        filled (JasperFillManager/fillReport
                 ^JasperReport report
                 ^Map (HashMap. ^Map (walk/stringify-keys (or ops {})))
                 ^JRDataSource j-data)
        bytes (case mtype
                :pdf (JasperExportManager/exportReportToPdf filled))]

    {:name (or filename (format "%s.%s" name (name mtype)))
     :type (get mime-types mtype)
     :file bytes}))
