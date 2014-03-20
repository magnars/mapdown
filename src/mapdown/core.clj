(ns mapdown.core
  (:require [clojure.string :as str]
            [stasis.core :as stasis]))

(def ^:private keyword-re #"(?m)^:[^ \n\t\r]+")

(defn parse [s]
  (when-not (re-find #"^[ \n\t\r]*:" s)
    (throw (Exception. "Mapdown content must start with a key - or the content has nowhere to go.")))
  (zipmap
   (map #(keyword (subs % 1)) (re-seq keyword-re s))
   (map str/trim (drop 1 (str/split s keyword-re)))))

(defn- parse-with-known-path [path contents]
  (try
    (parse contents)
    (catch Exception e
      (throw (Exception. (str "Error when parsing '" path "': " (.getMessage e)))))))

(defn parse-file [path]
  (parse-with-known-path path (slurp path)))

(defn slurp-directory [dir regexp]
  (let [files (stasis/slurp-directory dir regexp)]
    (->> files
         (map (fn [[path contents]]
                [path (parse-with-known-path (str dir path) contents)]))
         (into {}))))
