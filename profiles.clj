{:dev     {:source-paths   #{"src"}
           :resource-paths ["resources"]

           :target-path    "target/%s"
           :clean-targets  ^{:protect false} [:target-path]

           :plugins        [[lein-ancient "0.6.15"]]
           :dependencies   [[org.clojure/clojure "1.10.0"]
                            ;; Loggers
                            ;; -------
                            [com.taoensso/timbre "4.10.0"]
                            [com.fzakaria/slf4j-timbre "0.3.13"]
                            [org.slf4j/slf4j-api "1.7.26"]
                            [org.slf4j/log4j-over-slf4j "1.7.26"]
                            [org.slf4j/jul-to-slf4j "1.7.26"]
                            [org.slf4j/jcl-over-slf4j "1.7.26"]]}
 :uberjar {:aot :all :jvm-opts ["-Xmx1G"]}}
