(ns mapdown.core
  (:require [clojure.string :as str]))

(def ^:private keyword-re #"(?m)^:[^ \n\t\r]+")

(defn parse [s]
  (when-not (re-find #"^[ \n\t\r]*:" s)
    (throw (Exception. "Mapdown content must start with a key - or the content has nowhere to go.")))
  (zipmap
   (map #(keyword (subs % 1)) (re-seq keyword-re s))
   (map str/trim (drop 1 (str/split s keyword-re)))))

