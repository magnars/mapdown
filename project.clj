(defproject mapdown "0.2.1"
  :description "A lightweight markup format to turn strings into maps."
  :url "http://github.com/magnars/mapdown"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [stasis "1.0.0"]]
  :profiles {:dev {:dependencies [[midje "1.6.0"]
                                  [test-with-files "0.1.0"]]
                   :plugins [[lein-midje "3.1.3"]]}})
