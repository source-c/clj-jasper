{:dev     {:source-paths   #{"src"}
           :resource-paths ["resources"]

           :target-path    "target/%s"
           :clean-targets  ^{:protect false} [:target-path]

           :plugins        [[lein-ancient "0.6.15"]]
           :dependencies   [[org.clojure/clojure "1.10.0"]]}
 :uberjar {:aot :all :jvm-opts ["-Xmx1G"]}}
