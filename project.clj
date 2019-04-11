(defproject net.tbt-post/clj-jasper "0.0.3"
  :description "A Clojure library to work with Jasper Reports"
  :url "https://github.com/source-c/clj-jasper"
  :license {:name "MIT License"}

  :dependencies [;; Jasper Libs
                 [net.sf.jasperreports/jasperreports "6.7.0"
                  :exclusions [com.lowagie/itext]]
                 [net.sf.jasperreports/jasperreports-fonts "6.0.0"]
                 ;[com.lowagie/itext "4.2.2"]
                 [com.lowagie/itext "2.1.7"]
                 [net.sourceforge.barbecue/barbecue "1.5-beta1"]
                 [org.apache.xmlgraphics/batik-bridge "1.11"]

                 ;; Loggers
                 ;; -------
                 [com.taoensso/timbre "4.10.0"]
                 [com.fzakaria/slf4j-timbre "0.3.12"]
                 [org.slf4j/slf4j-api "1.7.25"]
                 [org.slf4j/log4j-over-slf4j "1.7.25"]
                 [org.slf4j/jul-to-slf4j "1.7.25"]
                 [org.slf4j/jcl-over-slf4j "1.7.25"]])
