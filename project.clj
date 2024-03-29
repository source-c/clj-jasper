(defproject net.tbt-post/clj-jasper "0.1.1"
  :description "A Clojure library to work with Jasper Reports"
  :url "https://github.com/source-c/clj-jasper"
  :license {:name "MIT License"}

  :uberjar-merge-with {#"^jasperreports_extension\.properties$" [slurp #(clojure.string/join "\r\n" %&) spit]}

  :dependencies [;; Jasper Libs
                 [net.sf.jasperreports/jasperreports "6.20.0"]
                 [net.sf.jasperreports/jasperreports-fonts "6.20.0"]
                 ;[com.lowagie/itext "2.1.7"]

                 [net.sourceforge.barbecue/barbecue "1.5-beta1"]
                 [com.google.zxing/core "3.5.0"]
                 [net.sf.barcode4j/barcode4j "2.1"]
                 [org.apache.xmlgraphics/batik-bridge "1.16"]])
