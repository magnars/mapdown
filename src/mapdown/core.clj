(ns mapdown.core
  (:require [clojure.string :as str]
            [stasis.core :as stasis]))

(def ^:private keyword-re #"(?m)^:[^ \n\t\r]+")

(def ^:private eighty-dashes
  "--------------------------------------------------------------------------------")

(def ^:private eighty-dashes-re (re-pattern eighty-dashes))

(defn- parse-1 [^String s]
  (when-not (re-find #"^[ \n\t\r]*:" s)
    (throw (Exception. "Mapdown content must start with a key - or the content has nowhere to go.")))
  (zipmap
   (map #(keyword (subs % 1)) (re-seq keyword-re s))
   (map str/trim (drop 1 (str/split s keyword-re)))))

(defn- ordinal [num]
  (if (contains? (set (range 11 14)) (mod num 100))
    (str num "th")
    (let [modulus (mod num 10)]
      (cond
       (= modulus 1) (str num "st")
       (= modulus 2) (str num "nd")
       (= modulus 3) (str num "rd")
       :else (str num "th")))))

(defn- parse-with-index [idx s]
  (try
    (parse-1 s)
    (catch Exception e
      (throw (Exception. (str "Error in " (ordinal (inc idx)) " entry: " (.getMessage e)))))))

(defn- parse-list [^String s]
  (->> (str/split s eighty-dashes-re)
       (map str/trim)
       (remove empty?)
       (map-indexed parse-with-index)))

(defn parse [^String s]
  (let [s (str/trim s)]
    (if (.startsWith s eighty-dashes)
      (parse-list s)
      (parse-1 s))))

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
