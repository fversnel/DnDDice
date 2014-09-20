(defproject dnddice "2.2.1"
  :description "Clojure library that parses Dungeons & Dragons dice rolls (e.g. '1d20' etc.) and 
rolls them."
  :url "https://github.com/fversnel/DnDDice"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [instaparse "1.3.4"]]
  :main dnddice.command-line)
