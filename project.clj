(defproject net.tbt-post/clj-jasper "0.0.8"
  :description "A Clojure library to work with Jasper Reports"
  :url "https://github.com/source-c/clj-jasper"
  :license {:name "MIT License"}

  :dependencies [;; Jasper Libs
                 [net.sf.jasperreports/jasperreports "6.16.0"
                  :exclusions [com.lowagie/itext]]
                 [net.sf.jasperreports/jasperreports-fonts "6.16.0"]
                 [com.lowagie/itext "2.1.7"]
                 [net.sourceforge.barbecue/barbecue "1.5-beta1"]
                 [com.google.zxing/core "3.4.1"]
                 [net.sf.barcode4j/barcode4j "2.1"]
                 [org.apache.xmlgraphics/batik-bridge "1.14"]])
