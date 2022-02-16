(ns clj-jasper.core
  (:gen-class)
  (:require [clojure.java.io :as io]
            [clojure.walk :as walk]
            [clojure.string :as string])
  (:import
    (clojure.lang ExceptionInfo)
    (java.io ByteArrayOutputStream OutputStream)
    (java.util HashMap Map ArrayList)
    (net.sf.jasperreports.engine JasperCompileManager
                                 JasperFillManager
                                 JRDataSource
                                 JRRewindableDataSource
                                 JRField
                                 JasperReport
                                 JasperPrint DefaultJasperReportsContext)
    (net.sf.jasperreports.engine.export JRPdfExporter)
    (net.sf.jasperreports.export SimpleExporterInput SimpleOutputStreamExporterOutput)))

(defn- data->jr [coll]
  (let [initial (cons nil coll)
        data (atom initial)]
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

(defn- fill [^JasperReport report data parameters]
  (JasperFillManager/fillReport
    report
    ^Map (HashMap. ^Map (walk/stringify-keys (or parameters {})))
    ^JRDataSource (data->jr (or data []))))

(defn- exporter [mtype]
  (case mtype
    :pdf (JRPdfExporter. (DefaultJasperReportsContext/getInstance))
    (throw (ExceptionInfo. (format "Unknown mtype: '%s'" mtype) {:mtype mtype}))))

(defn data->report [{:keys [name data mtype filename ops report]
                     :or   {mtype :pdf}}]
  (let [report (or report (template->object name))
        print (fill report data ops)
        exporter (exporter mtype)
        baos (ByteArrayOutputStream.)]
    (.setExporterInput exporter (SimpleExporterInput. ^JasperPrint print))
    (.setExporterOutput exporter (SimpleOutputStreamExporterOutput. baos))
    (.exportReport exporter)
    {:name (or filename (format "%s.%s" (last (string/split name #"/")) (clojure.core/name mtype)))
     :type (get mime-types mtype)
     :file (.toByteArray baos)}))

(defn render-multiple [^OutputStream output-stream
                       reports
                       {:keys [compile mtype common-parameters]
                        :or   {compile template->object
                               mtype   :pdf}}]
  (let [prints (ArrayList.)
        exporter (exporter mtype)]
    (doseq [{:keys [name data parameters]} reports]
      (.add prints (fill (compile name) data (merge parameters common-parameters))))
    (.setExporterInput exporter (SimpleExporterInput/getInstance prints))
    (.setExporterOutput exporter (SimpleOutputStreamExporterOutput. output-stream))
    (.exportReport exporter)))
