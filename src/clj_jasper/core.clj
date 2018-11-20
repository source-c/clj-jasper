(ns clj-jasper.core
  (:require [taoensso.timbre :as log]
            [clojure.java.io :as io]
            [clojure.walk :as walk])
  (:import (java.util HashMap Map)
           (net.sf.jasperreports.engine JasperCompileManager
                                        JasperFillManager
                                        JasperExportManager
                                        JRDataSource
                                        JasperReport)))

(defn -main [& _]
  (log/warn "There is no own runtime!"))

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

(def *jr-templates-path* "reports")

(defn data->report [{:keys [name data mtype filename ops]
                     :or   {mtype :pdf}}]

  (let [template (-> (format "%s/%s.jrxml" *jr-templates-path* name)
                     io/resource
                     io/file)
        report (-> template io/input-stream JasperCompileManager/compileReport)
        j-data (data->jr (or data {:empty true}))
        filled (JasperFillManager/fillReport
                 ^JasperReport report
                 ^Map (HashMap. ^Map (walk/stringify-keys (or ops {})))
                 ^JRDataSource j-data)
        bytes (case mtype
                :pdf (JasperExportManager/exportReportToPdf filled))]

    {:name (or filename (format "%s.%s" name (name mtype)))
     :type (get mtype mime-types)
     :file bytes}))
