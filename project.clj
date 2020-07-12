(defproject org.fversnel/dnddice "3.0.4-SNAPSHOT"
  :description "Clojure library that parses Dungeons & Dragons dice rolls (e.g. '1d20' etc.) and
rolls them."
  :url "https://github.com/fversnel/DnDDice"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [instaparse "1.4.1"]]
  :main org.fversnel.dnddice.commandline
  :aot [org.fversnel.dnddice.commandline])
