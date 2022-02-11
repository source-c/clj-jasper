{:dev     {:source-paths   #{"src"}
           :resource-paths ["resources"]

           :target-path    "target/%s"
           :clean-targets  ^{:protect false} [:target-path]

           :dependencies   [[org.clojure/clojure "1.10.3"]
                            ;; Loggers
                            ;; -------
                            [com.taoensso/timbre "5.1.2"]
                            [com.fzakaria/slf4j-timbre "0.3.21"]
                            [org.slf4j/slf4j-api "1.7.36"]
                            [org.slf4j/log4j-over-slf4j "1.7.36"]
                            [org.slf4j/jul-to-slf4j "1.7.36"]
                            [org.slf4j/jcl-over-slf4j "1.7.36"]]}}
